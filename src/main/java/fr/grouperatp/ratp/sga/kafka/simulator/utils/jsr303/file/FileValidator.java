/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.utils.jsr303.file;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Annotation de validation d'existence d'un fichier/repertoire 
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 31 mars 2019 - 13:09:27
 */
@Constraint(validatedBy = FileValidatorEngine.class)
@Documented
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
public @interface FileValidator {
	
	/**
	 * Méthode permettant de retourner le message en cas de violation de la contrainte
	 * @return Code du message d'erreur
	 */
	String message() default "{fr.grouperatp.sga.kafka.simulator.constraints.FileValidator.message}";
	
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
	 * Méthode permettant de retourner la valeur de l'attribut de type de fichier
	 * @return	Type de fichier
	 */
	FileType fileType() default FileType.ANY;
	
	/**
	 * 
	 * Méthode permettant de retourner la valeur du type de validation
	 * @return	Type de validation
	 */
	VisibilityType visibility() default VisibilityType.READEABLE;
	
	/**
	 * Méthode permettant d'obtenir l'État de validation du champ valide si il n'est pas renseigné
	 * @return	État de validation du champ valide si il n'est pas renseigné
	 */
	boolean acceptOnEmptyField() default true;
}
