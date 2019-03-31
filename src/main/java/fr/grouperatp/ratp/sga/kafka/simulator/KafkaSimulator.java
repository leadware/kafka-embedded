/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator;

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
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.security.auth.SecurityProtocol;
import org.apache.kafka.common.utils.Time;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

import fr.grouperatp.ratp.sga.kafka.simulator.properties.BrokerProperties;
import fr.grouperatp.ratp.sga.kafka.simulator.properties.ListenerProtocol;
import fr.grouperatp.ratp.sga.kafka.simulator.properties.SimulatorProperties;
import kafka.common.KafkaException;
import kafka.server.KafkaConfig;
import kafka.server.KafkaServer;
import kafka.utils.TestUtils;
import kafka.utils.ZKStringSerializer$;
import kafka.zk.EmbeddedZookeeper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Classe représentant le simulateur KAFKA
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 23 mars 2019 - 23:06:45
 */
@RequiredArgsConstructor
@Getter
public class KafkaSimulator {
	
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
	 * Méthode permettant d'initialiser le simulateur KAFKA
	 */
	@PostConstruct
	public void initialize() {
		
		// Validation des contraintes
		simulatorProperties.validate();
		
		// Initialisation de ZooKeeper
		initializeZookeeper();
		
		// Initialize Brokers
		initializeBrokers();
		
		// Initialisation des topics
		initializeTopics();
	}
	
	/**
	 * Méthode permettant d'initialiser les composants zookeeper embarque
	 */
	private void initializeZookeeper() {

		// Si la liste des configurations de brokerrs est vide
		System.setProperty("java.io.tmpdir", simulatorProperties.getJavaTemporaryDirectory());
		
		// Initialisation d'une instance de ZooKeeper
		zookeeper = new EmbeddedZookeeper();
		
		// Initialisation de la ciane de connexion à ZooKeeper
		zookeeperConnexionUrl = "127.0.0.1:".concat(String.valueOf(zookeeper.port()));
		
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
		
		// Vidage de l'ancienne liste de brokers
		kafkaServers.clear();
		
		// Si la liste des configurations de brokers est vide
		if(simulatorProperties.getBrokerConfigs() == null || 
				simulatorProperties.getBrokerConfigs().isEmpty()) 
			throw new RuntimeException("Veuillez renseigner la configuration d'au moins un Broker");
		
		// Parcours de la liste de configurations de brokers
		for(int index = 0; index < simulatorProperties.getBrokerConfigs().size(); index++) {
			
			// Obtention de la config
			BrokerProperties brokerProperties = simulatorProperties.getBrokerConfigs().get(index);
			
			// Construction des propriétés de base
			Properties properties = createBrokerProperties(index, brokerProperties);
			
			// Création d'un serveur Kafka
			KafkaServer server = TestUtils.createServer(new KafkaConfig(properties), Time.SYSTEM);
			
			// Ajout du serveur Kafka dans la liste des serveurs
			kafkaServers.add(server);
			
		}
		
		// MAP des configurations administrateur
		Map<String, Object> adminConfigs = new HashMap<>();
		
		// Positionnement des URL de serveurs
		adminConfigs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, getBrokersAsString());
		
		// Instanciation du client d.administration
		adminClient = AdminClient.create(adminConfigs);
	}
	
	/**
	 * Méthode permettant d'initialiser les propriétés communes des brokers 
	 * @param brokerId	ID du broker (On utilisera son numéro d'ordre dans la liste de configuration)
	 * @param brokerConfig current broker configuration
	 * @return	Liste de proprietes
	 */
	private Properties createBrokerProperties(int brokerId, BrokerProperties brokerConfig) {

		// Construction des propriétés de base
		Properties properties = TestUtils.createBrokerConfig(brokerId,
															 zookeeperConnexionUrl,
															 simulatorProperties.getControlledShutdown(),
															 simulatorProperties.getEnableDeleteTopics(), brokerConfig.getListener().getPort(),
															 scala.Option.apply(SecurityProtocol.valueOf(brokerConfig.getListener().getProtocol().getValue())),
															 scala.Option.apply(truststoreLocation),
															 scala.Option.apply(null),
															 
															 // Activation du PLAINTEXT
															 brokerConfig.getListener().getProtocol().equals(ListenerProtocol.PLAINTEXT),
															 
															 // Activation du SASL
															 false, 
															 
															 // Port SASL
															 0, 
															 
															 // Activation du SSL
															 brokerConfig.getListener().getProtocol().equals(ListenerProtocol.SSL),
															 
															 // Port SSL
															 brokerConfig.getListener().getPort(),
															 
															 // Activation du SASLSSL
															 false,
															 
															 // Port SASLSSL
															 0,
															 
															 // Options RACK
															 scala.Option.apply(null),
															 
															 // Nombre de Log Dirs
															 brokerConfig.getLogsDirectories().size(),
															 
															 // Activation d'un Token
															 false);
		
		// Timeout sur la socket de réplication
		properties.setProperty(KafkaConfig.ReplicaSocketTimeoutMsProp(), "1000");
		
		// Timeout sur la socket controleur
		properties.setProperty(KafkaConfig.ControllerSocketTimeoutMsProp(), "1000");
		
		// Facteur de réplication de topics
		properties.setProperty(KafkaConfig.OffsetsTopicReplicationFactorProp(), "1");
		
		// ReplicaHighWatermarkCheckpointIntervalMsProp
		properties.setProperty(KafkaConfig.ReplicaHighWatermarkCheckpointIntervalMsProp(),
							   String.valueOf(Long.MAX_VALUE));
		
		// Ajout du Nombre de Threads d'entree/sortie
		properties.setProperty(KafkaConfig.NumIoThreadsProp(), 
							   String.valueOf(simulatorProperties.getIoThreadCount()));
		
		// Ajout du Nombre de Threads network
		properties.setProperty(KafkaConfig.NumNetworkThreadsProp(), 
							   String.valueOf(simulatorProperties.getNetworkThreadCount()));
		
		// Ajout Send Buffer Size
		properties.setProperty(KafkaConfig.SocketSendBufferBytesProp(), 
							   String.valueOf(simulatorProperties.getSendBufferSize()));
		
		// Ajout du LogDir
		properties.setProperty(KafkaConfig.LogDirProp(), brokerConfig.getLogsDirectory());
		
		// Ajout de la liste des LogsDirs
		properties.setProperty(KafkaConfig.LogDirsProp(), 
							   brokerConfig.getLogsDirectories()
							   			   .stream()
							   			   .map(logDirectory -> {
							   				   try {
							   					   
							   					   // Tentative d'obtention de l'URL
							   					   return ResourceUtils.getURL(logDirectory).getFile();
							   					
							   				   } catch (Exception e) {
							   					   
							   					   // Return the original Path
							   					   return logDirectory;
							   				   }
							   			   })
							   			   .collect(Collectors.joining(",")));
		
		// Ajout etat activation SSL Enabled
		properties.setProperty(KafkaConfig.SslEnabledProtocolsProp(), 
							   String.valueOf(brokerConfig.getListener()
									   					  .getProtocol()
									   					  .equals(ListenerProtocol.SSL)));
		
		// Si la configuration de truststore est definie
		if(simulatorProperties.getTruststoreConfig() != null) {

			// Ajout Truststore location
			properties.setProperty(KafkaConfig.SslTruststoreLocationProp(), 
								   simulatorProperties.getTruststoreConfig().getLocation());
			
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
								   simulatorProperties.getKeystoreConfig().getLocation());
			
			// Ajout Truststore password
			properties.setProperty(KafkaConfig.SslKeystorePasswordProp(), 
								   simulatorProperties.getKeystoreConfig().getPassword());
			
			// Ajout Keystore type
			properties.setProperty(KafkaConfig.SslKeystoreTypeProp(), 
								   simulatorProperties.getKeystoreConfig().getType().getValue());
			
			// Ajout KeyManager Algorithm
			properties.setProperty(KafkaConfig.SslKeyManagerAlgorithmProp(), 
								   simulatorProperties.getKeystoreConfig().getKeymanagerAlgorithm().getValue());
		}
		
		// Ajout Protocole SSL
		properties.setProperty(KafkaConfig.SslProtocolProp(), 
							   simulatorProperties.getSslProtocol().getValue());
		
		// Ajout Send Buffer Size
		properties.setProperty(KafkaConfig.SocketSendBufferBytesProp(), 
							   String.valueOf(simulatorProperties.getSendBufferSize()));
		
		// On retourne le liste
		return properties;
	}
		
	/**
	 * Méthode permettant d'initialiser les topics
	 */
	private void initializeTopics() {
		
		// Vidage de la liste de topics
		createdTopics.clear();
		
		// Creation des topic de la liste parametrée
		internalCreateTopics(simulatorProperties.getInitialTopics());
	}
	
	/**
	 * Méthode permettant d'exécuter ine action sur le broker en mode Admin 
	 * @param callback	Callback de l'action
	 */
	private void doWithAdmin(Consumer<AdminClient> callback) {
		
		// MAP des configurations administrateur
		Map<String, Object> adminConfigs = new HashMap<>();
		
		// Positionnement des URL de serveurs
		adminConfigs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, getBrokersAsString());
		
		try {
			
			// Execution de l'operation du callback avec cet client administration
			callback.accept(adminClient);
			
		} catch (Exception e) {
			
			// Affichage de la trace
			e.printStackTrace();
		}
	}
	
	/**
	 * Méthode permettant d'obtenir l'ensemble des URL de Broker sous forme de chaine de caracteres 
	 * @return	Chaines de Brokers
	 */
	private String getBrokersAsString() {
		
		// On retourne la chiane
		return simulatorProperties.getBrokerConfigs()
								  .parallelStream()
								  .map(brokerProperty -> "127.0.0.1:" + brokerProperty.getListener().getPort())
								  .collect(Collectors.joining(",", "", ""));
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
			
			// On leve ue exception
			throw new KafkaException(e);
		}
	}
	
	/**
	 * Méthode permettant de créer une liste de topics à partir de leur noms
	 * @param topicsNames	Tableau de noms de topics
	 */
	public void createTopics(String...topicsNames) {
		
		// Creation de la liste de topics
		internalCreateTopics(

				 // Stream sur la liste des noms de topics
				 Arrays.stream(topicsNames).collect(Collectors.toList())
		);
	}
	
	/**
	 * Méthode permettant de lister les topics 
	 * @return	Liste de topics
	 */
	public List<String> listTopics() {
		
		// Verifier que ZooKeeper est actif
		Assert.notNull(this.zookeeper, "Assurez-vous que le cluster ZooKeeper est actif avant toute opération.");
		
		// Future
		KafkaFuture<Collection<TopicListing>> topicListingFuture = adminClient.listTopics().listings();
		
		try {
			
			// Attendre le resultat
			return topicListingFuture.get(DEFAULT_ADMIN_TIMEOUT, TimeUnit.SECONDS)
									 .stream().map(topicListing -> topicListing.name())
									 		  .collect(Collectors.toList());
			
		} catch (Exception e) {
			
			// Print exception stack trace
			e.printStackTrace();
			
			// On relance
			throw new KafkaException(e);
			
		}
	}
	
	/**
	 * Méthode permettant de supprimer les topics 
	 * @param topicsNames	Liste des noms des topics a supprimer
	 */
	public void deleteTopics(List<String> topicsNames) {
		
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
			e.printStackTrace();
			
			// On relance
			throw new KafkaException(e);
			
		}
	}
	
	/**
	 * Méthode permettant de supprimer une liste de noms de topics 
	 * @param topicsNames	Liste de noms de topics
	 */
	public void deleteTopics(String...topicsNames) {
		
		// Suppression
		deleteTopics(Arrays.stream(topicsNames).collect(Collectors.toList()));	
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
			e.printStackTrace();
		}
		
		try {
			
			// Tentative de fermeture du Client ZooKeeper
			zookeeperClient.close();
			
		} catch (Exception e) {}

		try {
			
			// Tentative d'arret de ZooKeeper
			zookeeper.shutdown();
			
		} catch (Exception e) {}
	}
}
