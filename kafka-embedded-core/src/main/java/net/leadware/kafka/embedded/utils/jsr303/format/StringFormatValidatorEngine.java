package net.leadware.kafka.embedded.utils.jsr303.format;

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

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import net.leadware.kafka.embedded.tools.SimulatorUtils;

/**
 * Classe d'implémentation de la validation de fichier définie par {@link StringFormatValidator}
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 3 avr. 2019
 */
public class StringFormatValidatorEngine implements ConstraintValidator<StringFormatValidator, String> {

	/**
	 * Annotation de validation
	 */
	private StringFormatValidator stringFormatValidator;
	
	/*
	 * (non-Javadoc)
	 * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
	 */
	@Override
	public void initialize(StringFormatValidator stringFormatValidator) {
		
		// Appel Parent
		ConstraintValidator.super.initialize(stringFormatValidator);
		
		// Positionnement du type de fichier
		this.stringFormatValidator = stringFormatValidator;
		
	}
	
	/* (non-Javadoc)
	 * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
	 */
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		
		// Si le format requis est JSON
		if(stringFormatValidator.value().equals(FormatType.JSON)) return SimulatorUtils.isValidJson(value);
		
		// On retourne false
		return false;
	}

}
