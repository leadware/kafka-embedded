package fr.grouperatp.ratp.sga.kafka.simulator.utils.jsr303.format;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import fr.grouperatp.ratp.sga.kafka.simulator.tools.SimulatorUtils;

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
