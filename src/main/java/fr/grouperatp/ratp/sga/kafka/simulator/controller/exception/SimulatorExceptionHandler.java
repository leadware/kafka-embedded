/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.controller.exception;

import java.util.Date;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * Classe de gestion des exceptions survenus dans les traitements REST
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 3 avr. 2019 - 22:10:35
 */
@RestControllerAdvice
public class SimulatorExceptionHandler {
	
	/**
	 * Méthode permettant de traiter les exception de violation de contraintes d'integrite
	 * @param exception	Exception survenue
	 * @param request	Requete source du traitement
	 * @return	Reponse a retourner à l'appelant
	 */
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ConstraintViolationException.class)
	public @ResponseBody ConstraintViolationErrorDetails constraintViolationException(ConstraintViolationException exception, WebRequest request) {
		
		// Initialisation de l'erreur
		final ConstraintViolationErrorDetails error = new ConstraintViolationErrorDetails(new Date(), 
																						  request.getContextPath(), 
																						  request.getDescription(false));
		
		// Construction des details de l'erreur a retourner
		exception.getConstraintViolations().forEach(constraintViolation -> {
			
			// Ajout du detail
			error.addViolation(
					
					// Ajout du detail de la contrainte
					new ConstraintViolationDetails(constraintViolation.getMessage(), 
							  					   constraintViolation.getMessageTemplate(),
							  					   constraintViolation.getPropertyPath().toString())
			);
		});
		
		// On retourne les details
		return error;
	}
}
