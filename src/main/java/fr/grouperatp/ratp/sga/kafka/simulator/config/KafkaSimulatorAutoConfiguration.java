/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import fr.grouperatp.ratp.sga.kafka.simulator.KafkaSimulator;
import fr.grouperatp.ratp.sga.kafka.simulator.properties.SimulatorProperties;
import fr.grouperatp.ratp.sga.kafka.simulator.utils.KafkaSimulatorFactory;

/**
 * Classe de condiguration automatique du simulateur KAFKA
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 24 mars 2019 - 14:11:54
 */
@Configuration
@ConditionalOnClass({
	KafkaSimulator.class,
	KafkaSimulatorFactory.class
})
@EnableConfigurationProperties(SimulatorProperties.class)
public class KafkaSimulatorAutoConfiguration {
	
	/**
	 * Propriété de configuration du simulateur KAFKA
	 */
	@Autowired
	private SimulatorProperties simulatorProperties;
	
	/**
	 * Méthode permettant de créer un bean kafkaSimulator Factory
	 * @return Bean kafkaSimulatorFactory
	 */
	@ConditionalOnProperty(
			prefix = SimulatorProperties.SIMULATOR_PROPERTIES_PREFIX,
			name = "enabled",
			havingValue = "true",
			matchIfMissing = false
	)
	@Bean
	@ConditionalOnMissingBean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public KafkaSimulatorFactory kafkaSimulatorFactory() {
		
		// On retourne l'instance du Bean
		return new KafkaSimulatorFactory(simulatorProperties);
	}
	
	/**
	 * Méthode permettant de créer un bean kafkaSimulator 
	 * @return	Bean kafkaSimulator
	 */
	@ConditionalOnProperty(
			prefix = SimulatorProperties.SIMULATOR_PROPERTIES_PREFIX,
			name = "enabled",
			havingValue = "true",
			matchIfMissing = false
	)
	@Bean
	@ConditionalOnMissingBean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public KafkaSimulator kafkaSimulator(KafkaSimulatorFactory kafkaSimulatorFactory) {
		
		// On retourne l'instance du simulateur KAFKA
		return kafkaSimulatorFactory.getInstance();
	}
}
