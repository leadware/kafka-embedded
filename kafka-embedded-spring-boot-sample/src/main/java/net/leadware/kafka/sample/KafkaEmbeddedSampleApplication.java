package net.leadware.kafka.sample;

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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Classe principale de l'application de sample KAFKA Embedded
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 21 mars 2019 - 08:30:39
 */
@EnableDiscoveryClient
@SpringBootApplication
public class KafkaEmbeddedSampleApplication {
	
	/**
	 * Méthode d'entrée de la classe principale 
	 * @param args	Tableau des argument passés en ligne de commande
	 */
	public static void main(String[] args) {
		
		// Demarrage du point d'entré SpringBoot
		SpringApplication.run(KafkaEmbeddedSampleApplication.class, args);
	}
}
