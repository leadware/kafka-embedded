/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.properties;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Classe de configuration du Borker de simulation
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 23 mars 2019 - 22:09:49
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BrokerProperties {
	
	/**
	 * Broker instance network thread count (used for receive and send messages)
	 */
	@NotNull(message = "Veuillez renseigner le nombre de threads réseaux")
	@Positive(message = "Le nombre de threads réseaux doit être une valeur supérieure à 0")
	private Integer networkThreadCount = 2;
	
	/**
	 * Broker instance I/O thread count (used for process messages with disk I/O)
	 */
	@NotNull(message = "Veuillez renseigner le nombre de threads d'I/O")
	@Positive(message = "Le nombre de threads I/O doit être une valeur supérieure à 0")
	private Integer ioThreadCount = 2;
	
	/**
	 * Broker instance send buffer max size (in byte)
	 */
	@NotNull(message = "Veuillez renseigner la taille du buffer")
	@Positive(message = "La taille du buffer doit être une valeur supérieure à 0")
	private Long sendBufferSize = 102400L;
	
	/**
	 * Broker SSL protocol 
	 */
	private String sslProtocol = "TLS";
	
	/**
	 * Broker Endpoints ports
	 */
	private List<Integer> ports = null;
	
}
