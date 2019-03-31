/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.test.configuration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import fr.grouperatp.ratp.sga.kafka.simulator.KafkaSimulator;

/**
 * Classe de test de bon démarrage d'un simulateur non sécurisé
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 31 mars 2019 - 11:06:05
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = {"classpath:unsecure-simulator-config-application.properties"})
public class UsecureSimulatorInitializingTest {
	
	/**
	 * Simulator
	 */
	@Autowired
	private KafkaSimulator simulator;

	/**
	 * Before Test
	 */
	@Before
	public void before() {}
	
	/**
	 * After Test
	 */
	@After
	public void after() {}
	
	/**
	 * Méthode permettant de tester l'envoie et la réception d'une chaine de caracteres
	 */
	@Test
	public void testSendReceiveString() {
		
	}
}
