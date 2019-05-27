package net.leadware.kafka.embedded.admin.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

@EnableAdminServer
@SpringBootApplication
public class KafkaEmbeddedSpringBootSampleAdminServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaEmbeddedSpringBootSampleAdminServerApplication.class, args);
	}

}
