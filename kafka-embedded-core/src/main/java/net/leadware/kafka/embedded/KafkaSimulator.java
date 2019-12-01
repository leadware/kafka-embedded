package net.leadware.kafka.embedded;

/*-
 * #%L
 * Apache Kafka Embedded Server
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2013 - 2019 Leadware
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.I0Itec.zkclient.ZkClient;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ConsumerGroupListing;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.utils.Time;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import kafka.common.KafkaException;
import kafka.server.KafkaConfig;
import kafka.server.KafkaServer;
import kafka.utils.TestUtils;
import kafka.utils.ZKStringSerializer$;
import kafka.zk.EmbeddedZookeeper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.leadware.bean.validation.ext.tools.FileUtils;
import net.leadware.kafka.embedded.model.ConsumerGroup;
import net.leadware.kafka.embedded.model.ConsumerGroupOffset;
import net.leadware.kafka.embedded.model.Topic;
import net.leadware.kafka.embedded.properties.BrokerProperties;
import net.leadware.kafka.embedded.properties.ListenerProperties;
import net.leadware.kafka.embedded.properties.ListenerProtocolProperties;
import net.leadware.kafka.embedded.properties.SimulatorProperties;
import net.leadware.kafka.embedded.tools.SimulatorUtils;

/**
 * Classe représentant le simulateur KAFKA
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @author <a href="mailto:ltchatch@leadware.net">Guy Landry TCHATCHOUANG (Java EE Developer)</a>
 * @since 23 mars 2019 - 23:06:45
 */
@RequiredArgsConstructor
@Getter
@Slf4j
public class KafkaSimulator {
	
	/**
	 * Hote par defaut
	 */
	private String DEFAULT_HOSTS = "127.0.0.1";
	
	/**
	 * Timeout de connexion à ZooKeeper
	 */
	private static final int ZOOKEEPER_CONNEXION_TIMEOUT = 6000;
	
	/**
	 * Timeout de session à ZooKeeper
	 */
	private static final int ZOOKEEPER_SESSION_TIMEOUT = 6000;
	
	/**
	 * Timeout du client d'administration
	 */
	private static final int DEFAULT_ADMIN_TIMEOUT = 30;
	
	/**
	 * Proprietes de configuration du simulateur
	 */
	private final SimulatorProperties simulatorProperties;
	
	/**
	 * MAP des propriétés communes des instances de brokers
	 */
	private Map<String, String> brokerProperties = null;
	
	/**
	 * Liste de serveurs KAFKA
	 */
	private final List<KafkaServer> kafkaServers = new ArrayList<>();
	
	/**
	 * Serveur embarqué ZooKeeper
	 */
	private EmbeddedZookeeper zookeeper; 
	
	/**
	 * Chaine de connexion à Zookeeper
	 */
	private String zookeeperConnexionUrl;
	
	/**
	 * Client Zookepper
	 */
	private ZkClient zookeeperClient;

	/**
	 * Simulateur Administration Kafka
	 */
	private KafkaAdmin kafkaAdmin;
	
	/**
	 * Client d'administration KAFKA embarqué
	 */
	private AdminClient adminClient;

	/**
	 * Template de production KAFKA (chaine de caractere)
	 */
	private KafkaTemplate<String, String> kafkaStringProducerTemplate;
	
	/**
	 * Répertoire temporaire de stockage
	 */
	private File temporairyDir;
	
	/**
	 * Chemin vers le magasin de clés de confiance
	 */
	private File truststoreLocation;
	
	/**
	 * Chemin vers le magasin de clés
	 */
	private File keystoreLocation;
	
	/**
	 * Ensemble de noms de topics creees
	 */
	private Set<String> createdTopics = new HashSet<>();
	
	/**
	 * Ensemble de ports deja générés pour les brokers
	 */
	private Set<Integer> generatedPorts = new HashSet<>();
	
	/**
	 * Méthode permettant d'initialiser le simulateur KAFKA
	 */
	@PostConstruct
	public void initialize() {
		
		// Log
		log.debug("Initialisation du Simulateur KAFKA");
		
		// Log
		log.debug("Validation des propriétés de configuration du Simulateur KAFKA");
		
		// Validation des contraintes
		simulatorProperties.validate();
		
		// Log
		log.debug("Initialisation du serveur Zookeeper");
		
		// Initialisation de ZooKeeper
		initializeZookeeper();
		
		// Log
		log.debug("Initialisation des brokers configurés");
		
		// Initialize Brokers
		initializeBrokers();
		
		// Log
		log.debug("Initialisation des Topics configurés");
		
		// Initialisation des topics
		initializeTopics();
		
		// Log
		log.debug("Initialisation du producteur par défaut (disponible via les services REST)");
		
		// Initialize internal producers
		initializeProducers();
	}
	
	/**
	 * Méthode permettant d'initialiser les composants zookeeper embarque
	 */
	private void initializeZookeeper() {
		
		// Log
		log.trace("Positionnement [redirection] de la propriété java.io.tmpdir [Nouvelle valeur : {}]", 
					simulatorProperties.getJavaTemporaryDirectory());
		
		// Si la liste des configurations de brokerrs est vide
		System.setProperty("java.io.tmpdir", simulatorProperties.getJavaTemporaryDirectory());
		
		// Log
		log.trace("Instantiation du serveur Zookeeper embarqué");
		
		// Initialisation d'une instance de ZooKeeper
		zookeeper = new EmbeddedZookeeper();
		
		// Log
		log.trace("Construction du ENDPOINT d'écoute du serveur Zookeeper embarqué");
		
		// Initialisation de la ciane de connexion à ZooKeeper
		zookeeperConnexionUrl = "127.0.0.1:".concat(String.valueOf(zookeeper.port()));
		
		// Log
		log.trace("Instantiation d'un client Zookeeper sur l'URL construite [{}]", zookeeperConnexionUrl);
		
		// Initialisation du client ZooKeeper
		zookeeperClient = new ZkClient(zookeeperConnexionUrl, 
									   ZOOKEEPER_SESSION_TIMEOUT, 
									   ZOOKEEPER_CONNEXION_TIMEOUT, 
									   ZKStringSerializer$.MODULE$);
	}
	
	/**
	 * Méthode permettant d'initialiser les brokers
	 */
	private void initializeBrokers() {
		
		// Log
		log.trace("Réinitialisation du cache des serveurs KAFKA");
		
		// Vidage de l'ancienne liste de brokers
		kafkaServers.clear();
		
		// Si la liste des configurations de brokers est vide
		if(simulatorProperties.getBrokerConfigs() == null || 
				simulatorProperties.getBrokerConfigs().isEmpty()) {
			
			// Levée d'une exception
			throw new RuntimeException("Veuillez renseigner la configuration d'au moins un Broker");
		}
			
		
		// Log
		log.trace("Parcours de la liste des configurations de brokers");
		
		// Parcours de la liste de configurations de brokers
		for(int index = 0; index < simulatorProperties.getBrokerConfigs().size(); index++) {
			
			// Log
			log.trace("Obtention de la configuration [{}]", index);
			
			// Obtention de la config
			BrokerProperties currentBrokerProperties = simulatorProperties.getBrokerConfigs().get(index);
			
			// Log
			log.trace("Obtention du port public du Broker [{}]", index);
			
			// Port public du broker
			int publicPort = findPublicPort(currentBrokerProperties);
			
			// Log
			log.trace("Port public du Broker [{}] : [{}]", index, publicPort);
			
			// Log
			log.trace("Calcul du port d'administration en fonction de la configuration");
			
			// Initialisation du port d'admin
			int adminPort = findAdminPort(currentBrokerProperties);
			
			// Log
			log.trace("Port d'administration du Broker [{}] : [{}]", index, adminPort);
			
			// Log
			log.trace("Calcul du port du producteur interne en fonction de la configuration");
			
			// Initialisation du port du producteur interne
			int internalProducerPort = findInternalProducerPort(currentBrokerProperties);
			
			// Log
			log.trace("Port du producteur interne du Broker [{}] : [{}]", index, internalProducerPort);
			
			// Log
			log.trace("Mise à jour de la configuration du broker [{}] avec le port public[{}]", index, publicPort);
						
			// Positionnement du port public
			currentBrokerProperties.getListener().setPort(publicPort);
			
			// Log
			log.trace("Mise à jour de la configuration du broker [{}] avec le port d'administration [{}]", index, adminPort);
			
			// Positionnement du port d'admin
			currentBrokerProperties.getListener().setAdminPort(adminPort);
			
			// Log
			log.trace("Mise à jour de la configuration du broker [{}] avec le port du producteur interne [{}]", index, internalProducerPort);
			
			// Positionnement du port du producteur interne
			currentBrokerProperties.getListener().setInternalProducerPort(internalProducerPort);
			
			// Log
			log.trace("Initialisation des propriétés de configuratio du Broker Kafka [{}]", index);
			
			// Construction des propriétés de base
			Properties properties = createBrokerProperties(index, currentBrokerProperties);
			
			// Log
			log.trace("Création d'un serveur KAFKA sur la base de des propriétés initialisées [{}]", properties);
			
			// Création d'un serveur Kafka
			KafkaServer server = TestUtils.createServer(new KafkaConfig(properties), Time.SYSTEM);
			
			// Log
			log.trace("Ajout du Broker Kafka [{}] dans le cache de serveur", index);
			
			// Ajout du serveur Kafka dans la liste des serveurs
			kafkaServers.add(server);
		}
		
		// Log
		log.trace("Initialisation de la MAP de configuration du client d'administration des Brokers KAFKA");
		
		// MAP des configurations administrateur
		Map<String, Object> adminConfigs = new HashMap<>();
		
		// Log
		log.trace("Ajout de la liste des URLs de Brokers dans la configuration du client d'administration [{}]", getAdminBrokersUrls());
		
		// Positionnement des URL de serveurs
		adminConfigs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, getAdminBrokersUrls());
		
		// Log
		log.trace("Ajout de l'identifiant du client administration du simulateur");
		
		// Positionnement de l'ID du client d'admin
		adminConfigs.put(AdminClientConfig.CLIENT_ID_CONFIG, "simulator-admin-client");
		
		// Log
		log.trace("Création du client d'administration");
		
		// Instanciation du client d.administration
		adminClient = AdminClient.create(adminConfigs);
	}

	/**
	 * Methode de recherche du port du producteur interne du broker
	 * @param brokerProperties	Proprietes du Broker
	 * @return Port du producteur interne
	 */
	private int findInternalProducerPort(BrokerProperties brokerProperties) {
		
		// Port a retourner
		int port = brokerProperties.getListener().getInternalProducerPort();
		
		// Si le port du producteur interne est <= 0
		if(port <= 0) {
			
			// Recuperation du premier port libre autre que le port d'écoute public du broker
			port = SimulatorUtils.findAvailablePortExcept(generatedPorts);
		}
		
		// Ajout du port généné dans la collection des ports déjà générés
		generatedPorts.add(port);
		
		// On retourne le port
		return port;
	}
	
	/**
	 * Methode de recherche du port d'écoute du brocer
	 * @param brokerProperties Propriétés du broker
	 * @return Port public d'écoute du broker
	 */
	private int findPublicPort(BrokerProperties brokerProperties) {
		
		//Port venant du fichier de configuration
		int port = brokerProperties.getListener().getPort();
		
		//Si le port est inférieur ou égal à 0
		if(port <= 0) {
			
			// Recherche et récupération d'un port libre sur le système
			port = SimulatorUtils.findAvailablePortExcept(generatedPorts);
		}
		
		// Ajout du port généné dans la collection des ports déjà générés
		generatedPorts.add(port);
		
		//On retourne le port
		return port;
	}
	
	/**
	 * Methode de recherche du port d'administration du broker
	 * @param brokerProperties	Proprietes du Broker
	 * @return Port d'administration
	 */
	private int findAdminPort(BrokerProperties brokerProperties) {
		
		// Port a retourner
		int port = brokerProperties.getListener().getAdminPort();
		
		// Si le port du producteur interne est <= 0
		if(port <= 0) {
			
			// Recuperation du premier port libre autre que le port d'écoute public du broker
			port = SimulatorUtils.findAvailablePortExcept(generatedPorts);
		}
		
		// Ajout du port généné dans la collection des ports déjà générés
		generatedPorts.add(port);
		
		// On retourne le port
		return port;
	}
	
	/**
	 * Méthode permettant d'initialiser les propriétés communes des brokers 
	 * @param brokerId	ID du broker (On utilisera son numéro d'ordre dans la liste de configuration)
	 * @param brokerConfig current broker configuration
	 * @return	Liste de proprietes
	 */
	private Properties createBrokerProperties(int brokerId, BrokerProperties brokerConfig) {
		
		// Instantiation des proprietes
		Properties properties = new Properties();

		// Broker Zookeeper URL
		properties.setProperty(KafkaConfig.ZkConnectProp(), zookeeperConnexionUrl);
		
		// Broker ID Generation Enabled
		properties.setProperty(KafkaConfig.BrokerIdGenerationEnableProp(), String.valueOf(true));
		
		// Broker ID
		properties.setProperty(KafkaConfig.BrokerIdProp(), String.valueOf(brokerId));
		
		// Broker Control Shut down
		properties.setProperty(KafkaConfig.ControlledShutdownEnableProp(), String.valueOf(simulatorProperties.getControlledShutdown()));

		// Broker Network Thread
		properties.setProperty(KafkaConfig.NumNetworkThreadsProp(), String.valueOf(simulatorProperties.getNetworkThreadCount()));

		// Broker IO Threads
		properties.setProperty(KafkaConfig.NumIoThreadsProp(), String.valueOf(simulatorProperties.getIoThreadCount()));
		
		// Broker Port
		properties.setProperty(KafkaConfig.PortProp(), String.valueOf(brokerConfig.getListener().getPort()));
		
		// Listeners
		properties.setProperty(KafkaConfig.ListenersProp(), getListenerAllUrls(brokerConfig.getListener()));
		
		// Inter Broker Listeners
		properties.setProperty(KafkaConfig.InterBrokerSecurityProtocolProp(), brokerConfig.getListener().getProtocol().getScheme().getValue());
		
		// Broker Socket Send Buffer
		properties.setProperty(KafkaConfig.SocketSendBufferBytesProp(), String.valueOf(simulatorProperties.getSendBufferSize()));
		
		// Broker Socket Receive Buffer
		properties.setProperty(KafkaConfig.SocketReceiveBufferBytesProp(), String.valueOf(simulatorProperties.getReceiveBufferSize()));
		
		// Broker Socket Max Request Size
		properties.setProperty(KafkaConfig.SocketRequestMaxBytesProp(), String.valueOf(simulatorProperties.getMaxRequestSize()));
		
		// Broker Number of partition per topic
		properties.setProperty(KafkaConfig.NumPartitionsProp(), String.valueOf(simulatorProperties.getPartitionCount()));
		
		// Broker Logs dirs
		properties.setProperty(KafkaConfig.LogDirsProp(), 
							   brokerConfig.getLogsDirectories()
							   			   .stream()
							   			   .map(logDirectory -> {
							   				   
							   				   // On retourne le chemin resolu
							   				   return FileUtils.getResolvedPath(logDirectory);
							   			   })
							   			   .collect(Collectors.joining(",")));
		
		// Broker Log dir
		properties.setProperty(KafkaConfig.LogDirProp(), FileUtils.getResolvedPath(brokerConfig.getLogsDirectory()));

		// Broker Log Log Flush Interval
		properties.setProperty(KafkaConfig.LogFlushIntervalMsProp(), String.valueOf(1000));
		
		// Broker Log Flush scheduler
		properties.setProperty(KafkaConfig.LogFlushSchedulerIntervalMsProp(), String.valueOf(1000));
		
		// Broker Log Retention Time in minute
		properties.setProperty(KafkaConfig.LogRetentionTimeMinutesProp(), String.valueOf(30));
		
		// Broker Log File size
		properties.setProperty("log.file.size", String.valueOf(536870912));
		
		// Broker Log dir
		properties.setProperty(KafkaConfig.LogCleanupIntervalMsProp(), String.valueOf(60000));
		
		// Timeout sur la socket de réplication
		properties.setProperty(KafkaConfig.ReplicaSocketTimeoutMsProp(), "1000");
		
		// Timeout sur la socket controleur
		properties.setProperty(KafkaConfig.ControllerSocketTimeoutMsProp(), "1000");
		
		// Facteur de réplication de topics
		properties.setProperty(KafkaConfig.OffsetsTopicReplicationFactorProp(), "1");
		
		// ReplicaHighWatermarkCheckpointIntervalMsProp
		properties.setProperty(KafkaConfig.ReplicaHighWatermarkCheckpointIntervalMsProp(),
							   String.valueOf(Long.MAX_VALUE));
		
		// Ajout etat activation SSL Enabled
		properties.setProperty(KafkaConfig.SslEnabledProtocolsProp(), SslConfigs.DEFAULT_SSL_ENABLED_PROTOCOLS);
		
		// Si la configuration de truststore est definie
		if(simulatorProperties.getTruststoreConfig() != null) {
			
			// Ajout Truststore location
			properties.setProperty(KafkaConfig.SslTruststoreLocationProp(), 
								   FileUtils.getResolvedPath(simulatorProperties.getTruststoreConfig().getLocation()));
			
			// Ajout Truststore password
			properties.setProperty(KafkaConfig.SslTruststorePasswordProp(), 
								   simulatorProperties.getTruststoreConfig().getPassword());
			
			// Ajout Truststore type
			properties.setProperty(KafkaConfig.SslTruststoreTypeProp(), 
								   simulatorProperties.getTruststoreConfig().getType().getValue());
			
			// Ajout KeyManager Algorithm
			properties.setProperty(KafkaConfig.SslKeyManagerAlgorithmProp(), 
								   simulatorProperties.getTruststoreConfig().getKeymanagerAlgorithm().getValue());
		}
		
		// Si la configuration de Keystore est definie
		if(simulatorProperties.getKeystoreConfig() != null) {

			// Ajout Truststore location
			properties.setProperty(KafkaConfig.SslKeystoreLocationProp(), 
								   FileUtils.getResolvedPath(simulatorProperties.getKeystoreConfig().getLocation()));
			
			// Ajout Keystore password
			properties.setProperty(KafkaConfig.SslKeystorePasswordProp(), 
								   simulatorProperties.getKeystoreConfig().getPassword());
			
			// Ajout Key password
			properties.setProperty(KafkaConfig.SslKeyPasswordProp(), 
								   simulatorProperties.getKeystoreConfig().getKeyPassword());
			
			// Ajout Keystore type
			properties.setProperty(KafkaConfig.SslKeystoreTypeProp(), 
								   simulatorProperties.getKeystoreConfig().getType().getValue());
			
			// Ajout KeyManager Algorithm
			properties.setProperty(KafkaConfig.SslKeyManagerAlgorithmProp(), 
								   simulatorProperties.getKeystoreConfig().getKeymanagerAlgorithm().getValue());
		}
		
		// Ajout Client Auth
		properties.setProperty(KafkaConfig.SslClientAuthProp(), 
							   simulatorProperties.getSslClientAuthentication().getValue().toLowerCase());
		
		// Ajout Protocole SSL
		properties.setProperty(KafkaConfig.SslProtocolProp(), 
							   simulatorProperties.getSslProtocol().getValue());
		
		// Ajout Map des protocoles
		properties.setProperty(KafkaConfig.ListenerSecurityProtocolMapProp(), 
							   getListenerProtocolMap(simulatorProperties));
		
		// On retourne le liste
		return properties;
	}
		
	/**
	 * Méthode permettant d'initialiser les topics
	 */
	private void initializeTopics() {
		
		// Log
		log.trace("Réinitialisation de la liste des topics crées");
		
		// Vidage de la liste de topics
		createdTopics.clear();
		
		// Log
		log.trace("Création de la liste de topics [{}]", simulatorProperties.getInitialTopics());
		
		// Creation des topic de la liste parametrée
		internalCreateTopics(simulatorProperties.getInitialTopics());
	}
	
	/**
	 * Méthode permettant d'initialiser les producteurs de messages
	 */
	private void initializeProducers() {
		
		// Propriétés du producer
		Map<String, Object> producerProperties = new HashMap<>();
		
		// Positionnement des URLs de serveurs KAFKA
		producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, getInternalProducerBrokersUrls());
		
		// Positionnement de l'ID du client
		producerProperties.put(ProducerConfig.CLIENT_ID_CONFIG, "simulator-string-producer");
		
		// Positionnement de la classe de secrialisation des clés
		producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		
		// Positionnement de la classe de serialisation des données
		producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		
		// Fabrique de producteurs
		ProducerFactory<String, String> kafkaProducerFactory = new DefaultKafkaProducerFactory<String, String>(producerProperties);
		
		// Template de production Kafka
		kafkaStringProducerTemplate = new KafkaTemplate<>(kafkaProducerFactory);
	}
	
	
	/**
	 * Méthode permettant d'exécuter ine action sur le broker en mode Admin 
	 * @param callback	Callback de l'action
	 */
	private void doWithAdmin(Consumer<AdminClient> callback) {
		
		try {
			
			// Execution de l'operation du callback avec cet client administration
			callback.accept(adminClient);
			
		} catch (Exception e) {
			
			// Affichage de la trace
			e.printStackTrace();
		}
	}

	/**
	 * Méthode permettant d'obtenir la chaine de correspodances entre noms de protocole et protocole 
	 * @param simulatorProperties	Proprietes du simulateur
	 * @return	Chaine de correspondance
	 */
	private String getListenerProtocolMap(SimulatorProperties simulatorProperties) {
		
		// On calcule er retourne la chaine de correspondance
		return simulatorProperties.getBrokerConfigs()
							      .stream()
							      .map(brokerConfig -> {
							    	  
							    	  // Obtention du Protocole publique
							    	  ListenerProtocolProperties protocole = brokerConfig.getListener().getProtocol();
							    	  
							    	  // Obtention du Protocole Admin
							    	  ListenerProtocolProperties adminProtocole = brokerConfig.getListener().getAdminProtocol();
							    	  
							    	  // Obtention du Protocole Producteur
							    	  ListenerProtocolProperties producerProtocole = brokerConfig.getListener().getInternalProducerProtocol();
							    	  
							    	  // Constructeur de chaines
							    	  StringBuilder mapBuilder = new StringBuilder();
							    	  
							    	  // Ajout de la correspondance publique
							    	  mapBuilder.append(protocole.getName() + ":" + protocole.getScheme().getValue());
							    	  
							    	  // Ajout de la correspondance admin
							    	  mapBuilder.append("," + adminProtocole.getName() + ":" + adminProtocole.getScheme().getValue());
							    	  
							    	  // Ajout de la correspondance producteur
							    	  mapBuilder.append("," + producerProtocole.getName() + ":" + producerProtocole.getScheme().getValue());
							    	  
							    	  // On retourne la chaine de correspondance pour ce protocole
							    	  return mapBuilder.toString();
							      })
							      .collect(Collectors.joining(","));
		
	}
	
	/**
	 * Méthode permettant d'obtenir l'ensemble des URL publique des Brokers 
	 * @return	URLs publique des brokers
	 */
	public String getPublicBrokersUrls() {
		
		// On retourne la chiane
		return simulatorProperties.getBrokerConfigs()
								  .parallelStream()
								  .map(brokerProperty -> getListenerPublicUrl(brokerProperty.getListener()))
								  .collect(Collectors.joining(",", "", ""));
	}
	
	/**
	 * Méthode permettant d'obtenir l'ensemble des URL d'administration des Brokers 
	 * @return	URLs d'administration des brokers
	 */
	public String getAdminBrokersUrls() {
		
		// On retourne la chiane
		return simulatorProperties.getBrokerConfigs()
								  .parallelStream()
								  .map(brokerProperty -> getListenerAdminUrl(brokerProperty.getListener()))
								  .collect(Collectors.joining(",", "", ""));
	}
	
	/**
	 * Méthode permettant d'obtenir l'ensemble des URL de producteur interne des Brokers 
	 * @return	URLs de producteur interne des brokers
	 */
	public String getInternalProducerBrokersUrls() {
		
		// On retourne la chiane
		return simulatorProperties.getBrokerConfigs()
								  .parallelStream()
								  .map(brokerProperty -> getListenerInternalProducerUrl(brokerProperty.getListener()))
								  .collect(Collectors.joining(",", "", ""));
	}
	
	/**
	 * Méthode permettant d'obtenir l'ensemble des URL du listener du Broker sous forme de chaine de caracteres
	 * @param listener Proprietes du listener source 
	 * @return	Urls du Brokers
	 */
	private String getListenerAllUrls(ListenerProperties listener) {
		
		// Constructeur de chaines
		StringBuilder builder = new StringBuilder();
		
		// Ajout du host public
		builder.append(getListenerPublicUrl(listener));
		
		// Ajout du host d'administration
		builder.append(",")
			   .append(getListenerAdminUrl(listener));
		
		// Ajout du host producteur interne
		builder.append(",")
			   .append(getListenerInternalProducerUrl(listener));
		
		// On retourne la chiane
		return builder.toString();
	}
	
	/**
	 * Méthode permettant d'obtenir l'URL publique du Broker sous forme de chaine de caracteres
	 * @param listener Proprietes du listener source 
	 * @return	Url publique du Broker
	 */
	private String getListenerPublicUrl(ListenerProperties listener) {
		
		// Constructeur de chaines
		StringBuilder builder = new StringBuilder();
		
		// Ajout du host d'administration
		builder.append(listener.getProtocol().getName())
			   .append("://")
			   .append(DEFAULT_HOSTS)
			   .append(":")
			   .append(listener.getPort());
		
		// On retourne la chiane
		return builder.toString();
	}
	
	/**
	 * Méthode permettant d'obtenir l'URL d'administration du Broker sous forme de chaine de caracteres
	 * @param listener Proprietes du listener source 
	 * @return	Url d'administration du Broker
	 */
	private String getListenerAdminUrl(ListenerProperties listener) {
		
		// Constructeur de chaines
		StringBuilder builder = new StringBuilder();
		
		// Ajout du host d'administration
		builder.append(listener.getAdminProtocol().getName())
			   .append("://")
			   .append(DEFAULT_HOSTS)
			   .append(":")
			   .append(listener.getAdminPort());
		
		// On retourne la chiane
		return builder.toString();
	}
	
	/**
	 * Méthode permettant d'obtenir l'URL du consomateur interne du Broker sous forme de chaine de caracteres
	 * @param listener Proprietes du listener source  
	 * @return	Url du consomateur interne du Broker
	 */
	private String getListenerInternalProducerUrl(ListenerProperties listener) {
		
		// Constructeur de chaines
		StringBuilder builder = new StringBuilder();
		
		// Ajout du host producteur interne
		builder.append(listener.getInternalProducerProtocol().getName())
			   .append("://")
			   .append(DEFAULT_HOSTS)
			   .append(":")
			   .append(listener.getInternalProducerPort());
		
		// On retourne la chiane
		return builder.toString();
	}
	
	/**
	 * Méthode interne permettant de créer une liste de topics à partir de leur noms
	 * @param topicsNames	Liste des noms de topics
	 */
	private void internalCreateTopics(List<String> topicsNames) {
		
		// Execution en tant qu'admin
		doWithAdmin(adminClient -> {
			
			// Creation de topics
			createTopics(adminClient,
						 
						 // Stream sur la liste des noms de topics
						 topicsNames.stream()
						 			
						 			// Transformation des noms en Topics
						 			.map(topicName -> new NewTopic(topicName,
						 										   simulatorProperties.getPartitionCount(), 
						 										   (short) simulatorProperties.getBrokerConfigs().size()))
						 			
						 			// Collecte dans une liste
						 			.collect(Collectors.toList()));
		});
	}
	
	/**
	 * Méthode permettant de créer une liste de topics 
	 * @param admin	Client d'administration
	 * @param newTopics	Liste de topics a creer
	 */
	private void createTopics(AdminClient admin, List<NewTopic> newTopics) {
		
		// Verifier que ZooKeeper est actif
		Assert.notNull(this.zookeeper, "Assurez-vous que le cluster ZooKeeper est actif avant toute opération.");
		
		// Parcours
		for (NewTopic topic : newTopics) {
			
			// Si le topic est deja crée
			Assert.isTrue(createdTopics.add(topic.name()), () -> "Ce topic existe déjà : " + topic.name());
		}
		
		// Execution de la commande de creation des topics
		CreateTopicsResult createTopics = admin.createTopics(newTopics);
		
		try {
			
			// Attente de la creation des topics ou de la fin du timeout
			createTopics.all().get(DEFAULT_ADMIN_TIMEOUT, TimeUnit.SECONDS);
			
		} catch (Exception e) {
			
			// Print exception stack trace
			log.error("createTopics - Exception survenue", e);
			
			// On leve ue exception
			throw new KafkaException(e);
		}
	}
	
	/**
	 * Méthode permettant de créer une liste de topics à partir de leur noms
	 * @param topicsNames	Tableau de noms de topics
	 */
	public void createTopics(String...topicsNames) {
		
		// Log
		log.debug("Création des topics [{}]", Arrays.asList(topicsNames));
		
		// Creation de la liste de topics
		internalCreateTopics(
				// Stream sur la liste des noms de topics
				Arrays.stream(topicsNames).collect(Collectors.toList())
		);
	}
	
	/**
	 * Methode permettant de renvoyé une liste de ports publics
	 * pour les brokers actif, si l'utilisateur n'en a pas pécisé 
	 * @return Liste de ports
	 */
	public List<Integer> getPublicPorts() {
		
		// Verifier que ZooKeeper est actif
		Assert.notNull(this.zookeeper, "Assurez-vous que le cluster ZooKeeper est actif avant toute opération.");
		
		//Initialisation d'une liste
		List<Integer> list = new ArrayList<Integer>();

		//Parcour de la liste des brokerConfig
		for(BrokerProperties brokerProperties: simulatorProperties.getBrokerConfigs()) {
			
			//Ajout d'un port dans la liste
			list.add(brokerProperties.getListener().getPort());
		}
		
		try {
			//On retourne la liste
			return list;
			
		} catch(Exception e) {
			
			// Print exception stack trace
			log.error("getPublicPorts - Exception survenue", e);
			
			// On relance
			throw new KafkaException(e);
		}
		
	}
	
	/**
	 * Méthode permettant de lister les topics 
	 * @return	Liste de topics
	 */
	public List<Topic> listTopics() {
		
		// On retourne la liste
		return listTopics(false);
	}
	
	/**
	 * Méthode permettant de lister les topics 
	 * @param internal Etat interne ou non du topic
	 * @return	Liste de topics
	 */
	public List<Topic> listTopics(boolean internal) {
		
		// Verifier que ZooKeeper est actif
		Assert.notNull(this.zookeeper, "Assurez-vous que le cluster ZooKeeper est actif avant toute opération.");
		
		// Option de recherche
		ListTopicsOptions options = new ListTopicsOptions().listInternal(internal);
		
		// Future
		KafkaFuture<Collection<TopicListing>> topicListingFuture = adminClient.listTopics(options).listings();
		
		try {
			
			// Attendre le resultat
			return topicListingFuture.get(DEFAULT_ADMIN_TIMEOUT, TimeUnit.SECONDS)
									 .stream().map(topicListing -> new Topic(topicListing.name(), topicListing.isInternal()))
									 		  .collect(Collectors.toList());
			
		} catch (Exception e) {
			
			// Print exception stack trace
			log.error("listTopics - Exception survenue", e);
			
			// On relance
			throw new KafkaException(e);
			
		}
	}
	
	/**
	 * Méthode permettant de supprimer les topics 
	 * @param topicsNames	Liste des noms des topics a supprimer
	 */
	public void deleteTopics(List<String> topicsNames) {
		
		// Log
		log.debug("Suppression des topics [{}]", topicsNames);
		
		// Verifier que ZooKeeper est actif
		Assert.notNull(this.zookeeper, "Assurez-vous que le cluster ZooKeeper est actif avant toute opération.");

		// Parcours
		for (String topicName : topicsNames) {
			
			// Si le topic est deja crée
			Assert.isTrue(createdTopics.remove(topicName), () -> "Ce topic n'existe pas : " + topicName);
		}
		
		// Requete de suppression des topics
		DeleteTopicsResult result = adminClient.deleteTopics(topicsNames);
		
		try {
			
			// Attendre la fin de la suppression
			result.all().get(DEFAULT_ADMIN_TIMEOUT, TimeUnit.SECONDS);
			
		} catch (Exception e) {
			
			// Print exception stack trace
			log.error("deleteTopics - Exception survenue", e);
			
			// On relance
			throw new KafkaException(e);
			
		}
	}
	
	/**
	 * Méthode permettant de supprimer une liste de noms de topics 
	 * @param topicsNames	Liste de noms de topics
	 */
	public void deleteTopics(String...topicsNames) {
		
		// Log
		log.debug("Suppression des topics [{}]", Arrays.asList(topicsNames));
		
		// Suppression
		deleteTopics(Arrays.stream(topicsNames).collect(Collectors.toList()));	
	}
	
	/**
	 * Méthode permettant de lister les groupes de consommateurs 
	 * @return	Liste de groupes de consommateurs
	 */
	public List<ConsumerGroup> listConsumerGroup() {
		
		// Verifier que ZooKeeper est actif
		Assert.notNull(this.zookeeper, "Assurez-vous que le cluster ZooKeeper est actif avant toute opération.");
		
		// Future
		KafkaFuture<Collection<ConsumerGroupListing>> consumerGroupsListingFuture = adminClient.listConsumerGroups().all();
		
		try {
			
			// Attendre le resultat
			return consumerGroupsListingFuture.get(DEFAULT_ADMIN_TIMEOUT, TimeUnit.SECONDS)
									 		  .stream().map(consumerGroupsListing -> new ConsumerGroup(consumerGroupsListing.groupId(), consumerGroupsListing.isSimpleConsumerGroup()))
									 		  .collect(Collectors.toList());
			
		} catch (Exception e) {
			
			// Print exception stack trace
			log.error("listConsumerGroup - Exception survenue", e);
			
			// On relance
			throw new KafkaException(e);
			
		}
	}

	/**
	 * Méthode permettant de lister les offsets d'un groupes de consommateurs 
	 * @param groupId ID du groupe de consommateurs
	 * @return	Liste des offsets d'un groupes de consommateurs
	 */
	public List<ConsumerGroupOffset> listConsumerGroupOffsets(String groupId) {
		
		// Verifier que ZooKeeper est actif
		Assert.notNull(this.zookeeper, "Assurez-vous que le cluster ZooKeeper est actif avant toute opération.");
		
		// Future
		KafkaFuture<Map<TopicPartition, OffsetAndMetadata>> consumerGroupsOffsetsMapFuture = adminClient.listConsumerGroupOffsets(groupId).partitionsToOffsetAndMetadata();
		
		try {
			
			// Attendre le resultat
			return consumerGroupsOffsetsMapFuture.get(DEFAULT_ADMIN_TIMEOUT, TimeUnit.SECONDS)
												 .entrySet()
									 		  	 .stream().map(consumerGroupsOffsetEntry -> {
									 		  		 
									 		  		 // Obtention du Topic
									 		  		 TopicPartition topic = consumerGroupsOffsetEntry.getKey();
									 		  		 
									 		  		 // Obtention de l'offset
									 		  		 OffsetAndMetadata offset = consumerGroupsOffsetEntry.getValue();
									 		  		 
									 		  		 // On retourne le groupe de consumer
									 		  		 return new ConsumerGroupOffset(topic.topic(), topic.partition(), offset.offset(), offset.metadata());
									 		  	 })
									 		  .collect(Collectors.toList());
			
		} catch (Exception e) {
			
			// Print exception stack trace
			log.error("listConsumerGroupOffsets - Exception survenue", e);
			
			// On relance
			throw new KafkaException(e);
			
		}
	}
	
	/**
	 * Méthode permettant d'envoyer un message via le simulateur KAFKA
	 * @param topic	Topic d'envoi
	 * @param key	Clé du message
	 * @param message	Contenu du message
	 */
	public void sendMessage(String topic, String key, String message) {
		
		// Log
		log.debug("Envoie d'un message Clé [{}], Topic [{}], Contenu [{}]", key, topic, message);
		
		// Envoie du message
		kafkaStringProducerTemplate.send(topic, key, message);
	}

	/**
	 * Méthode permettant d'envoyer un message via le simulateur KAFKA
	 * @param topic	Topic d'envoi
	 * @param message	Contenu du message
	 */
	public void sendMessage(String topic, String message) {
		
		// Log
		log.debug("Envoie d'un message Topic [{}], Contenu [{}]", topic, message);
		
		// Envoie du message
		kafkaStringProducerTemplate.send(topic, message);
	}
	
	/**
	 * Méthode permettant d'arreter l'ensemble des brokers
	 */
	private void stopBrokers() {
		
		// Log
		log.debug("Arrêt des brokers");
		
		// Si la liste de brokers est vide
		if(CollectionUtils.isEmpty(kafkaServers)) {
			
			// Sortie du traitement
			return;
		}
		
		// Parcours et arrêt des brokers de la liste
		kafkaServers.forEach(this::stopBroker);
		
		// Vidage de la liste de brokers
		kafkaServers.clear();
	}
	
	/**
	 * Méthode permettant d'arrêter un broker
	 * @param broker	Broker à arreter
	 */
	private void stopBroker(KafkaServer broker) {
		
		// Si le broker est non null
		if(broker != null) {
			
			try {
				
				// Tentative d'arret
				broker.shutdown();
				
			} catch (Exception e) {
				
				// Affichage de l'erreur
				log.error("stopBroker - Erreur survenue", e);
			}
		}
	}
	
	/**
	 * Méthode permettant de détruire le simulateur KAFKA
	 */
	@PreDestroy
	public void destroy() {
		
		try {
			
			// Fermeture du clien d'administration
			adminClient.close(DEFAULT_ADMIN_TIMEOUT, TimeUnit.SECONDS);
			
		} catch (Exception e) {
			
			// Affichage de l'erreur
			log.error("destroy - Erreur survenue lors de l'arrêt du client d'administration", e);
		}
		
		try {
			
			// Tentative de fermeture du Client ZooKeeper
			zookeeperClient.close();
			
		} catch (Exception e) {
			
			// Affichage de l'erreur
			log.error("destroy - Erreur survenue lors de l'arrêt du client zookeeper", e);
		}

		// Arret des Kafka Servers
		this.stopBrokers();
		
		try {
			
			// Tentative d'arret de ZooKeeper
			zookeeper.shutdown();
			
		} catch (Exception e) {
			
			// Affichage de l'erreur
			log.error("destroy - Erreur survenue lors de l'arrêt du serveur zookeeper", e);
		}
	}
}
