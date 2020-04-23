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
 * Classe représentant un groupe de consommateurs
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 2 avr. 2019 - 22:37:59
 */
@Schema(description = "Information sur un groupe de consommateur KAFKA")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ConsumerGroup {
	
	/**
	 * Nom du groupe
	 */
	@Schema(accessMode = AccessMode.READ_WRITE, name = "consumerGroupId", required = true, description = "ID du groupe de consommateurs")
	@JsonProperty(required = true, value = "consumerGroupId")
	@JsonPropertyDescription("ID du groupe de consommateurs")
	private String id;
	
	/**
	 * Le groupe est-il simple
	 */
	@Schema(accessMode = AccessMode.READ_WRITE, name = "consumerGroupId", required = true, description = "ID du groupe de consommateurs")
	@JsonProperty(required = true, value = "simpleGroup", defaultValue = "true")
	@JsonPropertyDescription("Type de groupe (simple ou non)")
	private Boolean simpleGroup;
}
