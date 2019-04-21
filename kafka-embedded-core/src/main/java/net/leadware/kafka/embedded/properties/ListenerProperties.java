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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Classe de configuration des endpoints d'un broker KAFKA
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 23 mars 2019 - 22:11:17
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@ToString
public class ListenerProperties {
	
	/**
	 * Protocole public par defaut
	 */
	private static final ListenerProtocolProperties DEFAULT_PUBLIC_PROTOCOL = new ListenerProtocolProperties("PLAINTEXT", ListenerSecurityProtocol.PLAINTEXT);
	
	/**
	 * Protocole admin par defaut
	 */
	private static final ListenerProtocolProperties DEFAULT_ADMIN_PROTOCOL = new ListenerProtocolProperties("ADMIN", ListenerSecurityProtocol.PLAINTEXT);
	
	/**
	 * Protocole interne producteur par defaut
	 */
	private static final ListenerProtocolProperties DEFAULT_INTERNAL_PRODUCER_PROTOCOL = new ListenerProtocolProperties("INTERNAL_PRODUCER", ListenerSecurityProtocol.PLAINTEXT);
	
	/**
	 * Broker listener port
	 */
	@Positive(message = "La valeur du port d'écoute du broker ne peut être nulle ou négative")
	@NotNull(message = "Veuillez renseigner le numéro de port d'écoute du broker")
	private Integer port = 0; 
	
	/**
	 * Broker listener admin port
	 */
	private Integer adminPort = 0;
	
	/**
	 * Broker listener internal producer port
	 */
	private Integer internalProducerPort = 0;
	
	/**
	 * Broker listener protocol
	 */
	private ListenerProtocolProperties protocol = DEFAULT_PUBLIC_PROTOCOL;
	
	/**
	 * Broker listener admin protocol
	 */
	private ListenerProtocolProperties adminProtocol = DEFAULT_ADMIN_PROTOCOL;
	
	/**
	 * Broker listener internal producer protocol
	 */
	private ListenerProtocolProperties internalProducerProtocol = DEFAULT_INTERNAL_PRODUCER_PROTOCOL;
	
	/**
	 * Méthode d'obtention de la valeur du champ "port"
	 * @return Valeur du champ "port"
	 */
	public Integer getPort() {
	
		// Renvoi de la valeur du champ "port"
		return (port == null) ? 0 : port;
	}

	/**
	 * Méthode d'obtention de la valeur du champ "protocol"
	 * @return Valeur du champ "protocol"
	 */
	public ListenerProtocolProperties getProtocol() {
	
		// Renvoi de la valeur du champ "protocol"
		return (protocol == null) ? DEFAULT_PUBLIC_PROTOCOL : protocol;
	}

	/**
	 * Getter du champ "adminPort"
	 * @return valeur du champ "adminPort"
	 */
	public synchronized Integer getAdminPort() {
		
		// Retourne la valeur du champ "adminPort"
		return (adminPort == null) ? 0 : adminPort;
	}

	/**
	 * Getter du champ "internalProducerPort"
	 * @return valeur du champ "internalProducerPort"
	 */
	public Integer getInternalProducerPort() {
	
		// Retourne la valeur du champ "internalProducerPort"
		return (internalProducerPort == null) ? 0 : internalProducerPort;
	}

	/**
	 * Getter du champ "adminProtocol"
	 * @return valeur du champ "adminProtocol"
	 */
	public ListenerProtocolProperties getAdminProtocol() {
	
		// Retourne la valeur du champ "adminProtocol"
		return (adminProtocol == null) ? DEFAULT_ADMIN_PROTOCOL : adminProtocol;
	}

	/**
	 * Getter du champ "internalProducerProtocol"
	 * @return valeur du champ "internalProducerProtocol"
	 */
	public ListenerProtocolProperties getInternalProducerProtocol() {
	
		// Retourne la valeur du champ "internalProducerProtocol"
		return (internalProducerProtocol == null) ? DEFAULT_INTERNAL_PRODUCER_PROTOCOL : internalProducerProtocol;
	}
	
}
