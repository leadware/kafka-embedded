package net.leadware.kafka.embedded.sample.consumer;

/*-
 * #%L
 * Apache Kafka Embedded Server
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2013 - 2019 Leadware
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
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

import net.leadware.kafka.embedded.KafkaSimulator;
import net.leadware.kafka.embedded.autoconfigure.KafkaEmbeddedAutoConfiguration;
import net.leadware.kafka.embedded.sample.consumer.model.ConsumedRecord;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Classe de configuration du consommateur de test pour demo swagger
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 4 avr. 2019
 */
@Configuration
@EnableSwagger2
@Import(KafkaEmbeddedAutoConfiguration.class)
public class KafkaSimulatorTestConsumerConfiguration {
	
	/**
	 * Méthode permettant de construire la liste d'enregistrement consommées par le consommateur KAFKA
	 * @return	Liste d'enregistrement
	 */
	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public List<ConsumedRecord> consumerRecords() {
		
		// On retourne la liste
		return new ArrayList<>();
	}
	
	/**
	 * Méthode de construction du Listener Kafka 
	 * @param kafkaSimulator	Simulateur Kafka
	 * @param consumerRecords Liste d'enregistrements consommés
	 * @return	Listener Kafka
	 */
	@Bean(initMethod = "start", destroyMethod = "stop")
	public KafkaMessageListenerContainer<String, String> kafkaListenerContainerFactory(KafkaSimulator kafkaSimulator, List<ConsumedRecord> consumerRecords) {

		// Propriétés du producer
		Map<String, Object> consumerProperties = new HashMap<>();
		
		// Positionnement des URLs de serveurs KAFKA
		consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaSimulator.getInternalProducerBrokersUrls());
		
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
			
			// Ajout de l'enregistrement dans la liste
			consumerRecords.add(new ConsumedRecord(record.topic(), record.partition(), 
												   record.offset(), record.timestamp(), 
												   record.timestampType(), 
												   record.serializedKeySize(), 
												   record.serializedValueSize(), 
												   record.key(), record.value()));
			
			// Ajout dans la liste des recourds
			System.out.println("==========================================");
			System.out.println(record);
			System.out.println("==========================================");
		});
		
		// On retourne le listener
		return kafkaMessageListenerContainer;
	}

	/**
	 * Methode de construction de la configuration de documentation d'API
	 * @return	Configuration de documentation d'API
	 */
	@Bean
	public Docket sampleApi() {
		
		// Construction d
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("net.leadware.kafka.embedded"))
				.paths(PathSelectors.any())
				.build();
	}
}
