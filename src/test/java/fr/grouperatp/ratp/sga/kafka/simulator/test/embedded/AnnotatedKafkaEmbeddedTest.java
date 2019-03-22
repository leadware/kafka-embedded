package fr.grouperatp.ratp.sga.kafka.simulator.test.embedded;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.common.KafkaFuture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Classe de test du broker Kafka Embarqué en mode Annotation (SANDBOX)
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 21 mars 2019 - 08:32:51
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@EnableKafka
@EmbeddedKafka(
		count = AnnotatedKafkaEmbeddedTest.BROKER_NUMBER,
		controlledShutdown = AnnotatedKafkaEmbeddedTest.COTROLLED_SHUTDOWN,
		ports = {AnnotatedKafkaEmbeddedTest.BROKER_1_PORT, AnnotatedKafkaEmbeddedTest.BROKER_2_PORT},
		partitions = AnnotatedKafkaEmbeddedTest.PARTITION_NUMBER,
		topics = {"HCPA", "DMES"}
)
public class AnnotatedKafkaEmbeddedTest {
	
	/**
	 * Number of Brokers
	 */
	protected static final int BROKER_NUMBER = 2;
	
	/**
	 * Controlled Shutdown
	 */
	protected static final boolean COTROLLED_SHUTDOWN = false;
	
	/**
	 * First Broker Port (0 for Random)
	 */
	protected static final int BROKER_1_PORT = 50880;
	
	/**
	 * Second Broker Port (0 for Random)
	 */
	protected static final int BROKER_2_PORT = 50881;
	
	/**
	 * Number of Partitions
	 */
	protected static final int PARTITION_NUMBER = 2;
	
	/**
	 * Kafka Rule
	 */
	@Autowired
	private EmbeddedKafkaBroker embeddedKafkaBroker;
	
	/**
	 * Kafka Administration
	 */
	private KafkaAdmin kafkaAdmin;
	
	/**
	 * Before Test
	 */
	@Before
	public void before() {
		
		// Get Admin Properties
		Map<String, Object> adminConfigs = new HashMap<>();
		
		// Put Propertie
		adminConfigs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafkaBroker.getBrokersAsString());
		
		// Instantiate Admin
		kafkaAdmin = new KafkaAdmin(adminConfigs);
	}
	
	/**
	 * After Test
	 */
	@After
	public void after() {
		
		// Shutdown Kafka Servers
		embeddedKafkaBroker.getKafkaServers().forEach(kafkaServer -> kafkaServer.shutdown());
		
		// Awiting Shutdown Kafka Servers
		embeddedKafkaBroker.getKafkaServers().forEach(kafkaServer -> kafkaServer.awaitShutdown());
	}
	
	/**
	 * Print Kafka properties to Console
	 * @throws InterruptedException Exception potentielle
	 * @throws ExecutionException Exception potentielle
	 */
	@Test
	public void printProperties() throws InterruptedException, ExecutionException {
		
		// Get Broker Admin Client
		AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfig());
		
		// Get Admin Configuration
		Map<String, Object> adminConfigs = kafkaAdmin.getConfig();
		
		// Get Properties
		Map<String, Object> consumerConfigs = KafkaTestUtils.consumerProps("consumer", "false", embeddedKafkaBroker);

		// Get Properties
		Map<String, Object> producerConfigs = KafkaTestUtils.producerProps(embeddedKafkaBroker);
		
		// Prlog
		System.out.println("---------------> ADMIN PROPERTIES");
		
		// Print properties
		System.out.println(adminConfigs);
		
		// Prlog
		System.out.println("---------------> CONSUMER PROPERTIES");
		
		// Print properties
		System.out.println(consumerConfigs);
		
		// Prlog
		System.out.println("---------------> PRODUCER PROPERTIES");
		
		// Print properties
		System.out.println(producerConfigs);
		
		// Future
		KafkaFuture<Collection<TopicListing>> topicListingFuture = adminClient.listTopics().listings();
		
		// get Lost Topics
		Collection<TopicListing> topics = topicListingFuture.get();
		
		// Print List topics
		topics.forEach(topic -> System.out.println("--------> TOPIC = " + topic.name()));
	}

}
