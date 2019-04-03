/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.config;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import fr.grouperatp.ratp.sga.kafka.simulator.KafkaSimulator;
import fr.grouperatp.ratp.sga.kafka.simulator.properties.SimulatorProperties;
import fr.grouperatp.ratp.sga.kafka.simulator.utils.KafkaSimulatorFactory;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

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
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
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
	@ConditionalOnMissingBean
	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public KafkaSimulatorFactory kafkaSimulatorFactory() {
		
		// On retourne l'instance du Bean
		return new KafkaSimulatorFactory(simulatorProperties);
	}
	
	/**
	 * Méthode permettant de créer un bean kafkaSimulator
	 * @param kafkaSimulatorFactory Fabrique de simulateur Kafka
	 * @return	Bean kafkaSimulator
	 */
	@ConditionalOnProperty(
			prefix = SimulatorProperties.SIMULATOR_PROPERTIES_PREFIX,
			name = "enabled",
			havingValue = "true",
			matchIfMissing = false
	)	
	@ConditionalOnMissingBean
	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public KafkaSimulator kafkaSimulator(KafkaSimulatorFactory kafkaSimulatorFactory) {
		
		// On retourne l'instance du simulateur KAFKA
		return kafkaSimulatorFactory.getInstance();
	}
	
	
	
	/**
	 * Methode de construction de la configuration de documentation d'API
	 * @return	Configuration de documentation d'API
	 */
	@ConditionalOnProperty(
			prefix = SimulatorProperties.SIMULATOR_PROPERTIES_PREFIX,
			name = "enabled",
			havingValue = "true",
			matchIfMissing = false
	)
	@ConditionalOnMissingBean
	@Bean
	public Docket api() {
		
		// Construction d
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("fr.grouperatp.ratp.sga.kafka.simulator"))
				.paths(PathSelectors.any())
				.build();
	}
	
	/**
	 * Méthode de construction du Listener Kafka 
	 * @param kafkaSimulator	Simulateur Kafka
	 * @return	Listener Kafka
	 */
	@ConditionalOnProperty(
			prefix = SimulatorProperties.SIMULATOR_PROPERTIES_PREFIX,
			name = "enabled",
			havingValue = "true",
			matchIfMissing = false
	)
	@ConditionalOnMissingBean
	@Bean(initMethod = "start", destroyMethod = "stop")
	public KafkaMessageListenerContainer<String, String> kafkaListenerContainerFactory(KafkaSimulator kafkaSimulator) {

		// Propriétés du producer
		Map<String, Object> consumerProperties = new HashMap<>();
		
		// Positionnement des URLs de serveurs KAFKA
		consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaSimulator.getBrokersAsString());
		
		// Positionnement de l'ID du client
		consumerProperties.put(ConsumerConfig.CLIENT_ID_CONFIG, "simulator-consumer-01");
		
		// Positionnement de l'ID du groupe
		consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-01");
		
		// Positionnement de la classe de deserialisation des clés
		consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		
		// Positionnement de la classe de deserialisation des données
		consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		
		// Positionnement de des packages trustés pour la deserialisation
		consumerProperties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		
		// Fabrique de consommateurs
		ConsumerFactory<String, String> kafkaConsumerFactory = new DefaultKafkaConsumerFactory<>(consumerProperties);
		
		// Container Propertie
		ContainerProperties containerPorperties = new ContainerProperties(Pattern.compile(".*"));
		
		// MessageListener Container
		KafkaMessageListenerContainer<String, String> kafkaMessageListenerContainer = new KafkaMessageListenerContainer<>(kafkaConsumerFactory, containerPorperties);
		
		// Add MessageListener
		kafkaMessageListenerContainer.setupMessageListener((MessageListener<String, String>) record -> {
			
			// Ajout dans la liste des recourds
			System.out.println("========================");
			System.out.println(record);
			System.out.println("========================");
		});
		
		// On retourne le listener
		return kafkaMessageListenerContainer;
	}
}
