/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.properties;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
	 * Identifiant du Broker
	 */
	private String brokerId;
	
	/**
	 * Répertoire de stockage des logs
	 */
	private String logsDirectory;
	
	/**
	 * Liste de répertoires de logs
	 */
	private List<String> logsDirectories;
	
	/**
	 * Broker Endpoints Listeners
	 */
	@NotNull(message = "Veuillez renseigner les propriétés du(des) listeners du broker")
	@Size(min = 1, message = "Veuillez renseigner au moins un listener")
	private Map<Integer, ListenerProtocol> listeners = null;
}
