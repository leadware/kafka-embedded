/**
 * RATP :: SIT :: I2V :: SGA
 */
package net.leadware.kafka.embedded.sample.consumer.controller;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import net.leadware.kafka.embedded.KafkaSimulator;
import net.leadware.kafka.embedded.sample.consumer.model.ConsumedRecord;
import net.leadware.kafka.embedded.utils.KafkaSimulatorFactory;

/**
 * Controleur Rest des operations sur la liste des enregistrements consommés 
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 4 avr. 2019 - 08:17:24
 */
@Api(description = "Service Rest de gestion de la liste des enregistrements consommés (ONLY FOR DEMO)", 
	 produces = MediaType.APPLICATION_JSON_VALUE, 
	 consumes = MediaType.APPLICATION_JSON_VALUE)
@ConditionalOnClass({
	KafkaSimulator.class,
	KafkaSimulatorFactory.class
})
@RestController
@RequestMapping(
		path = "/simulator/records/api/1.0", 
		produces = { MediaType.APPLICATION_JSON_VALUE }, 
		consumes = { MediaType.APPLICATION_JSON_VALUE }
)
public class ConsumerRecordTestController {
	
	/**
	 * Liste des enregistrements consommés
	 */
	@Autowired
	private List<ConsumedRecord> consumerRecords;
	
	/**
	 * Méthode permettant de lister les groupes de consommateurs du simulateur
	 * @return	Liste des consommateurs
	 */
	@ApiOperation(value = "Opération de listage des enregistrements consommés (ONLY FOR DEMO)")
	@ApiResponse(message = "Liste des enregistrements consommés", code = 200)
	@GetMapping(consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public List<ConsumedRecord> listRecords() {
		
		// On retourne la liste de groupe de consommateurs
		return consumerRecords;
	}	
}
