/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Énumération des type d'authentification client 
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 29 mars 2019 - 08:47:39
 */
@AllArgsConstructor
@Getter
public enum SslClientAuthentication {
	
	/**
	 * Aucune authentificationclient
	 */
	NONE("NONE"),
	
	/**
	 * Authentification client requise
	 */
	REQUIRED("REQUIRED"),

	/**
	 * Authentification client souhaitee
	 */
	REQUESTED("REQUESTED");
	
	
	/**
	 * Valeur de l'enumeration
	 */
	private String value;
	
}
