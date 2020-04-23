package net.leadware.kafka.embedded.autoconfigure;

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
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;

import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import kafka.server.KafkaServer;
import kafka.zk.EmbeddedZookeeper;
import lombok.extern.slf4j.Slf4j;
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
	KafkaSimulatorFactory.class,
	AccessMode.class,
	KafkaTemplate.class,
	KafkaAdmin.class,
	KafkaServer.class,
	EmbeddedZookeeper.class
})
@EnableConfigurationProperties(SimulatorProperties.class)
@ComponentScan(basePackageClasses = KafkaSimulator.class)
@Slf4j
public class KafkaEmbeddedAutoConfiguration {
	
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
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public KafkaSimulatorFactory kafkaSimulatorFactory() {
		
		// Log
		log.debug("Création du Bean de fabrique des Simulateurs Kafka");
		
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
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public KafkaSimulator kafkaSimulator(KafkaSimulatorFactory kafkaSimulatorFactory) {
		
		// Log
		log.debug("Création du Bean de simulation Kafka");
		
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
	@Bean
	public GroupedOpenApi kafkaEmbeddedApi() {
		
		// Log
		log.debug("Création du Bean swagger d'exposition de la documentation de l'API Kafka Embedded");
		
		// Construction du groupe d'api
		return GroupedOpenApi.builder()
				.setGroup("kafka-embedded-api")
				.packagesToScan("net.leadware.kafka.embedded")
				.build();
	}
}
