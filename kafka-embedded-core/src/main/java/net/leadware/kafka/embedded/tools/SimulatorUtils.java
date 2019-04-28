package net.leadware.kafka.embedded.tools;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import org.springframework.util.ResourceUtils;
import org.springframework.util.SocketUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

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
	 * Méthode permettant de vérifier chemin représente un fichier/repertoire en execution
	 * @param path	Chemin du fichier/repertoire
	 * @return	Etat de vivibilité fichier
	 */
	public static boolean isExecutable(String path) {
		
		try {
			
			// On retourne l'état de visibilié en ecriture
			return getFile(path).canExecute();
			
		} catch (Exception e) {
			
			// On retourne false
			return false;
		}
	}
	
	/**
	 * Méthode permettant de vérifier chemin représente un fichier/repertoire en ecriture
	 * @param path	Chemin du fichier/repertoire
	 * @return	Etat de vivibilité fichier
	 */
	public static boolean isWriteable(String path) {
		
		try {
			
			// On retourne l'état de visibilié en ecriture
			return getFile(path).canWrite();
			
		} catch (Exception e) {
			
			// On retourne false
			return false;
		}
	}
	
	/**
	 * Méthode permettant de vérifier chemin représente un fichier/repertoire en lecture
	 * @param path	Chemin du fichier/repertoire
	 * @return	Etat de vivibilité fichier
	 */
	public static boolean isReadeable(String path) {
		
		try {
			
			// log
			log.debug("Vérification de l'état de lecture du fichier [{}]", path);
			
			// On retourne l'état de visibilié en lecture
			return getFile(path).canRead();
			
		} catch (Exception e) {
			
			// Trace d'erreur
			e.printStackTrace();
			
			// On retourne false
			return false;
		}
	}
	
	/**
	 * Méthode permettant de vérifier chemin représente un fichier/repertoire caché
	 * @param path	Chemin du fichier/repertoire
	 * @return	Etat de vivibilité fichier
	 */
	public static boolean isHidden(String path) {
		
		try {
			
			// On retourne l'état de visibilié
			return getFile(path).isHidden();
			
		} catch (Exception e) {
			
			// On retourne false
			return false;
		}
	}
	
	/**
	 * Méthode permettant de vérifier chemin représente un fichier
	 * @param path	Chemin du fichier/repertoire
	 * @return	Etat de type fichier
	 */
	public static boolean isFile(String path) {
		
		try {
			
			// On retourne l'état d'existence
			return getFile(path).isFile();
			
		} catch (Exception e) {
			
			// On retourne false
			return false;
		}
	}
	
	/**
	 * Méthode permettant de vérifier chemin représente un repertoire
	 * @param path	Chemin du fichier/repertoire
	 * @return	Etat de type fichier
	 */
	public static boolean isDirectory(String path) {
		
		try {
			
			// On retourne l'état d'existence
			return getFile(path).isDirectory();
			
		} catch (Exception e) {
			
			// On retourne false
			return false;
		}
	}
	
	/**
	 * Méthode permettant de vérifier qu'un fichier (ou répertoire) existe
	 * @param path	Chemin du fichier/repertoire
	 * @return	Etat d'existence
	 */
	public static boolean fileExists(String path) {
		
		try {
			
			// On retourne 'état d'existence
			return getFile(path) != null;
			
		} catch (Exception e) {
			
			// On retourne false
			return false;
		}
	}
	
	/**
	 * Méthode permettant de construire un fichier à partir d'un chemin 
	 * @param path	Chemin source
	 * @return Fichier construit en cas d'existence
	 */
	public static File getFile(String path) {
		
		try {
			
			// Si le chemim est null
			if(path == null) throw new FileNotFoundException("Le fichier 'null' n'existe pas");
			
			// Log
			log.debug("Obtention de l'URL du chemin [{}]...", path);
			
			// resolution du chemin
			String resolvedPath = getResolvedPath(path);
			
			// Log
			log.debug("URL complète du chemin [{}] : [{}]", path, resolvedPath);
			
			// Si le fichier est dans une archive
			if(fileInArchive(resolvedPath)) {
				
				// On leve une exception
				throw new IOException("Impossible de construire un objet java.io.File depuis un fichier contenue dans une archive [{" + resolvedPath + "}]");
			}
			
			// Obtention du File sur le repertoire temporaire
			return ResourceUtils.getFile(path);
			
		} catch (FileNotFoundException e) {
			
			// Log
			log.debug("Le fichier [{}] n'existe pas.", path);
			
			// On relance
			throw new RuntimeException("Le chemin n'existe pas : " + path, e);
			
		} catch (IOException e) {
			
			// Log
			log.debug("Erreur survenue lors du traitement du fichier [{}] : [{}]", path, e.getMessage());
			
			// On relance
			throw new RuntimeException("Erreur survenue lors du traitement du fichier [{" + path + "}]", e);
		}
	}
	
	/**
	 * Méthode permettant d'obtenir un chemin resolu par Spring
	 * @param rawPath	Chemin brut
	 * @return Chemin de fichier resolu
	 */
	public static String getResolvedPath(String rawPath) {
		try {
			   
			   // Tentative d'obtention de l'URL
			   return ResourceUtils.getURL(rawPath).getFile();
			
		   } catch (Exception e) {
			   
			   // Return the original Path
			   return rawPath;
		   }
	}
	
	/**
	 * Methode permettant de tester si une chaine est au format JSON
	 * @param json	Chaine a tester
	 * @return	résultat du test
	 */
	public static boolean isValidJson(String json) {
		
		try {
			
			// Mapper JSon
			ObjectMapper mapper = new ObjectMapper();
			
			// Tentative de transformation JSon Objet
			mapper.readTree(json);
			
			// On retourne Trus si OK
			return true;
			
		} catch (IOException e) {
			
			// En cas d'exception
			return false;
		}
	}
	
	/**
	 * Methode de recherche des ports libres excluant ceux du tableau en parametre
	 * @param excludes	Tableau des ports exclus
	 * @return	Port libre
	 */
	public static int findAvailablePortExcept(int...excludes) {
		
		// Si le tableau est vide
		if(excludes == null || excludes.length == 0) return SocketUtils.findAvailableTcpPort(SIMULATOR_GENERATED_MIN_PORT);
		
		// Positionnement de la valeur minimale à générer par celle de la valeur maximum du tableau en paramètre
		int generatedPortMinValue = Arrays.stream(excludes).sorted().max().getAsInt() + 1;
		
		// Si la valeur max du tableau est inferieure à la valeur max que peut générer le Simulateur
		if(generatedPortMinValue < SIMULATOR_GENERATED_MIN_PORT ) generatedPortMinValue = SIMULATOR_GENERATED_MIN_PORT;
		
		// On retourne un port supérieur à la valeur minimale
		return SocketUtils.findAvailableTcpPort(generatedPortMinValue);
	}
	
	/**
	 * Méthode permettant de verifier si un chemin indique une ressource dans une archive
	 * @param path	Chemin a tester
	 * @return	Etat
	 */
	private static boolean fileInArchive(String path) {
		
		// On retourne le resultat
		return path.contains("!/");
		
	}
}
