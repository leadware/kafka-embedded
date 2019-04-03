/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.controller.exception;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Classe représentant une erreur de validation des parametres Rest
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 3 avr. 2019 - 22:20:11
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ConstraintViolationErrorDetails {
	
	/**
	 * Date à laquelle l'erreur est survenue
	 */
	private Date timestamp;
	
	/**
	 * Chemin du contexte de la requete source
	 */
	private String requestContextPath;
	
	/**
	 * Description de la requete source
	 */
	private String requestDescription;
	
	/**
	 * Liste des violations de contraintes
	 */
	private List<ConstraintViolationDetails> violations = new ArrayList<>();

	/**
	 * Constructeur parametre
	 * @param timestamp Date et heure de l'erreur
	 * @param requestContextPath Chemin du contexte de la requete
	 * @param requestDescription Description de la requete
	 */
	public ConstraintViolationErrorDetails(Date timestamp, String requestContextPath, String requestDescription) {
		
		// Positionnement de la date et heure de l'erreur
		this.timestamp = timestamp;
		
		// Positionnement du contexte de la requete
		this.requestContextPath = requestContextPath;
		
		// Positionnement de la description de la requete
		this.requestDescription = requestDescription;
		
		// Initialisation de la liste des violations
		violations = new ArrayList<>();
	}
	
	/**
	 * Méthode permettant d'ajouter une violation de contrainte
	 * @param violation	Violation a rajouter
	 */
	public void addViolation(ConstraintViolationDetails violation) {
		
		// Si la violation est non nulle
		if(violation != null) violations.add(violation);
	}
}