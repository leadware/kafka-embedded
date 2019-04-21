package net.leadware.kafka.embedded.sample.consumer.model;

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

import org.apache.kafka.common.record.TimestampType;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Classe représetant une donnée consommée
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 4 avr. 2019 - 08:29:15
 */
@ApiModel(description = "Information sur un enregistrement consommé sur KAFKA (ONLY FOR DEMO)")
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
