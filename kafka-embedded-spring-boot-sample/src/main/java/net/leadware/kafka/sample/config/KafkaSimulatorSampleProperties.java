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

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Classe de configuration du simulateur kafka 
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 27 avr. 2019 - 19:30:13
 */
@Component
@PropertySource(name = "kafkaEmbeddedConfiguration", value = {"${net.leadware.kafka.sample.config.simulator}"})
public class KafkaSimulatorSampleProperties {}
