/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.model;

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
 * Classe représentant un groupe de consommateurs
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 2 avr. 2019 - 22:37:59
 */
@ApiModel(description = "Information sur un groupe de consommateur KAFKA")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ConsumerGroup {
	
	/**
	 * Nom du groupe
	 */
	@ApiModelProperty(accessMode = AccessMode.READ_WRITE, name = "consumerGroupName", required = true, value = "Nom du groupe de consommateurs")
	@JsonProperty(required = true, value = "consumerGroupName")
	@JsonPropertyDescription("Nom du groupe de consommateurs")
	private String name;
}
