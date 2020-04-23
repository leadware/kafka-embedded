package net.leadware.kafka.embedded.model;

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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Classe représentant un topic
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 2 avr. 2019 - 22:32:33
 */
@Schema(description = "Information sur un topic du simulateur KAFKA")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Topic {
	
	/**
	 * Nom du topic
	 */
	@Schema(accessMode = AccessMode.READ_WRITE, name = "topicName", required = true, description = "Nom du topic")
	@JsonProperty(required = true, value = "topicName")
	@JsonPropertyDescription("Nom du topic KAFKA")
	private String name;
	
	/**
	 * Etat interne ou classique du topic
	 */
	@Schema(accessMode = AccessMode.READ_WRITE, name = "topicInternal", required = true, description = "État interne ou non du topic (Interne : groupe de consommateurs, partitions, etc...)")
	@JsonProperty(required = true, value = "topicInternal", defaultValue = "false")
	@JsonPropertyDescription("État interne ou non du topic (Interne : groupe de consommateurs, partitions, etc...)")
	private boolean internal;
}
