/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.properties;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Classe de configuration du simulateur KAFKA
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 22 mars 2019 - 08:26:40
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = SimulatorProperties.SIMULATOR_PROPERTIES_PREFIX)
@Validated
public class SimulatorProperties {
	
	/**
	 * Prefixe des propriétés deconfiguration du simulateur
	 */
	public static final String SIMULATOR_PROPERTIES_PREFIX = "embedded.kafka.simulator";
	
	/**
	 * Etat d'activation du Simulateur KAFKA
	 */
	@NotNull(message = "Veuillez renseigner l'etat d'activation du simulateur KAFKA")
	private Boolean enabled = Boolean.TRUE;
	
	/**
	 * Etat de contrôle d'arrêt des brokers
	 */
	@NotNull(message = "Veuillez renseigner l'état de contrôle d'arrêt des brokers")
	private Boolean controlledShutdown = Boolean.TRUE;
	
	/**
	 * Nombre de partitions par topic
	 */
	@NotNull(message = "Veuillez renseigner le nombre de partition par topics")
	@Positive(message = "Le nombre de partition par topic doit être une valeur supérieure à 0")
	private Integer partitionCount = 1;
	
	/**
	 * Liste initiale des topics
	 */
	private List<String> initialTopics = null;
	
	/**
	 * Propriétés supplémentaires des brokers
	 */
	@NotNull(message = "Veuillez renseigner les propriétés des Borkers")
	private BrokerProperties brokerConfig = null;
}
