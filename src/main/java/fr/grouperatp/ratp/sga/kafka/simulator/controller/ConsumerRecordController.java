/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fr.grouperatp.ratp.sga.kafka.simulator.KafkaSimulator;
import fr.grouperatp.ratp.sga.kafka.simulator.model.ConsumedRecord;
import fr.grouperatp.ratp.sga.kafka.simulator.properties.SimulatorProperties;
import fr.grouperatp.ratp.sga.kafka.simulator.utils.KafkaSimulatorFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**
 * Controleur Rest des operations sur la liste des enregistrements consommés 
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 4 avr. 2019 - 08:17:24
 */
@Api(description = "Service Rest de gestion de la liste des enregistrements consommés", 
	 produces = MediaType.APPLICATION_JSON_VALUE, 
	 consumes = MediaType.APPLICATION_JSON_VALUE)
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
		path = "/simulator/recrcords/api/1.0", 
		produces = { MediaType.APPLICATION_JSON_VALUE }, 
		consumes = { MediaType.APPLICATION_JSON_VALUE }
)
@Deprecated
public class ConsumerRecordController {
	
	/**
	 * Liste des enregistrements consommés
	 */
	@Autowired
	private List<ConsumedRecord> consumerRecords;
	
	/**
	 * Méthode permettant de lister les groupes de consommateurs du simulateur
	 * @return	Liste des consommateurs
	 */
	@ApiOperation(value = "Opération de listage des enregistrements consommés")
	@ApiResponse(message = "Liste des enregistrements consommés", code = 200)
	@GetMapping(consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public List<ConsumedRecord> listRecords() {
		
		// On retourne la liste de groupe de consommateurs
		return consumerRecords;
	}	
}
