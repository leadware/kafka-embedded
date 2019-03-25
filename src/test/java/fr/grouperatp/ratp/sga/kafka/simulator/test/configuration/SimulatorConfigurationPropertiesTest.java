/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator.test.configuration;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import fr.grouperatp.ratp.sga.kafka.simulator.properties.SimulatorProperties;

/**
 * Classe de test de chargement des propriétés de configuration du Simulateur
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 25 mars 2019 - 07:46:24
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = {"classpath:load-simulator-config-application.properties"})
public class SimulatorConfigurationPropertiesTest {
	
	/**
	 * Simulator properties
	 */
	@Autowired
	private SimulatorProperties simulatorProperties;
	

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
	 * Méthode permettant de tester le chargement des propriétés
	 */
	@Test
	public void testLoadedProperties() {
		
		// Assert Simulator properties is not null
		assertThat(simulatorProperties, is(notNullValue()));
		
		// Assert Initial topic List is set
		assertThat(simulatorProperties.getInitialTopics(), is(notNullValue()));
		
		// Assert there are 2 topics in initial list
		assertThat(simulatorProperties.getInitialTopics(), hasSize(2));
		
		// Assert that initial topics list contains some elements
		assertThat(simulatorProperties.getInitialTopics(), hasItems("HCPA", "DMES"));
	}
}
