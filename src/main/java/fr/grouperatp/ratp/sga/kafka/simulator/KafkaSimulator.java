/**
 * RATP :: SIT :: I2V :: SGA
 */
package fr.grouperatp.ratp.sga.kafka.simulator;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

import kafka.zk.EmbeddedZookeeper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Classe représentant le simulateur KAFKA
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 23 mars 2019 - 23:06:45
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class KafkaSimulator {
	
	/**
	 * Broker Kafka Embarqué
	 */
	private EmbeddedKafkaBroker embeddedKafkaBroker;
	
	/**
	 * Moniteur de Clueter Zookeeper embarqué
	 */
	private EmbeddedZookeeper embeddedZookeeper;
	
	/**
	 * Simulateur Administration Kafka
	 */
	private KafkaAdmin kafkaAdmin;
	
	/**
	 * Client d'administration KAFKA embarqué
	 */
	private AdminClient adminClient;
	
	/**
	 * Méthode permettant d'initialiser le simulateur KAFKA
	 */
	@PostConstruct
	public void initialize() {
		
		// Initialize embedded broker
		embeddedKafkaBroker.afterPropertiesSet();

		// Get Admin Properties
		Map<String, Object> adminConfigs = new HashMap<>();
		
		// Put Propertie
		adminConfigs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafkaBroker.getBrokersAsString());
		
		// Instantiate Admin
		kafkaAdmin = new KafkaAdmin(adminConfigs);
		
		// Get Broker Admin Client
		adminClient = AdminClient.create(kafkaAdmin.getConfig());
		
		// Zookeeper serveur
		embeddedZookeeper = embeddedKafkaBroker.getZookeeper();
	}
	
	/**
	 * Méthode permettant de détruire le simulateur KAFKA
	 */
	@PreDestroy
	public void destroy() {
		
		// Destroy Embedded Broker
		embeddedKafkaBroker.destroy();		
	}
}
