/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Classe de configuration d'un magasin de clés du Broker
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 23 mars 2019 - 22:14:08
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BrokerKeystoreProperties {
	
	/**
	 * Broker keystore location 
	 */
	private String location = null;

	/**
	 * Broker keystore password 
	 */
	private String password = null;
	
	/**
	 * Broker keystore type
	 */
	private String type = "JKS";
	
	/**
	 * Broker keymanager algorith
	 */
	private String keymanagerAlgorithm = "SunX509";
}
