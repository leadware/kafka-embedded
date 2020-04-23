package net.leadware.kafka.embedded.controller;

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

import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.leadware.kafka.embedded.KafkaSimulator;
import net.leadware.kafka.embedded.model.ConsumerGroup;
import net.leadware.kafka.embedded.model.ConsumerGroupOffset;
import net.leadware.kafka.embedded.properties.SimulatorProperties;
import net.leadware.kafka.embedded.utils.KafkaSimulatorFactory;

/**
 * Controleur Rest des operations sur les groupes de consommateurs 
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 3 avr. 2019 - 07:26:54
 */
@Tag(name = "Gestion des groupes de consomateurs", description = "Service Rest de gestion des groupes de consommateurs")
@ConditionalOnClass({
	KafkaSimulator.class,
	KafkaSimulatorFactory.class
})
@ConditionalOnProperty(
		prefix = SimulatorProperties.SIMULATOR_PROPERTIES_PREFIX,
		name = "enabled",
		havingValue = "true",
		matchIfMissing = false
)
@RestController
@RequestMapping(
		path = "/simulator/api/1.0/consumers/groups", 
		produces = { MediaType.APPLICATION_JSON_VALUE }
)
@Validated
public class ConsumerGroupController {
	
	/**
	 * Simulateur KAFKA
	 */
	@Autowired
	private KafkaSimulator kafkaSimulator;
	
	/**
	 * Méthode permettant de lister les groupes de consommateurs du simulateur
	 * @return	Liste des consommateurs
	 */
	@Operation(
		description = "Opération de listage des groupes de consommateurs Kafka", 
		method = "GET",
		responses = {
			@ApiResponse(description = "Liste des groupes de consommateurs trouvés", responseCode = "200"),
			@ApiResponse(description = "Erreur survenue lors de l'opération", responseCode = "500")
		}
	)
	@GetMapping
	@ResponseBody
	public List<ConsumerGroup> listConsumerGroup() {
		
		// On retourne la liste de groupe de consommateurs
		return kafkaSimulator.listConsumerGroup();
	}
	
	/**
	 * Méthode permettant de lister les offsets d'un groupe de consommateurs du simulateur
	 * @param groupId ID du groupe source
	 * @return	Liste des offsets d'un groupe de consommateurs du simulateur
	 */
	@Operation(
		description = "Opération de listage des offsets d'un groupe de consommateurs Kafka", 
		method = "GET",
		responses = {
			@ApiResponse(description = "Liste des offsets d'un groupe de consommateurs trouvés", responseCode = "200"),
			@ApiResponse(description = "Erreur survenue lors de l'opération", responseCode = "500")
		}
	)
	@GetMapping(path = "/{groupId}/offsets")
	@ResponseBody
	public List<ConsumerGroupOffset> listConsumerGroupOffsets(@Parameter(name = "groupId", required = true, allowEmptyValue = false) 
														      @PathVariable("groupId")
															  @NotEmpty(message = "Le paramètre 'groupId' doit être renseigné")
															  String groupId) {
		
		// On retourne la liste d'offsets du groupe de consommateurs
		return kafkaSimulator.listConsumerGroupOffsets(groupId);
	}
}
