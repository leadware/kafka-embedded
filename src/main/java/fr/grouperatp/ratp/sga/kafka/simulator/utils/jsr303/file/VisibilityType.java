/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.utils.jsr303.file;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Énumération des types de validations 
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 31 mars 2019 - 18:48:05
 */
@AllArgsConstructor
@Getter
public enum VisibilityType {
	
	/**
	 * Controle d'existence
	 */
	EXISTS,
	
	/**
	 * Controle d'inexistence
	 */
	NOTEXISTS,
	
	/**
	 * Controle caché
	 */
	HIDDEN,
	
	/**
	 * Controle du mode lecture
	 */
	READEABLE,

	/**
	 * Controle du mode lecture/ecriture
	 */
	WRITEABLE;
	
}
