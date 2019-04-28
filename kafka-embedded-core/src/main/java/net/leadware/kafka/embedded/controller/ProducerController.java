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

import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import net.leadware.bean.validation.ext.annotations.json.FormatType;
import net.leadware.bean.validation.ext.annotations.json.StringFormatValidator;
import net.leadware.kafka.embedded.KafkaSimulator;
import net.leadware.kafka.embedded.properties.SimulatorProperties;
import net.leadware.kafka.embedded.utils.KafkaSimulatorFactory;

/**
 * Service Rest de production de message Kafka sur le simulateur 
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 3 avr. 2019 - 08:26:44
 */
@Api(description = "Service Rest de production de message Kafka sur le simulateur", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
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
		path = "/kafka/simulator/api/1.0/producers", 
		produces = { MediaType.APPLICATION_JSON_VALUE }
)
@Validated
public class ProducerController {

	/**
	 * Simulateur KAFKA
	 */
	@Autowired
	private KafkaSimulator kafkaSimulator;
	
	/**
	 * Methode permettant d'envoyer un message sur un topic du simulateur 
	 * @param topicName	Nom du topic d'envoie
	 * @param messageKey	Clé du message
	 * @param message	Contenu du message
	 */
	@PostMapping(
			path = "/send/{topicName}/{messageKey}", 
			consumes = MediaType.APPLICATION_JSON_VALUE
	)
	@ResponseStatus(value = HttpStatus.OK, reason = "Message envoyé dans la file")
	public void sendMessage(@ApiParam(name = "topicName", value = "Nom du topic d'envoi", required = true) 
							@PathVariable("topicName") 
							@NotEmpty(message = "Le paramètre 'topicName' doit être renseigné")
							String topicName, 
							
							@ApiParam(name = "messageKey", value = "Clé du message KAFKA" ,required = true) 
							@PathVariable("messageKey") 
							@NotEmpty(message = "Le paramètre 'messageKey' doit être renseigné")
							String messageKey,
							
							@ApiParam(name = "message", value = "Contenu du message au format JSON", required = true)
							@RequestBody(required = true)
							@StringFormatValidator(format = FormatType.JSON, message = "Le contenu du message doit être au format JSON")
							String message) {
		
		// Envoi du message
		kafkaSimulator.sendMessage(topicName, messageKey, message);
	}

	/**
	 * Methode permettant d'envoyer un message sur un topic du simulateur 
	 * @param topicName	Nom du topic d'envoie
	 * @param message	Contenu du message
	 */
	@PostMapping(
			path = "/send/{topicName}", 
			consumes = MediaType.APPLICATION_JSON_VALUE
	)
	@ResponseStatus(value = HttpStatus.OK, reason = "Message envoyé dans la file")
	public void sendMessage(@ApiParam(name = "topicName", value = "Nom du topic d'envoi", required = true) 
							@PathVariable("topicName") 
							@NotEmpty(message = "Le paramètre 'topicName' doit être renseigné")
							String topicName, 
							
							@ApiParam(name = "message", value = "Contenu du message au format JSON", required = true)
							@RequestBody(required = true)
							@StringFormatValidator(format = FormatType.JSON) String message) {
		
		// Envoi du message
		kafkaSimulator.sendMessage(topicName, message);
	}
}
