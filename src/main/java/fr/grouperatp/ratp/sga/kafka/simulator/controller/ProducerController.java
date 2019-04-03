/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.controller;

import javax.validation.constraints.NotEmpty;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.grouperatp.ratp.sga.kafka.simulator.KafkaSimulator;
import fr.grouperatp.ratp.sga.kafka.simulator.properties.SimulatorProperties;
import fr.grouperatp.ratp.sga.kafka.simulator.utils.KafkaSimulatorFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Service Rest de production de message Kafka sur le simulateur 
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
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
		path = "/simulator/producer/api/1.0", 
		produces = { MediaType.APPLICATION_JSON_VALUE }, 
		consumes = { MediaType.APPLICATION_JSON_VALUE }
)
public class ProducerController {
	
	/**
	 * Méthode permettant d'envoyer un message sur un topic du simulateur 
	 * @param topicName	Nom du topic d'envoie
	 * @param messageKey	Cle du message Kafka
	 * @param payload	Contenu du message Kafka
	 */
	@ApiOperation(value = "Opération d'envoie d'un message sur un topic du simulateur kafka")
	@PostMapping(path = "/send/{topicName}/{messageKey}", consumes = MediaType.ALL_VALUE)
	@ResponseStatus(code = HttpStatus.OK)
	public void sendMessage(@ApiParam(name = "topicName", required = true) 
							@PathVariable("topicName") 
							@NotEmpty String topicName, 
							
							@ApiParam(name = "messageKey", required = true) 
							@PathVariable("topicName") 
							@NotEmpty String messageKey,
							
							@ApiParam(name = "messageBody", required = true)
							@NotEmpty 
							@RequestBody HttpEntity<String> payload) {}
	
}
