/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.tools;

import java.io.File;
import java.io.FileNotFoundException;

import org.springframework.util.ResourceUtils;

/**
 * Classe utilitaire du simulateur
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 31 mars 2019 - 12:10:22
 */
public class SimulatorUtils {

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
			
			// On retourne l'état de visibilié en lecture
			return getFile(path).canRead();
			
		} catch (Exception e) {
			
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
			
			// Obtention du File sur le repertoire temporaire
			return ResourceUtils.getFile(path);
			
		} catch (FileNotFoundException e) {
			
			// On relance
			throw new RuntimeException("Le chemin n'existe pas : " + path);
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
}
