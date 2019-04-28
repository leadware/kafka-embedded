/**
 * RATP :: SIT :: I2V :: SGA
 */
package net.leadware.kafka.sample.config;

/*-
 * #%L
 * Apache Kafka Embedded Server
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2019 Leadware
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

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * Classe de configuration du consommateur Kafka
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 27 avr. 2019 - 19:06:57
 */
@Component
@PropertySource(name = "consumerConfiguration", value = {"${net.leadware.kafka.sample.config.consumer}"})
@ConfigurationProperties(prefix="net.leadware.kafka.sample.config.consumer")
@Getter
@Setter
public class KafkaSimulatorSampleConsumerProperties {
	
	/**
	 * Liste des URL des Brokers Kafka
	 */
	private String bootstrapServers;
	
	/**
	 * Identifiant du consommateur
	 */
	private String clientId;
	
	/**
	 * Identifiant du groupe du consommateur
	 */
	private String clientGroupId;
	
	/**
	 * Durée maximale de recharge des metadonnees du consommateur
	 */
	private String metadataMaxAge;
	
	/**
	 * Pattern des topics d'abonnement automatique
	 */
	private String topicPattern;
	
	/**
	 * Protocole de securite du consomateur
	 */
	private String securityProtocol;
	
	/**
	 * Chemin vers le fichier trustore
	 */
	private String truststoreLocation;
	
	/**
	 * Mot de passe du fichier truststore
	 */
	private String truststorePassword;
	
	/**
	 * Chemin vers le fichier keystore
	 */
	private String keystoreLocation;
	
	/**
	 * Mot de passe du magasin de clés
	 */
	private String keystorePassword;
	
	/**
	 * Mot de passe de la clé
	 */
	private String keyPassword;
}
