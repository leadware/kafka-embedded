/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.test.unsecure.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe représentant un utilisateur à sérialiser/désérialiser en KAFKA
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 1 avr. 2019 - 08:07:06
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
	
	/**
	 * Prenom de l'utilisateur
	 */
	private String lastName;
	
	/**
	 * Nom de l'utilisateur
	 */
	private String firstName;
	
	/**
	 * No de connexion
	 */
	private String login;
	
	/**
	 * Mot de passe
	 */
	private String password;
	
}
