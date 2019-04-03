/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.controller;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fr.grouperatp.ratp.sga.kafka.simulator.KafkaSimulator;
import fr.grouperatp.ratp.sga.kafka.simulator.model.ConsumerGroup;
import fr.grouperatp.ratp.sga.kafka.simulator.model.ConsumerGroupOffset;
import fr.grouperatp.ratp.sga.kafka.simulator.properties.SimulatorProperties;
import fr.grouperatp.ratp.sga.kafka.simulator.utils.KafkaSimulatorFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;

/**
 * Controleur Rest des operations sur les groupes de consommateurs 
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 3 avr. 2019 - 07:26:54
 */
@Api(description = "Service Rest de gestion des groupes de consommateurs", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
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
		path = "/simulator/consumer/api/1.0", 
		produces = { MediaType.APPLICATION_JSON_VALUE }, 
		consumes = { MediaType.APPLICATION_JSON_VALUE }
)
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
	@ApiOperation(value = "Opération de listage des groupes de consommateurs Kafka")
	@ApiResponse(message = "Liste des groupes de consommateurs trouvés", code = 200)
	@GetMapping(path = "/groups", consumes = MediaType.ALL_VALUE)
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
	@ApiOperation(value = "Opération de listage des offsets d'un groupe de consommateurs Kafka")
	@ApiResponse(message = "Liste des offsets d'un groupe de consommateurs trouvés", code = 200)
	@GetMapping(path = "/groups/{groupId}/offsets", consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public List<ConsumerGroupOffset> listConsumerGroupOffsets(@ApiParam(name = "groupId", required = true) 
														      @PathVariable("groupId")
															  @NotEmpty  String groupId) {
		
		// On retourne la liste d'offsets du groupe de consommateurs
		return kafkaSimulator.listConsumerGroupOffsets(groupId);
	}
	
}
