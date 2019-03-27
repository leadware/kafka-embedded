/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.utils;

import javax.annotation.PostConstruct;

import fr.grouperatp.ratp.sga.kafka.simulator.KafkaSimulator;
import fr.grouperatp.ratp.sga.kafka.simulator.properties.SimulatorProperties;
import lombok.AllArgsConstructor;

/**
 * Classe de fabrique de simulateur KAFKA
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 24 mars 2019 - 14:30:32
 */
@AllArgsConstructor
public class KafkaSimulatorFactory {

	/**
	 * Proprietes de configuration du simulateur
	 */
	private final SimulatorProperties simulatorProperties;
	
	/**
	 * Méthode permettant d'initialiser le simulateur KAFKA
	 */
	@PostConstruct
	public void initialize() {}
	
	/**
	 * Méthode permettant de construire une instance de Simulateur KAFKA
	 * @return	Instance du simulateur KAFKA
	 */
	public KafkaSimulator getInstance() {
		
		// Instantiate Kafka Simulator
		KafkaSimulator kafkaSimulator = new KafkaSimulator(simulatorProperties);
		
		// On retourne l'instance du simulateur KAFKA
		return kafkaSimulator;
	}
}
