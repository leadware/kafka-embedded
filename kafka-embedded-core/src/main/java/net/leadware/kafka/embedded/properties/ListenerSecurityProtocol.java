/**
 * RATP :: SIT :: I2V :: SGA
 */
package net.leadware.kafka.embedded.properties;

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

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Énumération des protocoles des listeners de brokers
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 26 mars 2019 - 21:11:37
 */
@AllArgsConstructor
@Getter
public enum ListenerSecurityProtocol {
	
	/**
	 * Protocole PLAINTEXT
	 */
	PLAINTEXT("PLAINTEXT"),
	
	/**
	 * Protocole ADMIN
	 */
	ADMIN("ADMIN"),

	/**
	 * Protocole INTERNAL_PRODUCER
	 */
	INTERNAL_PRODUCER("INTERNAL_PRODUCER"),
	
	/**
	 * Protocole SSL
	 */
	SSL("SSL");
	
	/**
	 * Valeur de l'enumeration
	 */
	private String value;
	
}
