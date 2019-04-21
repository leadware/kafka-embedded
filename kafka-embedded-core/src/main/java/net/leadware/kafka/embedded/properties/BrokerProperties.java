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

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Classe de configuration du Borker de simulation
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 23 mars 2019 - 22:09:49
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BrokerProperties {
	
	/**
	 * Default Listener
	 */
	private ListenerProperties DEFAULT_LISTENER = new ListenerProperties();

	/**
	 * Default Logs Directory
	 */
	private List<String> DEFAULT_LOGS_DIRECTORIES = Collections.singletonList(System.getProperty("java.io.tmpdir"));
	
	/**
	 * Répertoire de stockage des logs
	 */
	private String logsDirectory;
	
	/**
	 * Liste de répertoires de logs
	 */
	private List<String> logsDirectories;
	
	/**
	 * Broker Endpoints Listeners
	 */
	@Valid
	private ListenerProperties listener = new ListenerProperties();
	
	/**
	 * Méthode d'obtention de la valeur du champ "logsDirectory"
	 * @return Valeur du champ "logsDirectory"
	 */
	public String getLogsDirectory() {
		
		// Renvoi de la valeur du champ "logsDirectory"
		return (logsDirectory == null || logsDirectory.trim().isEmpty()) ? DEFAULT_LOGS_DIRECTORIES.get(0) : logsDirectory.trim();
	}
	
	/**
	 * Méthode d'obtention de la valeur du champ "logsDirectories"
	 * @return Valeur du champ "logsDirectories"
	 */
	public List<String> getLogsDirectories() {
	
		// Renvoi de la valeur du champ "logsDirectories"
		return (logsDirectories == null || logsDirectories.isEmpty()) ? DEFAULT_LOGS_DIRECTORIES : logsDirectories;
	}
	
	/**
	 * Méthode d'obtention de la valeur du champ "listener"
	 * @return Valeur du champ "listener"
	 */
	public ListenerProperties getListener() {
	
		// Renvoi de la valeur du champ "listener"
		return (listener == null) ? DEFAULT_LISTENER : listener;
	}
}
