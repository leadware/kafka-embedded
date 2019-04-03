/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.utils.jsr303.file;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Énumération des types de format
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 3 avr. 2019
 */
@AllArgsConstructor
@Getter
public enum FormatType {
	
	/**
	 * Format JSON
	 */
	JSON,
	
	/**
	 * Format XML
	 */
	XML,
	
	/**
	 * Format CSV
	 */
	CSV;
}