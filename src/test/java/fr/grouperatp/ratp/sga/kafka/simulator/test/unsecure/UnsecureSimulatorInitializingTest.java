/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.test.unsecure;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import fr.grouperatp.ratp.sga.kafka.simulator.test.unsecure.data.User;

/**
 * Classe de test de bon démarrage d'un simulateur non sécurisé
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 31 mars 2019 - 11:06:05
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = {
		"classpath:unsecure-simulator-tests/unsecure-simulator-config-application.properties",
		"classpath:unsecure-simulator-tests/kafka-producer-application.properties",
		"classpath:unsecure-simulator-tests/kafka-consumer-application.properties"
})
public class UnsecureSimulatorInitializingTest {
	
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
	private String topic = "HCPA";
	
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
		
		// Propriétés du producer
		Map<String, Object> producerProperties = new HashMap<>();
		
		// Positionnement des URLs de serveurs KAFKA
		producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerBootstrapServers);
		
		// Positionnement de l'ID du client
		producerProperties.put(ProducerConfig.CLIENT_ID_CONFIG, producerClientId);
		
		// Positionnement de la classe de secrialisation des clés
		producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		
		// Positionnement de la classe de secrialisation des données
		producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		
		// Fabrique de producteurs
		ProducerFactory<String, User> kafkaProducerFactory = new DefaultKafkaProducerFactory<String, User>(producerProperties);
		
		// Template de production Kafka
		kafkaProducerTemplate = new KafkaTemplate<>(kafkaProducerFactory);
		
		// Propriétés du producer
		Map<String, Object> consumerProperties = new HashMap<>();
		
		// Positionnement des URLs de serveurs KAFKA
		consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerBootstrapServers);
		
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

		try {
			
			// Wait for Listener container start
			Thread.sleep(10000);
			
		} catch (InterruptedException e) {
			
			// Print exception stack trace
			e.printStackTrace();
		}
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
		int nbMessage = 500;
		
		// Instantiation d'un user
		User user = new User("YASHIRO", "NANAKAZE", "nyashiro", "nyashiro123");
		
		for(int count = 0; count < nbMessage ; count++) {

			// Envoi
			kafkaProducerTemplate.send(topic, user);
		}

		try {
			
			// Wait for Listener Process Message
			Thread.sleep(10000);
			
		} catch (InterruptedException e) {
			
			// Print exception stack trace
			e.printStackTrace();
		}
		
		// Assert that Record is not null
		assertThat(records, is(notNullValue()));
		
		// Assert that Record has one entry
		assertThat(records, hasSize(1));
		
		// Obtention de l'utilisateur recu
		User receivedUser = records.take().value();
		
		// Verification sur le nom
		assertThat(receivedUser.getLogin(), is(equalTo(user.getLogin())));
	}
}
