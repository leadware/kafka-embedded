package net.leadware.kafka.embedded.controller.exception;

/*-
 * #%L
 * Apache Kafka Embedded Server
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2013 - 2019 Leadware
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
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
