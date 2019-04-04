/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.model;

import org.apache.kafka.common.record.TimestampType;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Classe représetant une donnée consommée
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 4 avr. 2019 - 08:29:15
 */
@ApiModel(description = "Information sur un enregistrement consommé sur KAFKA")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ConsumedRecord {
	
	/**
	 * Topic source
	 */
	private String topic;
	
	/**
	 * Partition source
	 */
    private int partition;
    
    /**
     * Offset de la donnée
     */
    private long offset;
    
    /**
     * Date et heure de livraison
     */
    private long timestamp;
    
    /**
     * Type d'horodatage
     */
    private TimestampType timestampType;
    
    /**
     * Taille de la clé
     */
    private int serializedKeySize;
    
    /**
     * Taille de la valeur
     */
    private int serializedValueSize;
    
    /**
     * Clé
     */
    private String key;
    
    /**
     * Valeur
     */
    private String value;
}
