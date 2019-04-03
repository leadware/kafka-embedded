/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.utils.jsr303.file;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Énumération des types de Files 
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 31 mars 2019 - 18:33:57
 */
@AllArgsConstructor
@Getter
public enum FileType {
	
	/**
	 * Fichier
	 */
	FILE,
	
	/**
	 * Répertoire
	 */
	DIRECTORY,
	
	/**
	 * Fichier ou répertoire
	 */
	ANY;
	
}
