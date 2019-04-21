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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.leadware.kafka.embedded.utils.jsr303.file.FileValidator;

/**
 * Classe de configuration d'un magasin de clés du Broker
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 23 mars 2019 - 22:14:08
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class KeystoreProperties {
	
	/**
	 * Broker keystore location 
	 */
	@NotEmpty(message = "Veuillez renseigner le chemin vers le magasin de clés (Keystore)")
	@FileValidator
	private String location = null;
	
	/**
	 * Broker keystore password 
	 */
	private String password = null;
	
	/**
	 * Mot de passe de la clé
	 */
	private String keyPassword = null;
	
	/**
	 * Broker keystore type
	 */
	private KeystoreType type = KeystoreType.JKS;
	
	/**
	 * Broker keymanager algorithm
	 */
	private KeymanagerAlgorithm keymanagerAlgorithm = KeymanagerAlgorithm.SunX509;
	
	/**
	 * Méthode d'obtention de la valeur du champ "type"
	 * @return Valeur du champ "type"
	 */
	public KeystoreType getType() {
	
		// Renvoi de la valeur du champ "type"
		return (type == null) ? KeystoreType.JKS : type;
	}

	/**
	 * Méthode d'obtention de la valeur du champ "keymanagerAlgorithm"
	 * @return Valeur du champ "keymanagerAlgorithm"
	 */
	public KeymanagerAlgorithm getKeymanagerAlgorithm() {
		
		// Renvoi de la valeur du champ "keymanagerAlgorithm"
		return (keymanagerAlgorithm == null) ? KeymanagerAlgorithm.SunX509 : keymanagerAlgorithm;
	}
}
