/**
 * RATP :: SIT :: I2V :: SGA
 */
package net.leadware.kafka.sample.config;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriTemplate;

import com.fasterxml.classmate.TypeResolver;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Classe de configuration swagger 
 * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
 * @since 26 mai 2019 - 12:04:41
 */
@Configuration
@EnableSwagger2
@Slf4j
public class SwaggerConfig {
	
    /**
     * Méthode permettant de construire le Bean Plugin Swagger permettant de construire la spécification des opération RESt en corrigeant les bugs liées aux Endpoints Spring-Boot Actuator
     * @author <a href="https://github.com/rlippolis/spring-boot2-actuator-swagger/blob/master/src/main/java/com/rlippolis/spring/springboot2actuatorswagger/config/SwaggerConfig.java">Riccardo Lippolis</a>
     * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a> 
     * @param typeResolver	Extracteur d'information sur les classe accessibles de l'API
     * @return	Plugin de construction des specification des endpoints
     */
    @Bean
    @ConditionalOnProperty(name = "dirty.fix.enabled", havingValue = "true", matchIfMissing = true)
    public OperationBuilderPlugin operationBuilderPluginForCorrectingActuatorEndpoints(final TypeResolver typeResolver) {
    	
    	// On renvoie l'instance de plugin
        return new OperationBuilderPluginForCorrectingActuatorEndpoints(typeResolver);
    }

	/**
	 * Methode de construction de la configuration de documentation d'API
	 * @return	Configuration de documentation d'API
	 */
	@Bean
	public Docket sampleApi() {
		
		// Log
		log.debug("Création du Bean swagger d'exposition de la documentation de l'API Kafka Embedded");
		
		// Construction d
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("KafkaSampleConsumer")
				.select()
				.apis(RequestHandlerSelectors.basePackage("net.leadware.kafka.sample"))
				.paths(PathSelectors.any())
				.build();
	}

	/**
	 * Methode de construction de la configuration de documentation d'API
	 * @return	Configuration de documentation d'API
	 */
	@Bean
	public Docket actuatorApi() {
		
		// Log
		log.debug("Création du Bean swagger d'exposition de la documentation de l'API Kafka Embedded");
		
		// Construction d
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("Actuator")
				.select()
				.paths(PathSelectors.regex("/actuator/.*"))
				.build();
	}
	
    /**
     * Classe clase d'implémentation d'un plugin de construction des spécification d'opérations RESt avec résolution du Bug lié aux endpoints Spring Boot Actuator
     * @author <a href="https://github.com/rlippolis/spring-boot2-actuator-swagger/blob/master/src/main/java/com/rlippolis/spring/springboot2actuatorswagger/config/SwaggerConfig.java">Riccardo Lippolis</a>
     * @author <a href="mailto:jean-jacques.etune-ngi@ratp.fr">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
     * @since 26 mai 2019 - 11:38:03
     */
    private static class OperationBuilderPluginForCorrectingActuatorEndpoints implements OperationBuilderPlugin {
    	
    	/**
    	 * Extracteur d'information sur les classe accessibles de l'API
    	 */
        private final TypeResolver typeResolver;
        
        /**
         * Constructeur avec initialisation de parametres
         * @param typeResolver Extracteur d'information sur les classe accessibles de l'API
         */
        OperationBuilderPluginForCorrectingActuatorEndpoints(final TypeResolver typeResolver) {
        	
        	// Initialisation de l'extracteur d'information sur les classe accessibles de l'API
            this.typeResolver = typeResolver;
        }
        
        /*
         * (non-Javadoc)
         * @see springfox.documentation.spi.service.OperationBuilderPlugin#apply(springfox.documentation.spi.service.contexts.OperationContext)
         */
        @Override
        public void apply(final OperationContext context) {
        	
        	// Suppression des paramètres de body pour les opérations GET et HEAD
            removeBodyParametersForReadMethods(context);
            
            // Ajout de parametre de chemin
            addOperationParametersForPathParams(context);
        }
        
        /*
         * (non-Javadoc)
         * @see org.springframework.plugin.core.Plugin#supports(java.lang.Object)
         */
        @Override
        public boolean supports(final DocumentationType delimiter) {
        	
        	// On renvoie true
            return true;
        }
        
        /**
         * Méthode permettant de supprimer la génération d'un paramètre pour les opérations GET et HEAD
         * @param context	Contexte de l'opération RESt
         */
        private void removeBodyParametersForReadMethods(final OperationContext context) {
        	
        	// Si l'opération des un GET ou un HEAD
        	if (HttpMethod.GET.equals(context.httpMethod()) || HttpMethod.HEAD.equals(context.httpMethod())) {
        		
        		// Obtention de a liste des paramètres
        		final List<Parameter> parameters = getParameters(context);
        		
        		// Suppression du paramètre de nom "body"
        		parameters.removeIf(param -> "body".equals(param.getName()));
        	}
        }
        
        /**
         * Méthode permettant d'ajouter des paramètres de chemin au contexte de l'opération
         * @param context	Contexte de l'opération
         */
        private void addOperationParametersForPathParams(final OperationContext context) {
        	
        	// Construction d'un template d'URI
        	final UriTemplate uriTemplate = new UriTemplate(context.requestMappingPattern());
        	
        	// Création d'un paramètre pour chacune des variable du template
        	final List<Parameter> pathParams = uriTemplate.getVariableNames().stream()
        																	 .map(this::createPathParameter)
        																	 .collect(Collectors.toList());
        	
        	// Ajout de la liste de paramètre dans le constructeur de la spécification de l'opération
        	context.operationBuilder().parameters(pathParams);
        }
        
        /**
         * Méthode permettant de créer un paramètre de chemin à partir de son nom
         * @param pathParam	Nom du paramètre à créer
         * @return	Paramètre crée
         */
        private Parameter createPathParameter(final String pathParam) {
        	
        	// Construction et renvoi du paramètre
            return new ParameterBuilder()
                    .name(pathParam)
                    .description(pathParam)
                    .required(true)
                    .modelRef(new ModelRef("string"))
                    .type(typeResolver.resolve(String.class))
                    .parameterType("path")
                    .build();
        }
        
        /**
         * Méthode permettant d'obtenir la liste des paramètres de l'opération RESt
         * @param context	Contexte de l'opération
         * @return	Liste des paramètres
         */
        @SuppressWarnings("unchecked")
        private List<Parameter> getParameters(final OperationContext context) {
        	
        	// Instance courante du Constructeur d'opération
            final OperationBuilder operationBuilder = context.operationBuilder();
            
            try {
            	
            	// Obtention de la propriété "parameters"
                Field paramField = OperationBuilder.class.getDeclaredField("parameters");
                
                // Positionnement du flag d'accessibilité à true
                paramField.setAccessible(true);
                
                // Obtention de la valeur du champ pour l'instance en cours du constructeur d'opération
                return (List<Parameter>) paramField.get(operationBuilder);
                
            } catch (NoSuchFieldException | IllegalAccessException e) {
            	
            	// Relance de l'exception
                throw new RuntimeException("Unable to modify parameter field!", e);
            }
        }
    }
}
