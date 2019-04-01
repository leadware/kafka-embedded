/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.test.unsecure;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
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
public class UsecureSimulatorInitializingTest {
	
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
	 * Topic d'echange
	 */
	@Value("${producer.topic}")
	private String topic = "HCPA";
	
	/**
	 * Fabrique de Producteur KAKFA
	 */
	private ProducerFactory<String, User> kafkaProducerFactory;
	
	/**
	 * Template de production KAFKA
	 */
	private KafkaTemplate<String, User> kafkaProducerTemplate;
	
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
		kafkaProducerFactory = new DefaultKafkaProducerFactory<String, User>(producerProperties);
		
		// Template de production Kafka
		kafkaProducerTemplate = new KafkaTemplate<>(kafkaProducerFactory);
	}
	
	/**
	 * After Test
	 */
	@After
	public void after() {}
	
	/**
	 * Méthode permettant de tester l'envoie et la réception d'une chaine de caracteres
	 */
	@Test
	public void testSendReceiveString() {
		
		// Instantiation d'un user
		User user = new User("YASHIRO", "NANAKAZE", "nyashiro", "nyashiro123");
		
		// Envoi
		kafkaProducerTemplate.send(topic, user);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			
			// Print exception stack trace
			e.printStackTrace();
		}
	}
}
