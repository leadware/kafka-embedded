package net.leadware.kafka.embedded.tools;

/*-
 * #%L
 * Apache Kafka Embedded Server
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2019 Leadware
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

import java.util.Arrays;

import org.springframework.util.SocketUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Classe utilitaire du simulateur
 * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 31 mars 2019 - 12:10:22
 */
@Slf4j
public class SimulatorUtils {
	
	/**
	 * Port max privilégié (besoin de droit particulier pour qu'une application se binde dessus)
	 */
	public static final int SIMULATOR_GENERATED_MIN_PORT = 1024;
	
	/**
	 * Methode de recherche des ports libres excluant ceux du tableau en parametre
	 * @param excludes	Tableau des ports exclus
	 * @return	Port libre
	 */
	public static int findAvailablePortExcept(int...excludes) {
		
		// Log
		log.debug("Recherche d'un port libre non compris dans la liste [{}]", Arrays.asList(excludes));
		
		// Si le tableau est vide
		if(excludes == null || excludes.length == 0) return SocketUtils.findAvailableTcpPort(SIMULATOR_GENERATED_MIN_PORT);
		
		// Positionnement de la valeur minimale à générer par celle de la valeur maximum du tableau en paramètre
		int generatedPortMinValue = Arrays.stream(excludes).sorted().max().getAsInt() + 1;
		
		// Si la valeur max du tableau est inferieure à la valeur max que peut générer le Simulateur
		if(generatedPortMinValue < SIMULATOR_GENERATED_MIN_PORT ) generatedPortMinValue = SIMULATOR_GENERATED_MIN_PORT;
		
		// On retourne un port supérieur à la valeur minimale
		return SocketUtils.findAvailableTcpPort(generatedPortMinValue);
	}
}
