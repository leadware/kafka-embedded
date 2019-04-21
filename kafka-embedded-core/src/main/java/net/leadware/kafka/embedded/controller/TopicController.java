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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import net.leadware.kafka.embedded.KafkaSimulator;
import net.leadware.kafka.embedded.model.Topic;
import net.leadware.kafka.embedded.properties.SimulatorProperties;
import net.leadware.kafka.embedded.utils.KafkaSimulatorFactory;

/**
 * Controleur Rest des opérations sur les topics
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 2 avr. 2019 - 22:22:52
 */
@Api(description = "Service Rest de gestion des topics", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
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
		path = "/simulator/topic/api/1.0", 
		produces = { MediaType.APPLICATION_JSON_VALUE }, 
		consumes = { MediaType.APPLICATION_JSON_VALUE }
)
public class TopicController {
	
	/**
	 * Simulateur KAFKA
	 */
	@Autowired
	private KafkaSimulator kafkaSimulator;
	
	/**
	 * Méthode permettant de créer un topic dans le simulateur
	 * @param topic	Topic a creer
	 */
	@ApiOperation(value = "Opération de création d'un topic")
	@PostMapping(path = "/create")
	@ResponseStatus(code = HttpStatus.CREATED)
	public void createTopic(@ApiParam(name = "topic", required = true) 
							@NotNull 
							@RequestBody Topic topic) {
		
		// On cree le topic
		kafkaSimulator.createTopics(topic.getName());
	}
	
	/**
	 * Méthode permettant de lister les topics du simulateur
	 * @param internal Etat de visibilité (interne ou non du topic)
	 * @return	Liste des topics
	 */
	@ApiOperation(value = "Opération de listage des topics en fonction de leur état de visibilité")
	@ApiResponse(message = "Liste des topics trouvés", code = 200)
	@GetMapping(path = "/topics/{internal}", consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public List<Topic> listTopic(@ApiParam(name = "internal", required = true) 
								 @PathVariable("internal") 
								 @NotNull Boolean internal) {
		
		// On retourne la liste de topics
		return kafkaSimulator.listTopics(internal);
	}
	
	/**
	 * Méthode permettant de supprimer un topic 
	 * @param topicName	Nom du topic
	 */
	@ApiOperation(value = "Opération de suppression d'un topic à partir de son nom")
	@DeleteMapping(path = "/{topicName}", consumes = MediaType.ALL_VALUE)
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteTopic(@ApiParam(name = "topicName", required = true) 
						    @PathVariable("topicName")
							@NotEmpty String topicName) {
		
		// Suppression du topic
		kafkaSimulator.deleteTopics(topicName);
	}
}
