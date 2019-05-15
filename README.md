[![Leadware](http://leadware.net/medias/images/leadware/lw_logo_complet_sans_surlignement_2016_10_26_155X30.png)](http://www.leadware.net/) 
![](markdown/version.svg) ![](markdown/snapshot.svg) ![](markdown/build-status.svg)

![](http://www.pragma-innovation.fr/wp-content/uploads/2018/02/kafka-logo.png)

# Rappels Apache KAFKA
Apache KAFKA est une plateforme de streaming distribué de données de type Publication/Souscription (pub/sub), capable de gérer des très gros volumes de données par unité de temps.

## Origines de KAFKA

Le projet KAFKA a été initié par la société LinkedIn en 2009, afin de mettre en place une plateforme de messagerie permettant de :
*  Centraliser la production et la consommation des flux de données de l'entreprise
*  Supporter nativement la montée en charge (scalabilité horizontale)
*  Permettre la production et la consommation des données évenementielles en très haut débit
*  Garantir la livraison des données
*  Assurer la persistance des données avant consommation

En 2012, le projetKAFKA a rejoint l'incubateur de la fondation Apache et a subit de grandes évolution. Aujourd'hui, Apache KAFKA est une plateforme de streaming temps réel proposant
*  Des mécanismes de chargement/diffusion de données depuis ou vers la plateforme grâce à KAFKA CONNECT
*  Des mécanismes de Transformation de données au fil de l'eau (temps réel) grâce à KAFKA Stram
*  Des mécanismes de requêtage de données grâce à KAFKA SQL (KSQL).

## Positionnements de KAFKA

*  Apache KAFKA s'inscrit globalement dans la lignée des outils support aux architectures pilotées par les évènements (EDA). De ce point de vue, il se rapproche des implémentations des spécifications Java Messaging Service (comme Apache ActiveMQ) et Advance Messaging Queue Protocol (comme RabbtMQ).
*  Dans une architecture d'entreprise Apache KAFKA se positionne principalement comme médiateur de messagerie asynchrone entre des composants autonommes
*  Dans une architecture d'intégration de données, grâce à ses fonctions de Streaming évènementiel, KAFKA se positionnera comme une pièce centrale et incontournable allant plus loin qu'une simple plateforme de messagerie, en proposant des mécanismes de traitement de données en temps réels, permettant ainsi de le rendre comparable à un ETL ou un mini ESB
*  Dans une architecture d'intégration de service, Apache KAFKA se positionnera comme un des multiples médias de transport pour les connecteurs d'E/S d'une plateforme d'intégration de service (ESB)

![](https://fr.confluent.io/wp-content/uploads/chart-kafka-infrastructure@2x.png)

## Quelques mots clés de KAFKA

*  **Broker**
Il s'agit d'une des instances (un des noeuds) actives du serveur Apache KAFKA

*  **ZooKeeper**
C'est un système de gestion des noeud redondants exploité par KAFKA pour la coordinaton de ses différentes instances

*  **Producer**
Il s'agit du composants permettant ls production d'évènements à destination de un ou plusieurs consommateurs

*  **Consumer**
Il s'agit du composants permettant la consommation des évènements produits et diffusés par le biais de KAFKA

*  **Publish/Subscribe**
Il s'agit du mode de communication sur lequel repose la plateforme KAFKA

*  **Topics**
Il s'agit d'une zone de stockage des évènements diffusés par le(s) producteur(s) et qui doivent être récupérés par un ou plusieurs consommateur(s)

*  **Key**
Il s'agit d'une clé de répartition des messages dans les partitions, permettant ainsi la publication et la consommation de message sen parallèle.

*  **Partition**
Il s'agit d'une séquence ordonnée et immuable de messages ne permettant que le rajout en fin de queue

# Le projet KAFKA Embedded

Le principal objectif du projet KAFKA Embedded est de fournir aux développeurs, architectes, DevOps, une version embarquée du serveur Apache KAFKA, permettant de fournir les fonctionnalités principales d'un cluster KAFKA au sein d'une application et ainsi de faciliter les tests de composants et processus d'intégration de données dans un environnement d'intégration et de déploiement continue.

## Usages principaux

*  Implémentation de tests unitaire des composants d'intégration de données (venant d'un cluster KAFKA)
*  Implémentation de tests end-to-end des processus d'intégration de données (venant d'un cluster KAFKA)
*  Implémentation de simulateur de diffusion de données à la demande

## Versions des composants KAFKA utilisés dans le projet

| Composant | Version |
| ------ | ------ |
| Serveur KAFKA | 2.11 |
| Serveur ZooKeeper | 3.4.13 | 
| Client Kafka | 2.0.1 |
| Client ZooKeeper | 0.10 |

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

* NB : Seule la propriété ``embedded.kafka.simulator.enabled`` est nécessaire pour 
  le demarrage du simulateur. En effet, l'autoconfiguration du simulateur vous propose 
  des propriétés par défaut

```

# Embedded Kakfa Simulator control shutdown status
embedded.kafka.simulator.java-temporary-directory = <Valeur de java.io.tmpdir>

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
embedded.kafka.simulator.initial-topics = <AUCUN TOPIC PAR DEFAUT>

# Embedded Kakfa Simulator broker instance Logs directory
embedded.kafka.simulator.broker-configs[0].logs-directory = <Valeur de java.io.tmpdir>/kafka_simulator

# Embedded Kakfa Simulator broker instance Logs directories
embedded.kafka.simulator.broker-configs[0].logs-directories = <Valeur de java.io.tmpdir>/kafka_simulator

# Embedded Kakfa Simulator broker instance Listener port
embedded.kafka.simulator.broker-configs[0].listener.port = 9090

# Embedded Kakfa Simulator broker instance Listener protocol name
embedded.kafka.simulator.broker-configs[0].listener.protocol.name = PLAINTEXT

# Embedded Kakfa Simulator broker instance Listener protocol scheme
embedded.kafka.simulator.broker-configs[0].listener.protocol.scheme = PLAINTEXT
```

## Utilisation du starter Kafka Embedded

* Déclarer le starter comme dépendance MAVEN ou GRADLE
```
    <!-- MAVEN -->
    <dependencies>
        
        <!-- Kafka Embedded Starter dependency -->
        <dependency>
            <groupId>net.leadware</groupId>
            <artifactId>kafka-embedded-spring-boot-starter</artifactId>
            <version>1.0.0-RC8</version>
        </dependency>
        
    </dependencies>
    
    // GRADLE
    dependencies {
        compile "net.leadware:kafka-embedded-spring-boot-starter:1.0.0-RC8"
    }
```
* Référencer les fichiers de configuration du simulateur kafka et du consommateur dans 
  le fichier de configuration principal de l'application
  
```
# Application context
server.servlet.context-path=/kafka-embedded

# Server port
server.port=8080

# Chemin vers le fichier de configuration du simulateur KAFKA
net.leadware.kafka.sample.config.simulator=classpath:kafka-simulator.properties

# Chemin vers le fichier de configuration du consommateur KAFKA
net.leadware.kafka.sample.config.consumer=classpath:kafka-consumer.properties
```

* Mettre en place les propriétés de configuration du Simulateur Kafka

** Mode NON SÉCURISÉ

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
** Mode SÉCURISÉ
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
    
    # Embedded Kakfa Simulator broker SSL Protocol [Default : ssl.protocol = TLS]
    embedded.kafka.simulator.ssl-protocol = TLS
    
    # Embedded Kakfa Simulator broker SSL Client Authentication [Default : ssl.client.auth = none]
    embedded.kafka.simulator.ssl-client-authentication = NONE
    
    # Embedded Kakfa Simulator broker keystore keymanagerAlgorithm
    embedded.kafka.simulator.keystore-config.keymanager-algorithm = SunX509
    
    # Embedded Kakfa Simulator broker keystore location
    embedded.kafka.simulator.keystore-config.location=file:target/classes/kafka-broker.jks
    
    # Embedded Kakfa Simulator broker keystore password
    embedded.kafka.simulator.keystore-config.password = ratp123
    
    # Embedded Kakfa Simulator broker key password
    embedded.kafka.simulator.keystore-config.key-password = ratp123
    
    # Embedded Kakfa Simulator broker keystore type
    embedded.kafka.simulator.keystore-config.type = JKS
    
    # Embedded Kakfa Simulator broker truststore keymanagerAlgorithm
    embedded.kafka.simulator.truststore-config.keymanager-algorithm = SunX509
    
    # Embedded Kakfa Simulator broker truststore location
    embedded.kafka.simulator.truststore-config.location=file:target/classes/kafka-broker.jks
    
    # Embedded Kakfa Simulator broker truststore password
    embedded.kafka.simulator.truststore-config.password = ratp123
    
    # Embedded Kakfa Simulator broker truststore type
    embedded.kafka.simulator.truststore-config.type = JKS
    
    # Embedded Kakfa Simulator initial topics
    embedded.kafka.simulator.initial-topics = HCPA,DMES,IC
    
    # Embedded Kakfa Simulator broker instance Logs directory
    embedded.kafka.simulator.broker-configs[0].logs-directory=file:target/kafka/broker/broker0/logssecure
    
    # Embedded Kakfa Simulator broker instance Logs directories
    embedded.kafka.simulator.broker-configs[0].logs-directories=file:target/kafka/broker/broker0/logssecure
    
    # Embedded Kakfa Simulator broker instance Listener port
    embedded.kafka.simulator.broker-configs[0].listener.port = 9690
    
    # Embedded Kakfa Simulator broker instance Listener protocol name
    embedded.kafka.simulator.broker-configs[0].listener.protocol.name = SSL
    
    # Embedded Kakfa Simulator broker instance Listener protocol scheme
    embedded.kafka.simulator.broker-configs[0].listener.protocol.scheme = SSL

```

* Mettre en place la configuration d'un consommateur

** Mode NON SÉCURISÉ
  
```
    # Consumer Bootstrap Server
    net.leadware.kafka.sample.config.consumer.bootstrap-servers=PLAINTEXT://localhost:9090
    
    # Consumer Client ID
    net.leadware.kafka.sample.config.consumer.client-id=sample-consumer
    
    # Consumer Group ID
    net.leadware.kafka.sample.config.consumer.client-group-id=sample-consumer-group
    
    # Consumer Metadata Max Age
    net.leadware.kafka.sample.config.consumer.metadata-max-age=5000
    
    # Consumer topic pattern
    net.leadware.kafka.sample.config.consumer.topic-pattern=.*

```

** Mode SÉCURISÉ
  
```
    # Consumer Bootstrap Server
    net.leadware.kafka.sample.config.consumer.bootstrap-servers=SSL://localhost:9690
    
    # Consumer Client ID
    net.leadware.kafka.sample.config.consumer.client-id=sample-consumer
    
    # Consumer Group ID
    net.leadware.kafka.sample.config.consumer.client-group-id=sample-consumer-group
    
    # Consumer Metadata Max Age
    net.leadware.kafka.sample.config.consumer.metadata-max-age=5000
    
    # Consumer topic pattern
    net.leadware.kafka.sample.config.consumer.topic-pattern=.*
    
    # Consumer Security protocol
    net.leadware.kafka.sample.config.consumer.security-protocol=SSL
    
    # Consumer Truststore location
    net.leadware.kafka.sample.config.consumer.truststore-location=file:target/classes/kafka-broker.jks
    
    # Consumer Truststore password
    net.leadware.kafka.sample.config.consumer.truststore-password=ratp123
    
    # Consumer Keystore locaton
    net.leadware.kafka.sample.config.consumer.keystore-location=file:target/classes/kafka-broker.jks
    
    # Consumer Keystore password
    net.leadware.kafka.sample.config.consumer.keystore-password=ratp123
    
    # Consumer Key password
    net.leadware.kafka.sample.config.consumer.key-password=ratp123

```

** NB : Un producteur interne, disponible via une API Rest est déjà fournit par le simulateur (voir doc swagger http://your-app/your-context/swagger-ui.html), mais vous pouvez 
configurer d'autres producteurs en vous inspirant de la configuration du consomateur

* Exemple de chargement des configurations du consommateur

```
    @Component
    @PropertySource(name = "consumerConfiguration", value = {"${net.leadware.kafka.sample.config.consumer}"})
    @ConfigurationProperties(prefix="net.leadware.kafka.sample.config.consumer")
    @Getter
    @Setter
    public class KafkaSimulatorSampleConsumerProperties {
        
        /**
         * Liste des URL des Brokers Kafka
         */
        private String bootstrapServers;
        
        /**
         * Identifiant du consommateur
         */
        private String clientId;
        
        /**
         * Identifiant du groupe du consommateur
         */
        private String clientGroupId;
        
        /**
         * Durée maximale de recharge des metadonnees du consommateur
         */
        private String metadataMaxAge;
        
        /**
         * Pattern des topics d'abonnement automatique
         */
        private String topicPattern;
        
        /**
         * Protocole de securite du consomateur
         */
        private String securityProtocol;
        
        /**
         * Chemin vers le fichier trustore
         */
        private String truststoreLocation;
        
        /**
         * Mot de passe du fichier truststore
         */
        private String truststorePassword;
        
        /**
         * Chemin vers le fichier keystore
         */
        private String keystoreLocation;
        
        /**
         * Mot de passe du magasin de clés
         */
        private String keystorePassword;
        
        /**
         * Mot de passe de la clé
         */
        private String keyPassword;
    }
```
* Exemple de configuration d'un listener kafka

```
    @Configuration
    @EnableSwagger2
    @Slf4j
    public class KafkaSimulatorSampleConsumerConfiguration {
        
        /**
         * Configuration du consommateur
         */
        @Autowired
        private KafkaSimulatorSampleConsumerProperties consumerConfig;
        
        /**
         * Méthode permettant de construire la liste d'enregistrement consommées par le consommateur KAFKA
         * @return  Liste d'enregistrement
         */
        @Bean
        @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
        public List<ConsumedRecord> consumerRecords() {
            
            // On retourne la liste
            return new ArrayList<>();
        }
        
        /**
         * Méthode de construction du Listener Kafka
         * @param consumerRecords Liste d'enregistrements consommés
         * @return  Listener Kafka
         */
        @Bean(initMethod = "start", destroyMethod = "stop")
        public KafkaMessageListenerContainer<String, String> kafkaListenerContainerFactory(List<ConsumedRecord> consumerRecords) {
    
            // Propriétés du producer
            Map<String, Object> consumerProperties = new HashMap<>();
            
            // Positionnement des URLs de serveurs KAFKA
            consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerConfig.getBootstrapServers());
            
            // Positionnement de l'ID du client
            consumerProperties.put(ConsumerConfig.CLIENT_ID_CONFIG, consumerConfig.getClientId());
            
            // Positionnement de l'ID du groupe
            consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, consumerConfig.getClientGroupId());
            
            // Positionnement de la classe de deserialisation des clés
            consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            
            // Positionnement de la classe de deserialisation des données
            consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            
            // Positionnement de des packages trustés pour la deserialisation
            consumerProperties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
            
            // Positionnement du delai de recharche des metadonnees de topics (recharger la liste de topics toutes les 5)
            consumerProperties.put(ConsumerConfig.METADATA_MAX_AGE_CONFIG, consumerConfig.getMetadataMaxAge());
    
            // Positionnement de la localisation du keystore Consommateur
            consumerProperties.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, SimulatorUtils.getResolvedPath(consumerConfig.getKeystoreLocation()));
            
            // Positionnement de la clé du keystore Consommateur
            consumerProperties.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, consumerConfig.getKeystorePassword());
            
            // Positionnement du mot de passe de la clé Consommateur
            consumerProperties.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, consumerConfig.getKeyPassword());
            
            // Positionnement de la localisation du truststore Consommateur
            consumerProperties.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, SimulatorUtils.getResolvedPath(consumerConfig.getTruststoreLocation()));
            
            // Positionnement de la clé du truststore Consommateur
            consumerProperties.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, consumerConfig.getTruststorePassword());
            
            // Positionnement du type de cle
            consumerProperties.put(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, "JKS");
            
            consumerProperties.put("ssl.enabled.protocols", "TLSv1.2,TLSv1.1,TLSv1");
            
            // Positionnement du protocol de securite
            consumerProperties.put("security.protocol", consumerConfig.getSecurityProtocol());
            
            // Fabrique de consommateurs
            ConsumerFactory<String, String> kafkaConsumerFactory = new DefaultKafkaConsumerFactory<>(consumerProperties);
            
            // Container Propertie
            ContainerProperties containerPorperties = new ContainerProperties(Pattern.compile(consumerConfig.getTopicPattern()));
            
            // MessageListener Container
            KafkaMessageListenerContainer<String, String> kafkaMessageListenerContainer = new KafkaMessageListenerContainer<>(kafkaConsumerFactory, containerPorperties);
            
            // Add MessageListener
            kafkaMessageListenerContainer.setupMessageListener((MessageListener<String, String>) record -> {
                
                // Ajout de l'enregistrement dans la liste
                consumerRecords.add(new ConsumedRecord(record.topic(), record.partition(), 
                                                       record.offset(), record.timestamp(), 
                                                       record.timestampType(), 
                                                       record.serializedKeySize(), 
                                                       record.serializedValueSize(), 
                                                       record.key(), record.value()));
                
                // Ajout dans la liste des recourds
                System.out.println("==========================================");
                System.out.println(record);
                System.out.println("==========================================");
            });
            
            // On retourne le listener
            return kafkaMessageListenerContainer;
        }
    
        
    }

```

* Allez à l'adresse du Swagger UI et visualiser la documentation des services offerts
* Pour plus d'informations, veuillez regarder les codes sources de l'application **kafka-embedded-spring-boot-sample**
