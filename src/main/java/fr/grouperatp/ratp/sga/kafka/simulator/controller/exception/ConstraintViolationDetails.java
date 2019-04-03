/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.controller.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Classe représentant une violation de contrainte
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 3 avr. 2019 - 22:57:57
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ConstraintViolationDetails {
	
	/**
	 * Message d'erreur
	 */
	private String message;
	
	/**
	 * Code de message d'erreur
	 */
	private String messageTemplate;
	
	/**
	 * Chemin de la propriété encause
	 */
	private String propertyPath;
}
