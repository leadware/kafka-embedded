package net.leadware.kafka.embedded.properties;

import java.util.ArrayList;

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

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.leadware.kafka.embedded.utils.jsr303.file.FileType;
import net.leadware.kafka.embedded.utils.jsr303.file.FileValidator;

/**
 * Classe de configuration du simulateur KAFKA
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 22 mars 2019 - 08:26:40
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = SimulatorProperties.SIMULATOR_PROPERTIES_PREFIX)
public class SimulatorProperties {
	
	/**
	 * Prefixe des propriétés deconfiguration du simulateur
	 */
	public static final String SIMULATOR_PROPERTIES_PREFIX = "embedded.kafka.simulator";
	
	/**
	 * Etat d'activation du Simulateur KAFKA
	 */
	@NotNull(message = "Veuillez renseigner l'etat d'activation du simulateur KAFKA")
	private Boolean enabled = Boolean.TRUE;
	
	/**
	 * Répertoire temporaire de fichier (java.io.tmpdir)
	 */
	@FileValidator(fileType = FileType.DIRECTORY)
	private String javaTemporaryDirectory;
	
	/**
	 * Etat de contrôle d'arrêt des brokers
	 */
	private Boolean controlledShutdown = Boolean.TRUE;
	
	/**
	 * Etat d'activation de la capacité de suppression de topics du Simulateur KAFKA
	 */
	private Boolean enableDeleteTopics = Boolean.TRUE;
	
	/**
	 * Nombre de partitions par topic
	 */
	@Positive(message = "Le nombre de partition par topic doit être une valeur supérieure à 0")
	private Integer partitionCount = 1;
	
	/**
	 * Broker instance network thread count (used for receive and send messages)
	 */
	@Positive(message = "Le nombre de threads réseaux doit être une valeur supérieure à 0")
	private Integer networkThreadCount = 1;
	
	/**
	 * Broker instance I/O thread count (used for process messages with disk I/O)
	 */
	@Positive(message = "Le nombre de threads I/O doit être une valeur supérieure à 0")
	private Integer ioThreadCount = 1;
	
	/**
	 * Broker instance send buffer max size (in byte)
	 */
	@Positive(message = "La taille du buffer d'envoie doit être une valeur supérieure à 0")
	private Long sendBufferSize = 102400L;
	
	/**
	 * Broker instance send buffer max size (in byte)
	 */
	@Positive(message = "La taille du buffer de reception doit être une valeur supérieure à 0")
	private Long receiveBufferSize = 102400L;

	/**
	 * Broker instance send buffer max size (in byte)
	 */
	@NotNull(message = "Veuillez renseigner la taille max des requetes")
	@Positive(message = "La taille du max des requetes doit être une valeur supérieure à 0")
	private Long maxRequestSize = 104857600L;
	
	/**
	 * Broker SSL protocol 
	 */
	private SslProtocol sslProtocol = SslProtocol.TLS;
	
	/**
	 * SSL Client Authentication
	 */
	private SslClientAuthentication sslClientAuthentication = SslClientAuthentication.NONE;
	
	/**
	 * Liste initiale des topics
	 */
	private List<String> initialTopics = null;
	
	/**
	 * Broker Keystore properties
	 */
	@Valid
	private KeystoreProperties keystoreConfig;

	/**
	 * Broker Truststore properties
	 */
	@Valid
	private KeystoreProperties truststoreConfig;
	
	/**
	 * Liste des proprietes des brokers du cluster
	 */
	@NotNull(message = "Veuillez renseigner les propriétés des Borkers")
	@Size(min = 1, message = "Veuillez configurer au moins un broker")
	@Valid
	private List<BrokerProperties> brokerConfigs = null;
	
	/**
	 * Méthode d'obtention de la valeur du champ "javaTemporaryDirectory"
	 * @return Valeur du champ "javaTemporaryDirectory"
	 */
	public String getJavaTemporaryDirectory() {
	
		// Renvoi de la valeur du champ "javaTemporaryDirectory"
		return (javaTemporaryDirectory == null) ? System.getProperty("java.io.tmpDir") : javaTemporaryDirectory.trim();
	}
	
	/**
	 * Méthode d'obtention de la valeur du champ "sslProtocol"
	 * @return Valeur du champ "sslProtocol"
	 */
	public SslProtocol getSslProtocol() {
	
		// Renvoi de la valeur du champ "sslProtocol"
		return (sslProtocol == null) ? SslProtocol.TLS12 : sslProtocol;
	}
	
	/**
	 * Méthode d'obtention de la valeur du champ "sslClientAuthentication"
	 * @return Valeur du champ "sslClientAuthentication"
	 */
	public SslClientAuthentication getSslClientAuthentication() {
	
		// Renvoi de la valeur du champ "sslClientAuthentication"
		return (sslClientAuthentication == null) ? SslClientAuthentication.NONE : sslClientAuthentication;
	}
	
	/**
	 * Méthode permettant d'obtenir la liste des topics initiaux à créer
	 * @return	Liste initiale des topics à créer
	 */
	public List<String> getInitialTopics() {
		
		// Renvoi de la valeur du champ "initialTopics"
		return initialTopics == null ? new ArrayList<>() : initialTopics;
	}
	
	/**
	 * Méthode d'obtention de la valeur du champ "controlledShutdown"
	 * @return Valeur du champ "controlledShutdown"
	 */
	public Boolean getControlledShutdown() {
	
		// Renvoi de la valeur du champ "controlledShutdown"
		return (controlledShutdown == null) ? Boolean.TRUE : controlledShutdown;
	}
	
	/**
	 * Méthode d'obtention de la valeur du champ "enableDeleteTopics"
	 * @return Valeur du champ "enableDeleteTopics"
	 */
	public Boolean getEnableDeleteTopics() {
	
		// Renvoi de la valeur du champ "enableDeleteTopics"
		return (enableDeleteTopics == null) ? Boolean.TRUE : enableDeleteTopics;
	}
	
	/**
	 * Méthode d'obtention de la valeur du champ "partitionCount"
	 * @return Valeur du champ "partitionCount"
	 */
	public Integer getPartitionCount() {
	
		// Renvoi de la valeur du champ "partitionCount"
		return (partitionCount == null) ? 1 : partitionCount;
	}

	/**
	 * Méthode d'obtention de la valeur du champ "networkThreadCount"
	 * @return Valeur du champ "networkThreadCount"
	 */
	public Integer getNetworkThreadCount() {
	
		// Renvoi de la valeur du champ "networkThreadCount"
		return (networkThreadCount == null) ? 1 : networkThreadCount;
	}

	/**
	 * Méthode d'obtention de la valeur du champ "ioThreadCount"
	 * @return Valeur du champ "ioThreadCount"
	 */
	public Integer getIoThreadCount() {
	
		// Renvoi de la valeur du champ "ioThreadCount"
		return (ioThreadCount == null) ? 1 : ioThreadCount;
	}

	/**
	 * Méthode d'obtention de la valeur du champ "sendBufferSize"
	 * @return Valeur du champ "sendBufferSize"
	 */
	public Long getSendBufferSize() {
	
		// Renvoi de la valeur du champ "sendBufferSize"
		return (sendBufferSize == null) ? 102400L : sendBufferSize;
	}
	
	/**
	 * Méthode d'obtention de la valeur du champ "receiveBufferSize"
	 * @return Valeur du champ "receiveBufferSize"
	 */
	public Long getReceiveBufferSize() {
	
		// Renvoi de la valeur du champ "receiveBufferSize"
		return (receiveBufferSize == null) ? 102400L : sendBufferSize;
	}

	/**
	 * Méthode d'obtention de la valeur du champ "maxRequestSize"
	 * @return Valeur du champ "maxRequestSize"
	 */
	public Long getMaxRequestSize() {
	
		// Renvoi de la valeur du champ "maxRequestSize"
		return (maxRequestSize == null) ? 104857600L : maxRequestSize;
	}
	
	/**
	 * Méthode permettant de valider l'instance
	 */
	public void validate() {
		
		// Fabrique de validateurs
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		
		// Obtention d'un valiateur
		Validator validator = factory.getValidator();
		
		// Execution de la validation
		Set<ConstraintViolation<SimulatorProperties>> constraintViolations = validator.validate(this);
		
		// Si il ya pas d'erreur
		if(constraintViolations == null || constraintViolations.size() == 0) return;
		
		// Affichage des violations
		constraintViolations.forEach(constraintViolation -> {
			
			// Affichage du chemin de la propriete
			System.out.println("-------> Propriete : " + constraintViolation.getRootBeanClass() + "." + constraintViolation.getPropertyPath());

			// Affichage de la valeur de la propriete
			System.out.println("-------> Valeur : " + constraintViolation.getInvalidValue());

			// Affichage dela propriete
			System.out.println("-------> Raison : " + constraintViolation.getMessage());
		});
		
		// On leve une exception
		throw new RuntimeException("Violation des contraintes de validation des propriété de configuration du Simulateur : " + constraintViolations);
	}
	
}
