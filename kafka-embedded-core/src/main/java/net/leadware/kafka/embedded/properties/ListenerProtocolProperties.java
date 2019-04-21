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

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Classe de configuration des proprietes d'un des protocole d'un listener 
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 20 avr. 2019 - 18:41:18
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ListenerProtocolProperties {
	
	/**
	 * Nom du protocole
	 */
	@NotEmpty(message = "Veuillez renseigner le nom du protocole du listener")
	private String name;
	
	/**
	 * Protocole de securite
	 */
	@NotNull(message = "Veuillez renseigner la valeur du protocole de securite du listener")
	private ListenerSecurityProtocol scheme;
}
