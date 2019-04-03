/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.utils.jsr303.file;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import fr.grouperatp.ratp.sga.kafka.simulator.tools.SimulatorUtils;

/**
 * Classe d'implémentation de la validation de fichier définie par {@link FileValidator} 
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 31 mars 2019 - 18:54:21
 */
public class FileValidatorEngine implements ConstraintValidator<FileValidator, String> {
	
	/**
	 * Annotation de validation
	 */
	private FileValidator fileValidator;
	
	/*
	 * (non-Javadoc)
	 * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
	 */
	@Override
	public void initialize(FileValidator fileValidator) {
		
		// Appel Parent
		ConstraintValidator.super.initialize(fileValidator);
		
		// Positionnement du type de fichier
		this.fileValidator = fileValidator;
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
	 */
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		
		// Si le champ est vide
		if(value == null || value.trim().isEmpty()) 
			
			// On retourne la valeur de l'état de validation en cas de champs vide
			return fileValidator.acceptOnEmptyField();
		
		// Enumération des cas de validation
		switch (fileValidator.visibility()) {
		
		// En cas de validation de l'existence
		case EXISTS:
			
			// Énumération des type de fichier
			switch (fileValidator.fileType()) {
			
			// Fichier regulier
			case FILE:
				
				// Validation de l'existence du fichier regulier
				return SimulatorUtils.isFile(value.trim());
				
			case DIRECTORY:
				
				// Validation de l'existence du repertoire
				return SimulatorUtils.isDirectory(value.trim());
				
			case ANY:
				
				// Validation de l'existence du fichier ou repertoire
				return SimulatorUtils.fileExists(value.trim());
				
			default:
				
				// Validation de l'existence du fichier ou repertoire
				return SimulatorUtils.fileExists(value.trim());
				
			}
			
		// En cas de validation de la non existence
		case NOTEXISTS:

			// Énumération des type de fichier
			switch (fileValidator.fileType()) {
			
			// Fichier regulier
			case FILE:
				
				// Validation de la non existence du fichier regulier
				return !SimulatorUtils.isFile(value.trim());
				
			case DIRECTORY:
				
				// Validation de la non existence du repertoire
				return !SimulatorUtils.isDirectory(value.trim());
				
			case ANY:
				
				// Validation de la non existence du fichier ou repertoire
				return !SimulatorUtils.fileExists(value.trim());
				
			default:
				
				// Validation de la non existence du fichier ou repertoire
				return !SimulatorUtils.fileExists(value.trim());
			}	
			
		// Validation de la visibilité cachée
		case HIDDEN:
		
			// Validation
			return SimulatorUtils.isHidden(value.trim());
			
		// Validation de la visibilité lecture	
		case READEABLE:
			
			// On retourne l'état de lecture
			return SimulatorUtils.isReadeable(value.trim());

		// Validation de la visibilité lecture	
		case WRITEABLE:
			
			// On retourne l'état de lecture/ecriture
			return SimulatorUtils.isWriteable(value.trim());
		}
		
		// On retourne false
		return false;
	}
}
