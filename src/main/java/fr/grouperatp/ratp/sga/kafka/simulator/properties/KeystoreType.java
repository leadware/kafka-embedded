/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Énumération des type de magasins de cles 
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 26 mars 2019 - 21:25:11
 */
@AllArgsConstructor
@Getter
public enum KeystoreType {
	
	/**
	 * Magasin de cl´s de type JKS
	 */
	JKS("JKS");
	
	/**
	 * Valeur de l'enumeration
	 */
	private String value;
}
