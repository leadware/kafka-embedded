/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.properties;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Classe de configuration du simulateur KAFKA
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 22 mars 2019 - 08:26:40
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = SimulatorProperties.SIMULATOR_PROPERTIES_PREFIX)
@Validated
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
	private String javaTemporaryDirectory;
	
	/**
	 * Etat de contrôle d'arrêt des brokers
	 */
	@NotNull(message = "Veuillez renseigner l'état de contrôle d'arrêt des brokers")
	private Boolean controlledShutdown = Boolean.TRUE;

	/**
	 * Etat d'activation de la capacité de suppression de topics du Simulateur KAFKA
	 */
	@NotNull(message = "Veuillez renseigner l'etat d'activation de la suppression de topics du simulateur KAFKA")
	private Boolean enableDeleteTopics = Boolean.TRUE;
	
	/**
	 * Nombre de partitions par topic
	 */
	@NotNull(message = "Veuillez renseigner le nombre de partition par topics")
	@Positive(message = "Le nombre de partition par topic doit être une valeur supérieure à 0")
	private Integer partitionCount = 1;
	
	/**
	 * Broker instance network thread count (used for receive and send messages)
	 */
	@NotNull(message = "Veuillez renseigner le nombre de threads réseaux")
	@Positive(message = "Le nombre de threads réseaux doit être une valeur supérieure à 0")
	private Integer networkThreadCount = 1;
	
	/**
	 * Broker instance I/O thread count (used for process messages with disk I/O)
	 */
	@NotNull(message = "Veuillez renseigner le nombre de threads d'I/O")
	@Positive(message = "Le nombre de threads I/O doit être une valeur supérieure à 0")
	private Integer ioThreadCount = 1;
	
	/**
	 * Broker instance send buffer max size (in byte)
	 */
	@NotNull(message = "Veuillez renseigner la taille du buffer")
	@Positive(message = "La taille du buffer doit être une valeur supérieure à 0")
	private Long sendBufferSize = 102400L;
	
	/**
	 * Broker SSL protocol 
	 */
	private SslProtocol sslProtocol = SslProtocol.TLS;
	
	/**
	 * Liste initiale des topics
	 */
	private List<String> initialTopics = null;

	/**
	 * Broker Keystore properties
	 */
	private KeystoreProperties keystoreConfig;
	
	/**
	 * Liste des proprietes des brokers du cluster
	 */
	@NotNull(message = "Veuillez renseigner les propriétés des Borkers")
	@Size(min = 1, message = "Veuillez configurer au moins un broker")
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
		return (sslProtocol == null) ? SslProtocol.TLS : sslProtocol;
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
}
