/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.properties;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Classe de configuration des endpoints d'un broker KAFKA
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 23 mars 2019 - 22:11:17
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@ToString
public class ListenerProperties {
	
	/**
	 * Broker listener port
	 */
	private Integer port = 9090; 
	
	/**
	 * Broker listener protocol
	 */
	private ListenerProtocol protocol = ListenerProtocol.PLAINTEXT;

	/**
	 * Méthode d'obtention de la valeur du champ "port"
	 * @return Valeur du champ "port"
	 */
	public Integer getPort() {
	
		// Renvoi de la valeur du champ "port"
		return (port == null) ? 9090 : port;
	}

	/**
	 * Méthode d'obtention de la valeur du champ "protocol"
	 * @return Valeur du champ "protocol"
	 */
	public ListenerProtocol getProtocol() {
	
		// Renvoi de la valeur du champ "protocol"
		return (protocol == null) ? ListenerProtocol.PLAINTEXT : protocol;
	}
	
}
