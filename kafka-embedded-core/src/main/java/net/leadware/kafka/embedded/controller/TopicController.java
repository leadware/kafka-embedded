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
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.leadware.kafka.embedded.KafkaSimulator;
import net.leadware.kafka.embedded.model.Topic;
import net.leadware.kafka.embedded.properties.SimulatorProperties;
import net.leadware.kafka.embedded.utils.KafkaSimulatorFactory;

/**
 * Controleur Rest des opérations sur les topics
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 2 avr. 2019 - 22:22:52
 */
@Tag(name = "Gestion de topics", description = "Service Rest de gestion des topics")
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
		path = "/kafka/simulator/api/1.0/topics", 
		produces = { MediaType.APPLICATION_JSON_VALUE }
)
@Slf4j
@Validated
public class TopicController {
	
	/**
	 * Simulateur KAFKA
	 */
	@Autowired
	private KafkaSimulator kafkaSimulator;
	
	/**
	 * Méthode permettant de créer un topic dans le simulateur
	 * @param topicName	Nom du topic a creer
	 */
	@Operation(
		description = "Opération de création d'un topic", 
		method = "PUT"
	)
	@PutMapping(path = "/{topicName}")
	@ResponseStatus(code = HttpStatus.CREATED)
	public void createTopicByName(@Parameter(name = "topicName", required = true, description = "Nom du topic à créer") 
								  @PathVariable("topicName")
								  @NotEmpty(message = "Le paramètre 'topicName' doit être renseigné")
								  String topicName) {
		
		// Log
		log.debug("Création du topic : [{}]", topicName);
		
		// On cree le topic
		kafkaSimulator.createTopics(topicName);
	}
	
	/**
	 * Méthode permettant de lister les topics du simulateur
	 * @param internal Etat de visibilité (interne ou non du topic)
	 * @return	Liste des topics
	 */
	@Operation(
		description = "Opération de listage des topics en fonction de leur état de visibilité", 
		method = "GET",
		responses = {
			@ApiResponse(description = "Liste des topics trouvés", responseCode = "200"),
			@ApiResponse(description = "Erreur survenue lors de l'opération", responseCode = "500")
		}
	)
	@GetMapping(path = "/{internal}")
	@ResponseBody
	public List<Topic> listTopic(@Parameter(name = "internal", required = true, description = "Topic interne ?") 
								 @RequestParam(name = "internal", required = true, defaultValue = "false") 
								 @NotNull(message = "Le paramètre 'internal' doit être renseigné")
								 Boolean internal) {
		
		// Log
		log.debug("Listage des topics [Interne = {}]", internal);
		
		// On retourne la liste de topics
		return kafkaSimulator.listTopics(internal);
	}
	
	/**
	 * Méthode permettant de supprimer un topic 
	 * @param topicName	Nom du topic
	 */
	@Operation(
		description = "Opération de suppression d'un topic à partir de son nom", 
		method = "DELETE"
	)
	@DeleteMapping(path = "/{topicName}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteTopic(@Parameter(name = "topicName", required = true, description = "Nom du topic à supprimer") 
						    @PathVariable("topicName")
							@NotEmpty(message = "Le paramètre 'topicName' doit être renseigné")
							String topicName) {
		
		// Log
		log.debug("Suppression du topic [{}]", topicName);
		
		// Suppression du topic
		kafkaSimulator.deleteTopics(topicName);
	}
}
