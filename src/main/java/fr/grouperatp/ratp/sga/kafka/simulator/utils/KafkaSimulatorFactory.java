/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.kafka.test.EmbeddedKafkaBroker;

import fr.grouperatp.ratp.sga.kafka.simulator.KafkaSimulator;
import lombok.AllArgsConstructor;

/**
 * Classe de fabrique de simulateur KAFKA
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 24 mars 2019 - 14:30:32
 */
@AllArgsConstructor
public class KafkaSimulatorFactory {

	/**
	 * Nombre d'instances de Brokers KAFKA
	 */
	private int brokerCount = 1;
	
	/**
	 * Etat de contrôle d'arrêt des brokers
	 */
	private boolean controlledShutdown = Boolean.TRUE;
	
	/**
	 * Nombre de partitions par topic
	 */
	private int partitionCount = 1;
	
	/**
	 * Liste initiale des topics
	 */
	private List<String> initialTopics = null;
	
	/**
	 * Liste des ports des brokers
	 */
	private List<Integer> brokersPorts = null;
	
	/**
	 * MAP des propriétés communes des instances de brokers
	 */
	private Map<String, String> brokerProperties = null;
	
	/**
	 * Méthode permettant d'initialiser le simulateur KAFKA
	 */
	@PostConstruct
	public void initialize() {
		
		// If initialTopics is null
		if(initialTopics == null) initialTopics = new ArrayList<>();
		
		// If Brokers Ports is null
		if(brokersPorts == null) brokersPorts = new ArrayList<>();
		
		// if Brokers properties is null
		if(brokerProperties == null) brokerProperties = new HashMap<>();
	}
	
	/**
	 * Méthode permettant de construire une instance de Simulateur KAFKA
	 * @return	Instance du simulateur KAFKA
	 */
	public KafkaSimulator getInstance() {
		
		// Instantiation d'un Embedded broker
		EmbeddedKafkaBroker embeddedKafkaBroker = 
				new EmbeddedKafkaBroker(brokerCount, 
										controlledShutdown, 
										partitionCount, 
										initialTopics.stream().toArray(String[]::new));
		
		// Set the broker properties
		embeddedKafkaBroker.brokerProperties(brokerProperties);
		
		// Set the brokers Ports
		embeddedKafkaBroker.kafkaPorts(brokersPorts.stream().mapToInt(i -> i.intValue()).toArray());
		
		// Instantiate Kafka Simulator
		KafkaSimulator kafkaSimulator = new KafkaSimulator(embeddedKafkaBroker, null, null, null);
		
		// On retourne l'instance du simulateur KAFKA
		return kafkaSimulator;
	}
}
