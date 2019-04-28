package net.leadware.kafka.sample.config;

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
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import lombok.extern.slf4j.Slf4j;
import net.leadware.kafka.embedded.tools.SimulatorUtils;
import net.leadware.kafka.sample.consumer.model.ConsumedRecord;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Classe de configuration du consommateur de test pour demo swagger
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 4 avr. 2019
 */
@Configuration
@EnableSwagger2
@Slf4j
public class KafkaSimulatorSampleConsumerConfiguration {
	
	/**
	 * Configuration du consommateur
	 */
	@Autowired
	private KafkaSimulatorSampleConsumerProperties consumerConfig;
	
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
	 * @param consumerRecords Liste d'enregistrements consommés
	 * @return	Listener Kafka
	 */
	@Bean(initMethod = "start", destroyMethod = "stop")
	public KafkaMessageListenerContainer<String, String> kafkaListenerContainerFactory(List<ConsumedRecord> consumerRecords) {

		// Propriétés du producer
		Map<String, Object> consumerProperties = new HashMap<>();
		
		// Positionnement des URLs de serveurs KAFKA
		consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerConfig.getBootstrapServers());
		
		// Positionnement de l'ID du client
		consumerProperties.put(ConsumerConfig.CLIENT_ID_CONFIG, consumerConfig.getClientId());
		
		// Positionnement de l'ID du groupe
		consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, consumerConfig.getClientGroupId());
		
		// Positionnement de la classe de deserialisation des clés
		consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		
		// Positionnement de la classe de deserialisation des données
		consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		
		// Positionnement de des packages trustés pour la deserialisation
		consumerProperties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		
		// Positionnement du delai de recharche des metadonnees de topics (recharger la liste de topics toutes les 5)
		consumerProperties.put(ConsumerConfig.METADATA_MAX_AGE_CONFIG, consumerConfig.getMetadataMaxAge());

		// Positionnement de la localisation du keystore Consommateur
		consumerProperties.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, SimulatorUtils.getResolvedPath(consumerConfig.getKeystoreLocation()));
		
		// Positionnement de la clé du keystore Consommateur
		consumerProperties.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, consumerConfig.getKeystorePassword());
		
		// Positionnement du mot de passe de la clé Consommateur
		consumerProperties.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, consumerConfig.getKeyPassword());
		
		// Positionnement de la localisation du truststore Consommateur
		consumerProperties.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, SimulatorUtils.getResolvedPath(consumerConfig.getTruststoreLocation()));
		
		// Positionnement de la clé du truststore Consommateur
		consumerProperties.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, consumerConfig.getTruststorePassword());
		
		// Positionnement du type de cle
		consumerProperties.put(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, "JKS");
		
		consumerProperties.put("ssl.enabled.protocols", "TLSv1.2,TLSv1.1,TLSv1");
		
		// Positionnement du protocol de securite
		consumerProperties.put("security.protocol", consumerConfig.getSecurityProtocol());
		
		// Fabrique de consommateurs
		ConsumerFactory<String, String> kafkaConsumerFactory = new DefaultKafkaConsumerFactory<>(consumerProperties);
		
		// Container Propertie
		ContainerProperties containerPorperties = new ContainerProperties(Pattern.compile(consumerConfig.getTopicPattern()));
		
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
		
		// Log
		log.debug("Création du Bean swagger d'exposition de la documentation de l'API Kafka Embedded");
		
		// Construction d
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("KafkaSampleConsumer")
				.select()
				.apis(RequestHandlerSelectors.basePackage("net.leadware.kafka.sample"))
				.paths(PathSelectors.any())
				.build();
	}
}
