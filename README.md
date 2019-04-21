[![Leadware](http://leadware.net/medias/images/leadware/lw_logo_complet_sans_surlignement_2016_10_26_155X30.png)](http://www.leadware.net/)&nbsp;&nbsp;
![](markdown/version.svg)&nbsp;&nbsp;![](markdown/build-status.svg)


# Leadware KAFKA Embedded

Le principal objectif du projet KAFKA Embedded est de fournir aux développeurs, architectes, DevOps, une version embarquée du serveur Apache KAFKA, permettant de fournir les fonctionnalités principales d'un cluster KAFKA au sein d'une application et ainsi de faciliter les test de processus d'intégration de données dans un environnement d'intégration et de déploiement continue.


## Fonctionnalités ##

Le projet KAFKA Embedded fournit un sous-ensemble assez complet des fonctionnalités du serveur Apache KAFKA en version full.

* Configuration Multi Brokers
* Multi Listener par Brokers
* Répertoires de logs par broker
* Configurations globale des Buffers (send et receive)
* Configurations globale des threads networks et I/O
* Configurations globale des taille de requêtes
* Configuration PLAINTEXT et SSL (Keystore, Truststore)
* Initialisation des Topics
* Service Rest de gestion des topics
* Service Rest de production de données
* Autoconfiguration Spring Boot
* Documentation d'API via Swagger et Swagger-UI
* Exemple d'application utilisant le projet comme starter (kafka-embedded-spring-boot-sample)

## Présentation des modules du projet

* #### kafka-embedded
  Projet maven parent de tous les sous-projets constituant le starter Kafka Embedded

* #### kafka-embedded-core
  
  Module principal contenant l'implémentation et les tests du cluster embarqué KAFKA, ainsi que des  
  services RESt permettant l'administration globale du cluster (Topics, Groupes de Consommateurs,  
  Messages, etc...) documentés via Swagger 2.

* #### kafka-embedded-spring-boot-autoconfigure
  Module Spécifique Spring Boot, permettant de définir l'auto configurateur du module 
  Kafka

* #### kafka-embedded-spring-boot-starter
  Module spécifique Spring Boot permettant de définir le Starter qui sera utilisé par 
  les développeurs afin d'intégrer le module Kafka Embedded dans leurs applications.
  
* #### kafka-embedded-spring-boot-sample
  Module présentant un exemple d'utilisation du module Kafka Embedded dans une application 
  Spring Boot

## Composants principaux du projet

### La classe de configuration [net.leadware.kafka.embedded.properties.SimulatorProperties]

* Cette classe représente le contenu du fichier de propriétés de configuration du simulateur.
* Elle permet de valider le contenu du fichier après l'avoir associé à chacun de ses champs structurés grâce 
à la **JSR 303 Bean Validation** prise en charge par Spring Boot **@Validated**. 
* Cette classe sera utilisée par le Simulateur Kafka afin d'initialiser le serveur ZooKeeper et les brokers configurés

### La classe de simulation [net.leadware.kafka.embedded.KafkaSimulator]

* Cette classe représente le simulateur Kafka embarqué, contenant un serveur ZooKeeper 
  et plusieurs Brokers Kafka.
* Le simulateur propose une méthode d'initialisation **initialize** permettant de configurer et démarrer tous les composants du cluster Kafka
**ZooKeeper**, **Brokers**, **Client d'administration des brokers** et **Client d'administration ZooKeeper**
* Le simulateur offre aussi des méthodes permettant de créer/supprimer/lister des topics, lister les groupes de consommateurs connectés, 
lister les offsets des groupes de consommateurs.

### Les contrôleurs

Il s'agit des contrôleurs exposant les APIs REST d'exploitation du simulateur kafka

* **net.leadware.kafka.embedded.controller.TopicController** expose les opérations d'administration sur les topics
* **net.leadware.kafka.embedded.controller.ConsumerGroupController** expose les opérations de lecture d'informations sur les groupes de consommateurs
* **net.leadware.kafka.embedded.controller.ProducerController** expose les opérations de production de message Kafka pour des consommateurs Kafka

### La classe d'auto-configuration [net.leadware.kafka.embedded.autoconfigure.KafkaEmbeddedAutoConfiguration]

Cette classe permet de connstruire et configurer automatiquement l'ensemble des beans et de services nécessaires au bon fonctionnement du conteneur 
Kafka Embarqué de manière conditionnée.

* Condition de présence dans le classpath des classes ``net.leadware.kafka.embedded.KafkaSimulator`` 
  et ``net.leadware.kafka.embedded.utils.KafkaSimulatorFactory``

* Condition d'activation du simulateur via la propiété ``embedded.kafka.simulator.enabled`` qui doit être à true pour permettre la création des beans du simulateur Kafka

## Utilisation du starter Kafka Embedded

* Déclarer le starter comme dépendance MAVEN ou GRADLE
```
    <!-- MAVEN -->
    <dependencies>
        
        <!-- Kafka Embedded Starter dependency -->
        <dependency>
            <groupId>net.leadware</groupId>
            <artifactId>kafka-embedded-spring-boot-starter</artifactId>
            <version>1.0.0-RC1</version>
        </dependency>
        
    </dependencies>
    
    // GRADLE
    dependencies {
        compile "net.leadware:kafka-embedded-spring-boot-starter:1.0.0-RC1"
    }
```
* Mettre en place les propriétés de configuration du Kafka Embedded
```
    # Embedded Kafka Simulator activation status
    embedded.kafka.simulator.enabled = true
    
    # Embedded Kakfa Simulator control shutdown status
    embedded.kafka.simulator.java-temporary-directory = target
    
    # Embedded Kakfa Simulator control shutdown status
    embedded.kafka.simulator.controlled-shutdown = false
    
    # Embedded Kakfa Simulator enable delete topics capability
    embedded.kafka.simulator.enable-delete-topics = true
    
    # Embedded Kakfa Simulator partition count [Default KAFKA Properties : num.partitions = 1]
    embedded.kafka.simulator.partition-count = 1
    
    # Embedded Kakfa servers Network Threads used for receive and send messages [Default : num.network.threads=3]
    embedded.kafka.simulator.network-thread-count = 2
    
    # Embedded Kakfa servers I/O Threads used for process messages with disk I/O [Default : num.io.threads=8]
    embedded.kafka.simulator.io-thread-count = 2
    
    # Embedded Kakfa servers send buffer max size in byte [Default : socket.send.buffer.bytes=102400]
    embedded.kafka.simulator.send-buffer-size = 102400
    
    # Embedded Kakfa servers receive buffer max size in byte [Default : socket.receive.buffer.bytes=102400]
    embedded.kafka.simulator.receive-buffer-size = 102400
    
    # Embedded Kakfa servers receive buffer max size in byte [Default : max.socket.request.bytes=104857600]
    embedded.kafka.simulator.max-request-size = 104857600
    
    # Embedded Kakfa Simulator initial topics
    embedded.kafka.simulator.initial-topics = HCPA,DMES,IC
    
    # Embedded Kakfa Simulator broker instance Logs directory
    embedded.kafka.simulator.broker-configs[0].logs-directory = file:target/kafka/broker/brokerusecure/logs
    
    # Embedded Kakfa Simulator broker instance Logs directories
    embedded.kafka.simulator.broker-configs[0].logs-directories = file:target/kafka/broker/brokerusecure/logs
    
    # Embedded Kakfa Simulator broker instance Listener port
    embedded.kafka.simulator.broker-configs[0].listener.port = 9590
    
    # Embedded Kakfa Simulator broker instance Listener protocol name
    embedded.kafka.simulator.broker-configs[0].listener.protocol.name = PLAINTEXT
    
    # Embedded Kakfa Simulator broker instance Listener protocol scheme
    embedded.kafka.simulator.broker-configs[0].listener.protocol.scheme = PLAINTEXT
```
* Charger les proprétés dans la JVM (par exemple avec un @PropertySource)
* Vous pouvez désormais créer des producteurs et des consommateurs qui se connectent 
  en localhost sur les brokers Kafka
  
```
    # PROPRIÉTÉS DU CONSOMMATEUR
    
    # Consumer Bootstrap Server
    consumer.bootstrap-servers=localhost:9590
    
    # Consumer Client ID
    consumer.client-id=kafka-consumer
    
    # Consumer Group ID
    consumer.group-id=kafka-consumer-group
    
    # Consumer topic
    consumer.topic=HCPA
```

```
    // CLASSE REPRÉSENTANT LA DONNÉE QUI SERA ECHANGÉE
        
    /**
     * Classe représentant un utilisateur à sérialiser/désérialiser en KAFKA
     * @author <a href="mailto:jetune@leadware.net">Jean-Jacques ETUNE NGI (Java EE Technical Lead / Enterprise Architect)</a>
     * @since 1 avr. 2019 - 08:07:06
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public class User {
        
        /**
         * Prenom de l'utilisateur
         */
        private String lastName;
        
        /**
         * Nom de l'utilisateur
         */
        private String firstName;
        
        /**
         * No de connexion
         */
        private String login;
        
        /**
         * Mot de passe
         */
        private String password;
        
    }
```

```
    // CHARGEMENT DES PROPRIÉTÉ DU PRODUCTEUR
    
    /**
     * Bootstrap Servers du Producteur
     */
    @Value("${producer.bootstrap-servers}")
    private String producerBootstrapServers;
    
    /**
     * Client ID du Producteur
     */
    @Value("${producer.client-id}")
    private String producerClientId;
    
    /**
     * Topic d'echange du Producteur
     */
    @Value("${producer.topic}")
    private String topic; 
```

```
    // CHARGEMENT DES PROPRIÉTÉ DU CONSOMMATEUR
    
    /**
     * Bootstrap Servers du Consommateur
     */
    @Value("${consumer.bootstrap-servers}")
    private String consumerBootstrapServers;
    
    /**
     * Client ID du Consommateur
     */
    @Value("${consumer.client-id}")
    private String consumerClientId;
    
    /**
     * Group ID du Consommateur
     */
    @Value("${consumer.group-id}")
    private String consumerGroupId;
    
    /**
     * Topic d'echange du Consommateur
     */
    @Value("${consumer.topic}")
    private String consumerTopic = "HCPA";    
```

```
    
    /**
     * Template de production KAFKA
     */
    private KafkaTemplate<String, User> kafkaProducerTemplate;
    
    /**
     * Conteneur de Listener de Message KAFKA
     */
    private KafkaMessageListenerContainer<String, User> kafkaMessageListenerContainer;
    
    /**
     * File de stockage des messages reçues de KAFKA par le consommateur
     */
    final BlockingQueue<ConsumerRecord<String, User>> records = new LinkedBlockingQueue<>();
```

```
    // INITIALISATION DU PRODUCTEUR ET DU CONSOMMATEUR
    
    
    // Propriétés du producer
    Map<String, Object> producerProperties = new HashMap<>();
    
    // Positionnement des URLs de serveurs KAFKA
    producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerBootstrapServers);
    
    // Positionnement de l'ID du client
    producerProperties.put(ProducerConfig.CLIENT_ID_CONFIG, producerClientId);
    
    // Positionnement de la classe de secrialisation des clés
    producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    
    // Positionnement de la classe de secrialisation des données
    producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    
    // Fabrique de producteurs
    ProducerFactory<String, User> kafkaProducerFactory = new DefaultKafkaProducerFactory<String, User>(producerProperties);
    
    // Template de production Kafka
    kafkaProducerTemplate = new KafkaTemplate<>(kafkaProducerFactory);
    
    // Propriétés du producer
    Map<String, Object> consumerProperties = new HashMap<>();
    
    // Positionnement des URLs de serveurs KAFKA
    consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerBootstrapServers);
    
    // Positionnement de l'ID du client
    consumerProperties.put(ConsumerConfig.CLIENT_ID_CONFIG, consumerClientId);
    
    // Positionnement de l'ID du groupe
    consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
    
    // Positionnement de la classe de deserialisation des clés
    consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    
    // Positionnement de la classe de deserialisation des données
    consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    
    // Positionnement de des packages trustés pour la deserialisation
    consumerProperties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
    
    // Fabrique de consommateurs
    ConsumerFactory<String, User> kafkaConsumerFactory = new DefaultKafkaConsumerFactory<String, User>(consumerProperties);
    
    // Container Propertie
    ContainerProperties containerPorperties = new ContainerProperties(consumerTopic);
    
    // MessageListener Container
    kafkaMessageListenerContainer = new KafkaMessageListenerContainer<>(kafkaConsumerFactory, containerPorperties);
    
    // Add MessageListener
    kafkaMessageListenerContainer.setupMessageListener((MessageListener<String, User>) record -> {
        
        // Ajout dans la liste des recourds
        records.add(record);
    });
    
    // Demarrage du listener
    kafkaMessageListenerContainer.start();
    
```

```
    // Envoi du message 500 fois
    
    // Nombre de message a envoyer
    int nbMessage = 500;
    
    // Instantiation d'un user
    User user = new User("JEANN-JACQUES", "ETUNE NGI", "jetune", "jetune123");
    
    for(int count = 0; count < nbMessage ; count++) {
        
        // Envoi
        kafkaProducerTemplate.send(topic, user);
    }
    
```

* Allez à l'adresse du Swagger UI et visualiser la documentation des services offerts
