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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Classe représentant un offset de groupe de consommateurs
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 3 avr. 2019 - 07:59:25
 */
@ApiModel(description = "Information sur l'offset d'un groupe de consommateurs du simulateur KAFKA")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ConsumerGroupOffset {
	
	/**
	 * Nom du topic
	 */
	@ApiModelProperty(accessMode = AccessMode.READ_ONLY, name = "topicName", required = true, value = "Nom du topic")
	@JsonProperty(required = true, value = "topicName")
	@JsonPropertyDescription("Nom du topic KAFKA")
	private String topicName;
	
	/**
	 * Numero de partition du topic
	 */
	@ApiModelProperty(accessMode = AccessMode.READ_ONLY, name = "partitionId", required = true, value = "ID de la partition du topic")
	@JsonProperty(required = true, value = "partitionId")
	@JsonPropertyDescription("ID de la partition du topic")
	private Integer partitionId;
	
	/**
	 * Offset de la partition
	 */
	@ApiModelProperty(accessMode = AccessMode.READ_ONLY, name = "offset", required = true, value = "Offset de la partition")
	@JsonProperty(required = true, value = "offset")
	@JsonPropertyDescription("Offset de la partition")
	private Long offset;
	
	/**
	 * Meta donnees de l'offset
	 */
	@ApiModelProperty(accessMode = AccessMode.READ_ONLY, name = "offsetMetadata", required = false, value = "Metadonnées")
	@JsonProperty(required = true, value = "offsetMetadata")
	@JsonPropertyDescription("Offset de la partition")
	private String offsetMetadata;
}
