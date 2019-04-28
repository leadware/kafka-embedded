package net.leadware.kafka.embedded.utils;

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

import javax.annotation.PostConstruct;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.leadware.kafka.embedded.KafkaSimulator;
import net.leadware.kafka.embedded.properties.SimulatorProperties;

/**
 * Classe de fabrique de simulateur KAFKA
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 24 mars 2019 - 14:30:32
 */
@AllArgsConstructor
@Slf4j
public class KafkaSimulatorFactory {

	/**
	 * Proprietes de configuration du simulateur
	 */
	private final SimulatorProperties simulatorProperties;
	
	/**
	 * Méthode permettant d'initialiser le simulateur KAFKA
	 */
	@PostConstruct
	public void initialize() {}
	
	/**
	 * Méthode permettant de construire une instance de Simulateur KAFKA
	 * @return	Instance du simulateur KAFKA
	 */
	public KafkaSimulator getInstance() {
		
		// Log
		log.debug("Instantiation d'un Simulateur KAFKA");
		
		// Instantiate Kafka Simulator
		KafkaSimulator kafkaSimulator = new KafkaSimulator(simulatorProperties);
		
		// On retourne l'instance du simulateur KAFKA
		return kafkaSimulator;
	}
}
