/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principale de l'application de simulation KAFKA
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 21 mars 2019 - 08:30:39
 */
@SpringBootApplication
public class KafkaSimulatorApplication {
	
	/**
	 * Méthode d'entrée de la classe principale 
	 * @param args	Tableau des argument passés en ligne de commande
	 */
	public static void main(String[] args) {
		
		// Demarrage du point d'entré SpringBoot
		SpringApplication.run(KafkaSimulatorApplication.class, args);
	}
}
