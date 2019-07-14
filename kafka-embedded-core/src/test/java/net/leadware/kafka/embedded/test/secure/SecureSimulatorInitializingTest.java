package net.leadware.kafka.embedded.test.secure;

import static org.awaitility.Awaitility.await;

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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;
import net.leadware.bean.validation.ext.tools.FileUtils;
import net.leadware.kafka.embedded.KafkaSimulator;
import net.leadware.kafka.embedded.properties.SimulatorProperties;
import net.leadware.kafka.embedded.test.KafkaSimulatorAutoConfiguration;
import net.leadware.kafka.embedded.test.unsecure.UnsecureSimulatorInitializingWithoutPortTest;
import net.leadware.kafka.embedded.test.unsecure.data.User;

/**
 * Classe de test de bon démarrage d'un simulateur non sécurisé
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 31 mars 2019 - 11:06:05
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = {
		"classpath:secure-simulator-tests/secure-simulator-config-application.properties",
		"classpath:secure-simulator-tests/kafka-producer-application.properties",
		"classpath:secure-simulator-tests/kafka-consumer-application.properties"
})
@ContextConfiguration(classes = {KafkaSimulatorAutoConfiguration.class})
@Slf4j
public class SecureSimulatorInitializingTest {
	
	/**
	 * Simulateur KAFKA
	 */
	@Autowired
	private KafkaSimulator kafkaSimulator;
	
	/**
	 * Bootstrap Servers du Producteur
	 */
	@Value("${producer.bootstrap-servers}")
	private String producerBootstrapServers;
	
	/**
	 * Client ID du Producteur
	 */
	@Value("${producer.client-id}")
	private String producerClientId;
	
	/**
	 * Topic d'echange du Producteur
	 */
	@Value("${producer.topic}")
	private String producerTopic;
	
	/**
	 * Protocol De securite du Producteur
	 */
	@Value("${producer.security-protocol}")
	private String producerSecurityProtocol;
	
	/**
	 * localisation du truststore Producteur
	 */
	@Value("${producer.truststore-location}")
	private String producerTruststoreLocation;
	
	/**
	 * Mot de passe du truststore Producteur
	 */
	@Value("${producer.truststore-password}")
	private String producerTruststorePassword;
	
	/**
	 * localisation du keystore Producteur
	 */
	@Value("${producer.keystore-location}")
	private String producerKeyststoreLocation;
	
	/**
	 * Mot de passe du keystore Producteur
	 */
	@Value("${producer.keystore-password}")
	private String producerKeyststorePassword;
	
	/**
	 * Mot de passe de la clé Producteur
	 */
	@Value("${producer.key-password}")
	private String producerKeyPassword;
	
	/**
	 * Bootstrap Servers du Consommateur
	 */
	@Value("${consumer.bootstrap-servers}")
	private String consumerBootstrapServers;
	
	/**
	 * Client ID du Consommateur
	 */
	@Value("${consumer.client-id}")
	private String consumerClientId;
	
	/**
	 * Group ID du Consommateur
	 */
	@Value("${consumer.group-id}")
	private String consumerGroupId;
	
	/**
	 * Topic d'echange du Consommateur
	 */
	@Value("${consumer.topic}")
	private String consumerTopic = "HCPA";
	/**
	 * Protocol De securite du Consommateur
	 */
	@Value("${consumer.security-protocol}")
	private String consumerSecurityProtocol;
	
	/**
	 * localisation du truststore Consommateur
	 */
	@Value("${consumer.truststore-location}")
	private String consumerTruststoreLocation;
	
	/**
	 * Mot de passe du truststore Consommateur
	 */
	@Value("${consumer.truststore-password}")
	private String consumerTruststorePassword;
	
	/**
	 * localisation du keystore Consommateur
	 */
	@Value("${consumer.keystore-location}")
	private String consumerKeyststoreLocation;
	
	/**
	 * Mot de passe du keystore Consommateur
	 */
	@Value("${consumer.keystore-password}")
	private String consumerKeyststorePassword;
	
	/**
	 * Mot de passe de la clé Consommateur
	 */
	@Value("${consumer.key-password}")
	private String consumerKeyPassword;
	/**
	 * Template de production KAFKA
	 */
	private KafkaTemplate<String, User> kafkaProducerTemplate;
	
	/**
	 * Conteneur de Listener de Message KAFKA
	 */
	private KafkaMessageListenerContainer<String, User> kafkaMessageListenerContainer;
	
	/**
	 * File de messages reçues de KAFKA
	 */
	final BlockingQueue<ConsumerRecord<String, User>> records = new LinkedBlockingQueue<>();
	
	/**
	 * Before Test
	 */
	@Before
	public void before() {
		
		// Proprietes de simulation
		SimulatorProperties properties = kafkaSimulator.getSimulatorProperties();
		
		// Verification du nombre de listener
		assertThat(properties.getBrokerConfigs(), hasSize(1));
								
		// Verification du nombre de serveur
		assertThat(kafkaSimulator.getKafkaServers(), hasSize(1));
				
		// Verification de l'URL publique
		assertThat(kafkaSimulator.getPublicBrokersUrls(), equalTo("SSL://127.0.0.1:" + kafkaSimulator.getPublicPorts().get(0)));
		
		// Propriétés du producer
		Map<String, Object> producerProperties = new HashMap<>();
		
		// Positionnement des URLs de serveurs KAFKA
		producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerBootstrapServers+ kafkaSimulator.getPublicPorts().get(0));
		
		// Positionnement de l'ID du client
		producerProperties.put(ProducerConfig.CLIENT_ID_CONFIG, producerClientId);
		
		// Positionnement de la classe de secrialisation des clés
		producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		
		// Positionnement de la classe de secrialisation des données
		producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		
		// Positionnement de la localisation du keystore producteur
		producerProperties.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, FileUtils.getResolvedPath(producerKeyststoreLocation));
		
		// Positionnement de la clé du keystore producteur
		producerProperties.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, producerKeyststorePassword);
		
		// Positionnement du mot de passe de la clé producteur
		producerProperties.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, producerKeyPassword);
		
		// Positionnement de la localisation du truststore producteur
		producerProperties.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, FileUtils.getResolvedPath(producerTruststoreLocation));
		
		// Positionnement de la clé du truststore producteur
		producerProperties.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, producerTruststorePassword);
		
		// Positionnement du type de cle
		producerProperties.put(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, "JKS");
		
		// Positionnement du protocol de securite
		producerProperties.put("security.protocol", producerSecurityProtocol);
		
		producerProperties.put("ssl.enabled.protocols", "TLSv1.2,TLSv1.1,TLSv1");
		
		// Fabrique de producteurs
		ProducerFactory<String, User> kafkaProducerFactory = new DefaultKafkaProducerFactory<String, User>(producerProperties);
		
		// Template de production Kafka
		kafkaProducerTemplate = new KafkaTemplate<>(kafkaProducerFactory);
		
		// Propriétés du producer
		Map<String, Object> consumerProperties = new HashMap<>();
		
		// Positionnement des URLs de serveurs KAFKA
		consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerBootstrapServers + kafkaSimulator.getPublicPorts().get(0));
		
		// Positionnement de l'ID du client
		consumerProperties.put(ConsumerConfig.CLIENT_ID_CONFIG, consumerClientId);
		
		// Positionnement de l'ID du groupe
		consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
		
		// Positionnement de la classe de deserialisation des clés
		consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		
		// Positionnement de la classe de deserialisation des données
		consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		
		// Positionnement de des packages trustés pour la deserialisation
		consumerProperties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		
		// Positionnement de la localisation du keystore Consommateur
		consumerProperties.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, FileUtils.getResolvedPath(consumerKeyststoreLocation));
		
		// Positionnement de la clé du keystore Consommateur
		consumerProperties.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, consumerKeyststorePassword);
		
		// Positionnement du mot de passe de la clé Consommateur
		consumerProperties.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, consumerKeyPassword);
		
		// Positionnement de la localisation du truststore Consommateur
		consumerProperties.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, FileUtils.getResolvedPath(consumerTruststoreLocation));
		
		// Positionnement de la clé du truststore Consommateur
		consumerProperties.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, consumerTruststorePassword);
		
		// Positionnement du type de cle
		consumerProperties.put(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, "JKS");
		
		consumerProperties.put("ssl.enabled.protocols", "TLSv1.2,TLSv1.1,TLSv1");
		
		// Positionnement du protocol de securite
		consumerProperties.put("security.protocol", consumerSecurityProtocol);
		
		// Fabrique de consommateurs
		ConsumerFactory<String, User> kafkaConsumerFactory = new DefaultKafkaConsumerFactory<String, User>(consumerProperties);
		
		// Container Propertie
		ContainerProperties containerPorperties = new ContainerProperties(consumerTopic);
		
		// MessageListener Container
		kafkaMessageListenerContainer = new KafkaMessageListenerContainer<>(kafkaConsumerFactory, containerPorperties);
		
		// Add MessageListener
		kafkaMessageListenerContainer.setupMessageListener((MessageListener<String, User>) record -> {
			
			// Ajout dans la liste des recourds
			records.add(record);
		});
		
		// Demarrage du listener
		kafkaMessageListenerContainer.start();
		
		// Wait for Listener container start
		await().until( () -> !CollectionUtils.isEmpty(kafkaMessageListenerContainer.getAssignedPartitions()));
		
		// Log
		log.debug("Kafka Message Listener Container is started");
	}
	
	/**
	 * After Test
	 */
	@After
	public void after() {
		
		// Arret du listener
		kafkaMessageListenerContainer.stop();
	}
	
	/**
	 * Méthode permettant de tester l'envoie et la réception d'une chaine de caracteres
	 * @throws InterruptedException Exception potentielle
	 */
	@Test
	public void testSendReceiveString() throws InterruptedException {
		
		// Nombre de message a envoyer
		int nbMessage = 10;
		
		// Instantiation d'un user
		User user = new User("YASHIRO", "NANAKAZE", "nyashiro", "nyashiro123");
		
		for(int count = 0; count < nbMessage ; count++) {

			// Envoi
			kafkaProducerTemplate.send(producerTopic, user);
		}

		// Wait for Listener Process Message
		await().until(() -> records.size() >= nbMessage);
		
		// Assert that Record is not null
		assertThat(records, is(notNullValue()));
		
		// Assert that Record has one entry
		assertThat(records, hasSize(nbMessage));
		
		// Obtention de l'utilisateur recu
		User receivedUser = records.take().value();
		
		// Verification sur le nom
		assertThat(receivedUser.getLogin(), is(equalTo(user.getLogin())));
	}
}
