package net.leadware.kafka.embedded.test.unsecure.data;

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
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe représentant un utilisateur à sérialiser/désérialiser en KAFKA
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 1 avr. 2019 - 08:07:06
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
	
	/**
	 * Prenom de l'utilisateur
	 */
	private String lastName;
	
	/**
	 * Nom de l'utilisateur
	 */
	private String firstName;
	
	/**
	 * No de connexion
	 */
	private String login;
	
	/**
	 * Mot de passe
	 */
	private String password;
	
}
