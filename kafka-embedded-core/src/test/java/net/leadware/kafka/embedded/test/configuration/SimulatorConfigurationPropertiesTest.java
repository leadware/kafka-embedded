package net.leadware.kafka.embedded.test.configuration;

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
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import net.leadware.kafka.embedded.properties.KeymanagerAlgorithm;
import net.leadware.kafka.embedded.properties.KeystoreType;
import net.leadware.kafka.embedded.properties.ListenerSecurityProtocol;
import net.leadware.kafka.embedded.properties.SimulatorProperties;
import net.leadware.kafka.embedded.properties.SslProtocol;
import net.leadware.kafka.embedded.test.KafkaSimulatorAutoConfiguration;

/**
 * Classe de test de chargement des propriétés de configuration du Simulateur
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 25 mars 2019 - 07:46:24
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {KafkaSimulatorAutoConfiguration.class})
@TestPropertySource(locations = {"classpath:load-simulator-properties/load-simulator-config-application.properties"})
public class SimulatorConfigurationPropertiesTest {
	
	/**
	 * Simulator properties
	 */
	@Autowired
	private SimulatorProperties simulatorProperties;
	

	/**
	 * Before Test
	 */
	@Before
	public void before() {}
	
	/**
	 * After Test
	 */
	@After
	public void after() {}
	
	/**
	 * Méthode permettant de tester le chargement des propriétés
	 */
	@Test
	public void testLoadedProperties() {
		
		// Assert Simulator properties is not null
		assertThat(simulatorProperties, is(notNullValue()));
		
		// Assert Controlled Shutdown is false
		assertThat(simulatorProperties.getControlledShutdown(), is(false));
		
		// Assert that there is 4 partition per topics
		assertThat(simulatorProperties.getPartitionCount(), is(4));
		
		// Assert that there is 3 network threads count
		assertThat(simulatorProperties.getNetworkThreadCount(), is(3));
		
		// Assert that there is 4 I/O threads count
		assertThat(simulatorProperties.getIoThreadCount(), is(4));
		
		// Assert that there is send buffer size
		assertThat(simulatorProperties.getSendBufferSize(), is(102400L));
		
		// Assert that the default SSL protocol is TLS
		assertThat(simulatorProperties.getSslProtocol(), is(SslProtocol.TLS));
		
		// Assert Keystore configuration is set
		assertThat(simulatorProperties.getTruststoreConfig(), is(notNullValue()));
		
		// Assert Keystore password is equal to certain value
		assertThat(simulatorProperties.getTruststoreConfig().getPassword(), is(equalTo("r@tp!k@fk@#")));
		
		// Assert Keystore type is JKS
		assertThat(simulatorProperties.getTruststoreConfig().getType(), is(equalTo(KeystoreType.JKS)));
		
		// Assert Keymanager algorithm type is SunX509
		assertThat(simulatorProperties.getTruststoreConfig().getKeymanagerAlgorithm(), is(equalTo(KeymanagerAlgorithm.SunX509)));
		
		// Assert Initial topic List is set
		assertThat(simulatorProperties.getInitialTopics(), is(notNullValue()));
		
		// Assert there are 3 topics in initial list
		assertThat(simulatorProperties.getInitialTopics(), hasSize(3));
		
		// Assert that initial topics list contains some elements
		assertThat(simulatorProperties.getInitialTopics(), hasItems("HCPA", "DMES", "IC"));
		
		// Assert that broker configs is not null
		assertThat(simulatorProperties.getBrokerConfigs(), is(notNullValue()));
		
		// Assert that broker configs is not null has one entry
		assertThat(simulatorProperties.getBrokerConfigs(), hasSize(1));
		
		// Assert that broker first config port map 9090 is set to PLAINTEXT
		assertThat(simulatorProperties.getBrokerConfigs().get(0).getListener().getPort(), is(equalTo(9090)));
		
		// Assert that broker first config port map 9091 is set to SSL
		assertThat(simulatorProperties.getBrokerConfigs().get(0).getListener().getProtocol().getScheme(), is(equalTo(ListenerSecurityProtocol.PLAINTEXT)));

		// Assert that broker first config port map 9091 is set to SSL
		assertThat(simulatorProperties.getBrokerConfigs().get(0).getListener().getProtocol().getName(), is(equalTo("PLAINTEXT")));
		
		// Validate Configuration
		simulatorProperties.validate();
	}
}
