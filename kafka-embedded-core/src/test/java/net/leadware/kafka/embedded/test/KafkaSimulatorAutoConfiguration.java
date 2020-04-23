package net.leadware.kafka.embedded.test;

import org.springdoc.core.GroupedOpenApi;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.leadware.kafka.embedded.KafkaSimulator;
import net.leadware.kafka.embedded.properties.SimulatorProperties;
import net.leadware.kafka.embedded.utils.KafkaSimulatorFactory;

/**
 * Classe de condiguration automatique du simulateur KAFKA
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 24 mars 2019 - 14:11:54
 */
@Configuration
@ConditionalOnClass({
	KafkaSimulator.class,
	KafkaSimulatorFactory.class
})
@EnableConfigurationProperties(SimulatorProperties.class)
public class KafkaSimulatorAutoConfiguration {
	
	/**
	 * Propriété de configuration du simulateur KAFKA
	 */
	@Autowired
	private SimulatorProperties simulatorProperties;
	
	/**
	 * Méthode permettant de créer un bean kafkaSimulator Factory
	 * @return Bean kafkaSimulatorFactory
	 */
	@ConditionalOnProperty(
			prefix = SimulatorProperties.SIMULATOR_PROPERTIES_PREFIX,
			name = "enabled",
			havingValue = "true",
			matchIfMissing = false
	)
	@ConditionalOnMissingBean
	@Bean
	public KafkaSimulatorFactory kafkaSimulatorFactory() {
		
		// On retourne l'instance du Bean
		return new KafkaSimulatorFactory(simulatorProperties);
	}
	
	/**
	 * Méthode permettant de créer un bean kafkaSimulator
	 * @param kafkaSimulatorFactory Fabrique de simulateur Kafka
	 * @return	Bean kafkaSimulator
	 */
	@ConditionalOnProperty(
			prefix = SimulatorProperties.SIMULATOR_PROPERTIES_PREFIX,
			name = "enabled",
			havingValue = "true",
			matchIfMissing = false
	)	
	@ConditionalOnMissingBean
	@Bean
	public KafkaSimulator kafkaSimulator(KafkaSimulatorFactory kafkaSimulatorFactory) {
		
		// On retourne l'instance du simulateur KAFKA
		return kafkaSimulatorFactory.getInstance();
	}

	/**
	 * Methode de construction de la configuration de documentation d'API
	 * @return	Configuration de documentation d'API
	 */
	@ConditionalOnProperty(
			prefix = SimulatorProperties.SIMULATOR_PROPERTIES_PREFIX,
			name = "enabled",
			havingValue = "true",
			matchIfMissing = false
	)
	@ConditionalOnMissingBean
	@Bean
	public GroupedOpenApi api() {
		
		// Construction du groupe d'api
		return GroupedOpenApi.builder()
				.setGroup("kafka-simulator-test")
				.packagesToScan("net.leadware.kafka.embedded")
				.build();
	}
}
