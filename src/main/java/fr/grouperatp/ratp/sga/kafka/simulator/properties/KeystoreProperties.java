/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.properties;

import javax.validation.constraints.NotEmpty;

import fr.grouperatp.ratp.sga.kafka.simulator.utils.jsr303.file.FileValidator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Classe de configuration d'un magasin de clés du Broker
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 23 mars 2019 - 22:14:08
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class KeystoreProperties {
	
	/**
	 * Broker keystore location 
	 */
	@NotEmpty(message = "Veuillez renseigner le chemin vers le magasin de clés (Keystore)")
	@FileValidator
	private String location = null;
	
	/**
	 * Broker keystore password 
	 */
	private String password = null;
	
	/**
	 * Mot de passe de la clé
	 */
	private String keyPassword = null;
	
	/**
	 * Broker keystore type
	 */
	private KeystoreType type = KeystoreType.JKS;
	
	/**
	 * Broker keymanager algorithm
	 */
	private KeymanagerAlgorithm keymanagerAlgorithm = KeymanagerAlgorithm.SunX509;
	
	/**
	 * Méthode d'obtention de la valeur du champ "type"
	 * @return Valeur du champ "type"
	 */
	public KeystoreType getType() {
	
		// Renvoi de la valeur du champ "type"
		return (type == null) ? KeystoreType.JKS : type;
	}

	/**
	 * Méthode d'obtention de la valeur du champ "keymanagerAlgorithm"
	 * @return Valeur du champ "keymanagerAlgorithm"
	 */
	public KeymanagerAlgorithm getKeymanagerAlgorithm() {
		
		// Renvoi de la valeur du champ "keymanagerAlgorithm"
		return (keymanagerAlgorithm == null) ? KeymanagerAlgorithm.SunX509 : keymanagerAlgorithm;
	}
}
