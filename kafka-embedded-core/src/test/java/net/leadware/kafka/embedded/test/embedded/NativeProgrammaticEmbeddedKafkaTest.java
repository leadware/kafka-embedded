/**
 * RATP :: SIT :: I2V :: SGA
 */
package net.leadware.kafka.embedded.test.embedded;

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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.KafkaFuture.BiConsumer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import net.leadware.kafka.embedded.test.KafkaSimulatorAutoConfiguration;

/**
 * Classe de test du broker Kafka Embarqué en mode Programmatic (SANDBOX)
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 21 mars 2019 - 08:33:56
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = {"classpath:native-kafka-emdebbed-application.properties"})
@ContextConfiguration(classes = {KafkaSimulatorAutoConfiguration.class})
public class NativeProgrammaticEmbeddedKafkaTest {
	
	/**
	 * Number of Brokers
	 */
	protected static final int BROKER_NUMBER = 2;
	
	/**
	 * Controlled Shutdown
	 */
	protected static final boolean CONTROLLED_SHUTDOWN = true;

	/**
	 * First Broker Port (0 for Random)
	 */
	protected static final int BROKER_1_PORT = 0;
	
	/**
	 * Second Broker Port (0 for Random)
	 */
	protected static final int BROKER_2_PORT = 0;
	
	/**
	 * Number of Partitions
	 */
	protected static final int PARTITION_NUMBER = 2;
	
	/**
	 * Initial Topics List
	 */
	protected static final String[] TOPICS = {"HCPA", "DMES"};
	
	/**
	 * Embedded Kafka Broker (Internally create by EmbeddedKafkaRule)
	 */
	private EmbeddedKafkaBroker embeddedKafkaBroker;
	
	/**
	 * Kafka Administration
	 */
	private KafkaAdmin kafkaAdmin;
	
	/**
	 * Broker Admin Client
	 */
	private AdminClient adminClient = null;
	
	/**
	 * Before Test
	 */
	@Before
	public void before() {
		
		// Initialize Embedded Kafka Broker
		embeddedKafkaBroker = new EmbeddedKafkaBroker(BROKER_NUMBER, CONTROLLED_SHUTDOWN, PARTITION_NUMBER, TOPICS);
		
		// Set Instances Ports
		embeddedKafkaBroker.kafkaPorts(BROKER_1_PORT, BROKER_2_PORT);
		
		// Initialize
		embeddedKafkaBroker.afterPropertiesSet();
		
		// Get Admin Properties
		Map<String, Object> adminConfigs = new HashMap<>();
		
		// Put Propertie
		adminConfigs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafkaBroker.getBrokersAsString());
		
		// Instantiate Admin
		kafkaAdmin = new KafkaAdmin(adminConfigs);

		// Get Broker Admin Client
		adminClient = AdminClient.create(kafkaAdmin.getConfig());
	}

	/**
	 * After Test
	 */
	@After
	public void after() {
		
		// Destroy Embedded Broker
		embeddedKafkaBroker.destroy();		
	}
	
	/**
	 * Print Kafka properties to Console
	 * @throws InterruptedException Exception potentielle
	 */
	@Test
	public void printProperties() throws InterruptedException {
		
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
		
		// Print the Topics List when we get them
		topicListingFuture.whenComplete(new BiConsumer<Collection<TopicListing>, Throwable>() {
			
			@Override
			public void accept(Collection<TopicListing> topics, Throwable exception) {
				
				// If there are exception
				if(exception != null) throw new RuntimeException(exception);
				
				// Print List of Topics
				topics.forEach(topic -> System.out.println("--------> TOPIC = " + topic.name()));
			}
		});
		
		// Sleep
		Thread.sleep(5000);
	}

}
