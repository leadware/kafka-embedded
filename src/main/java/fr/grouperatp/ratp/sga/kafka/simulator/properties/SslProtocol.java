/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Énumération des protocoles des listeners de brokers
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 26 mars 2019 - 21:11:37
 */
@AllArgsConstructor
@Getter
public enum SslProtocol {
	
	/**
	 * TLS SSL
	 */
	TLS("TLS");
	
	/**
	 * Valeur de l'enumeration
	 */
	private String value;
}
