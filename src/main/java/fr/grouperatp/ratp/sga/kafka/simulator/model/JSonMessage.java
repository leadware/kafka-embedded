package fr.grouperatp.ratp.sga.kafka.simulator.model;

import org.springframework.validation.annotation.Validated;

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
 * Classe représentant un message à envoyer via KAFKA
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 3 avr. 2019
 */
@ApiModel(description = "Message d'envoie sur le simulateur KAFKA")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Validated
public class JSonMessage {
	
	/**
	 * Topic d'envoi
	 */
	@ApiModelProperty(accessMode = AccessMode.READ_WRITE, name = "topicName", required = true, value = "Nom du topic")
	@JsonProperty(required = true, value = "topicName")
	@JsonPropertyDescription("Nom du topic KAFKA")
	private String topicName;
	
	/**
	 * Cle du message
	 */
	@ApiModelProperty(accessMode = AccessMode.READ_WRITE, name = "messageKey", required = true, value = "Clé du message à envoyer")
	@JsonProperty(required = true, value = "messageKey")
	@JsonPropertyDescription("Clé du message")
	private String messageKey;
	
	/**
	 * Corps du message
	 */
	@ApiModelProperty(accessMode = AccessMode.READ_WRITE, name = "messageBody", required = true, value = "Contenu du message à envoyer")
	@JsonProperty(required = true, value = "messageBody")
	@JsonPropertyDescription("Contenu du message")
	private String messageBody;
}
