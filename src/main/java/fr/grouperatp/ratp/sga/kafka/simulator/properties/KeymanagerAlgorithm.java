/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Énumération des algorithmes de gestion de clés 
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 26 mars 2019 - 21:27:53
 */
@AllArgsConstructor
@Getter
public enum KeymanagerAlgorithm {
	
	/**
	 * Algorithme de gestion SunX509
	 */
	SunX509("SunX509");
	
	/**
	 * Valeur de l'enumeration
	 */
	private String value;
}
