package fr.grouperatp.ratp.sga.kafka.simulator.utils.jsr303.format;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.springframework.core.annotation.AliasFor;

/**
 *  Annotation de validation du format d'une chaine de caractere
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 3 avr. 2019
 */
@Constraint(validatedBy = StringFormatValidatorEngine.class)
@Documented
@Retention(RUNTIME)
@Target({ FIELD, PARAMETER, ANNOTATION_TYPE })
public @interface StringFormatValidator {

	/**
	 * Méthode permettant de retourner le message en cas de violation de la contrainte
	 * @return Code du message d'erreur
	 */
	String message() default "{fr.grouperatp.sga.kafka.simulator.constraints.StringFormatValidator.message}";
	
	/**
	 * Méthode permettant d'obtenir le groupe auquel appartient l'annotation
	 * @return	Groupe
	 */
	Class<?>[] groups() default { };
	
	/**
	 * Methode permettant d'obtenir le payload (faccultatif)
	 * @return	Payload
	 */
	Class<? extends Payload>[] payload() default { };
	
	/**
	 * Methode permettant d'obtenir le format que doit respecter la chaine
	 * @return	Format requis
	 */
	@AliasFor("format")
	FormatType value() default FormatType.JSON;
	
	/**
	 * Methode permettant d'obtenir le format que doit respecter la chaine
	 * @return	Format requis
	 */
	@AliasFor("value")
	FormatType format() default FormatType.JSON;
}
