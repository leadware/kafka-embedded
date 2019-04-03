/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.controller;

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

import fr.grouperatp.ratp.sga.kafka.simulator.KafkaSimulator;
import fr.grouperatp.ratp.sga.kafka.simulator.model.Topic;
import fr.grouperatp.ratp.sga.kafka.simulator.properties.SimulatorProperties;
import fr.grouperatp.ratp.sga.kafka.simulator.utils.KafkaSimulatorFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;

/**
 * Controleur Rest des opérations sur les topics
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
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
