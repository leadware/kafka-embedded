/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.properties;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
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
@Getter
@Setter
@ToString
public class ListenerProperties {
	
	/**
	 * Broker listener port
	 */
	private Integer port = 9090; 
	
	/**
	 * Broker listener URL
	 */
	private String url = "";
	
	/**
	 * Broker listener Security protocol MAP
	 */
	private Map<String, String> securityProtocolMap = null;
}
