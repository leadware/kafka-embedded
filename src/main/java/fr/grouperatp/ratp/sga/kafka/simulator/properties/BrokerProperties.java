/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.properties;

import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Classe de configuration du Borker de simulation
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
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
	 * Identifiant du Broker
	 */
	private String brokerId;
	
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
	private ListenerProperties listener = null;
	
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
