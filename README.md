[![Leadware](http://leadware.net/medias/images/leadware/lw_logo_complet_sans_surlignement_2016_10_26_155X30.png)](http://www.leadware.net/) 
![](markdown/version.svg) ![](markdown/snapshot.svg) ![](markdown/build-status.svg)

![](http://www.pragma-innovation.fr/wp-content/uploads/2018/02/kafka-logo.png)

# Le projet KAFKA Embedded

Le principal objectif du projet KAFKA Embedded est de mettre à disposition des 
développeurs, architectes, technicals leads, DevOps, un outil fournissant toutes 
les fonctionnalités principales d'un cluster KAFKA, embarqué à la demande dans 
une application JAVA/Spring.

Cette Intégration : 

*  Facilitera les tests de composants et processus d'intégration de données et en 
environnement d'intégration et de déploiement continue.
*  **Permettra de remplacer les Mock de serveur KAFKA, par un Cluster KAFKA réel et allégé**
*  Apportera une plus grande précision aux tests d'intégration de vos composants 
et processus d'intégration de données
*  **Fournira un moeyn de simuler des données à la demande en environnement de recette et de préproduction**

## Usages principaux du projet KAFKA Embedded

*  Implémentation de tests unitaire des composants d'intégration de données (venant d'un cluster KAFKA)
*  Implémentation de tests end-to-end des processus d'intégration de données (venant d'un cluster KAFKA)
*  Implémentation de simulateur de diffusion de données à la demande
*  Implémentation de tests précis des processus de streaming d'évènements

# Rappels Apache KAFKA

Apache KAFKA est une plateforme de streaming distribué de données fonctionnement
en mode Publication/Souscription (pub/sub), capable de gérer des très gros volumes 
de données par unité de temps.  [**Plus d'informations sur le produit KAFKA**](https://kafka.apache.org/documentation/).

## Origines de KAFKA

Le projet KAFKA a été initié par la société LinkedIn en 2009, afin de mettre en place une plateforme de messagerie permettant de :
*  Centraliser la production et la consommation des flux de données de l'entreprise
*  Supporter nativement la montée en charge (scalabilité horizontale)
*  Permettre la production et la consommation des données évenementielles en très haut débit
*  Garantir la livraison des données
*  Assurer la persistance des données avant consommation

En 2012, le projet KAFKA a rejoint l'incubateur de la fondation Apache et a subit de grandes évolution. Aujourd'hui, Apache KAFKA est une plateforme de streaming temps réel proposant
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

*  **Cluster**
Il s'agit d'un ensemble d'instances de serveur KAFKA fonctionnant ensemble dans le but principal de supporter la charge et la disponibilité.

*  **ZooKeeper**
C'est un système de gestion des noeuds redondants exploité par KAFKA pour la coordinaton de ses différentes instances dans un cluster

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


# Retour sur le projet KAFKA Embedded

## Présentation des modules du projet

Le projet Kafka est livré sous la forme d'un Starter Spring Boot (kafka-embedded-spring-boot-starter) découpé en 5 sous modules.

* #### kafka-embedded
  Projet maven parent, parapluie de tous les sous-projets constituant le starter Kafka Embedded

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

## Présentation des Fonctionnalités

Le projet KAFKA Embedded fournit un sous-ensemble assez complet des fonctionnalités d'un Cluster Apache KAFKA déployé en mode Production-Ready.

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

## Versions des composants KAFKA utilisés dans le projet

| Composant | Version |
| ------ | ------ |
| Serveur KAFKA | 2.11 |
| Serveur ZooKeeper | 3.4.13 | 
| Client Kafka | 2.0.1 |
| Client ZooKeeper | 0.10 |

## Versions des outils et Frameworks utilisés

| Outils de base | Version | Usage |
| ------ | ------ | ------ |
| GIT | 2.17.1 | Gestion des sources |
| Oracle Java | 1.8.x | Langage de développement |
| Apache Maven | 3.x | Outils de construction |
| Spring Framework | 5.x | Framework de développement Managé |
| Spring Boot | 2.1.3.RELEASE | Framework de configuration/packaging de projet |
| Swagger | 2.9.2 | Framework de documentation d'API |
| Leadware Bean Validator | 1.0.0-RC6 | Bibliothèque d'extention de la JSR 303 Bean Validation |

## Classes principales du projet

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

Cette classe permet de connstruire et configurer automatiquement l'ensemble des beans et de services nécessaires au bon fonctionnement du conteneur Kafka Embarqué de manière conditionnée.

* Condition de présence dans le classpath des classes 
```
net.leadware.kafka.embedded.KafkaSimulator
net.leadware.kafka.embedded.utils.KafkaSimulatorFactory
springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration
io.swagger.annotations.ApiModelProperty.AccessMode
org.springframework.kafka.core.KafkaTemplate
kafka.server.KafkaServer
kafka.zk.EmbeddedZookeeper
```
* Condition d'activation du simulateur via la propiété ``embedded.kafka.simulator.enabled`` qui doit être à `` true`` pour permettre la création et l'enregistrement des beans du simulateur Kafka.

* ***Notez que, une fois les conditions de présence de classes requises satisfaites, seule la propriété ``embedded.kafka.simulator.enabled = true`` est nécessaire pour le demarrage du simulateur. En effet, l'autoconfiguration du simulateur vous propose des valeurs de propriétés par défaut***

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

* **Déclarer le starter, ainsi que les bibliothèques dépendantes comme dépendance MAVEN ou GRADLE**

```
    <!-- MAVEN -->
    <dependencies>
        
        <!-- Kafka Embedded Starter dependency -->
        <dependency>
            <groupId>net.leadware</groupId>
            <artifactId>kafka-embedded-spring-boot-starter</artifactId>
            <version>1.0.0-RC11</version>
        </dependency>
        
        <!-- Spring Kafka dependency -->
        <dependency>
            <groupId>org.springframework.kafka</groupId>
            <artifactId>spring-kafka</artifactId>
            <version>2.2.4.RELEASE</version>
        </dependency>
        
        <!-- Spring Kafka Tests dependency -->
        <dependency>
            <groupId>org.springframework.kafka</groupId>
            <artifactId>spring-kafka-test</artifactId>
            <version>2.2.4.RELEASE</version>
        </dependency>
        
        <!-- Spring Fox Bean Validator dependency -->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-bean-validators</artifactId>
            <version>2.9.2</version>
        </dependency>
        
        <!-- Spring Fox Swagger UI dependency -->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
            <version>2.9.2</version>
        </dependency>
        
        <!-- Spring Fox Swagger dependency -->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger</artifactId>
            <version>2.9.2</version>
        </dependency>
        
    </dependencies>
    
```

```
// GRADLE
dependencies {
    compile("net.leadware:kafka-embedded-spring-boot-starter:1.0.1-RC11")
    compile("org.springframework.kafka:spring-kafka:2.2.4.RELEASE")
    compile("org.springframework.kafka:spring-kafka-test:2.2.4.RELEASE")
    compile("io.springfox:springfox-bean-validators:2.9.2")
    compile("io.springfox:springfox-swagger-ui:2.9.2")
    compile("io.springfox:springfox-swagger2:2.9.2")
}
```

* **Mettre en place le fichier de configuration du Cluster KAFKA**

1.  CONFIGURATION SIMPLIFIEE (Uniquement la propriété d'activation)

```
# Embedded Kafka Simulator activation status
embedded.kafka.simulator.enabled = true

```
2. CONFIGURATION NORMALE EN MODE PLAINTEXT
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

3. CONFIGURATION EN MODE SSL

```
# Embedded Kafka Simulator activation status
embedded.kafka.simulator.enabled = true

# Embedded Kakfa Simulator control shutdown status
embedded.kafka.simulator.java-temporary-directory = build

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
embedded.kafka.simulator.keystore-config.location=file:build/resources/test/tests-kafka-embedded-kck/kafka-broker.jks

# Embedded Kakfa Simulator broker keystore password
embedded.kafka.simulator.keystore-config.password = ratp123

# Embedded Kakfa Simulator broker key password
embedded.kafka.simulator.keystore-config.key-password = ratp123

# Embedded Kakfa Simulator broker keystore type
embedded.kafka.simulator.keystore-config.type = JKS

# Embedded Kakfa Simulator broker truststore keymanagerAlgorithm
embedded.kafka.simulator.truststore-config.keymanager-algorithm = SunX509

# Embedded Kakfa Simulator broker truststore location
embedded.kafka.simulator.truststore-config.location=file:build/resources/test/tests-kafka-embedded-kck/kafka-broker.jks

# Embedded Kakfa Simulator broker truststore password
embedded.kafka.simulator.truststore-config.password = ratp123

# Embedded Kakfa Simulator broker truststore type
embedded.kafka.simulator.truststore-config.type = JKS

# Embedded Kakfa Simulator initial topics
embedded.kafka.simulator.initial-topics = HCPA,DMES,IC

# Embedded Kakfa Simulator broker instance Logs directory
embedded.kafka.simulator.broker-configs[0].logs-directory=file:build/kafka/broker/broker0/logskckint

# Embedded Kakfa Simulator broker instance Logs directories
embedded.kafka.simulator.broker-configs[0].logs-directories=file:build/kafka/broker/broker0/logskckint

# Embedded Kakfa Simulator broker instance Listener port
embedded.kafka.simulator.broker-configs[0].listener.port = 9650

# Embedded Kakfa Simulator broker instance Listener protocol name
embedded.kafka.simulator.broker-configs[0].listener.protocol.name = SSL

# Embedded Kakfa Simulator broker instance Listener protocol scheme
embedded.kafka.simulator.broker-configs[0].listener.protocol.scheme = SSL
```

* **Charger Les propriétés de configuration du Clueter Kafka**

Vous disposez de plusieurs options de chargement des propriétés.

1.  Dans le cas de Spring Boot, Les propriétés seront automatiquement chargés su elle se trouves dans le fichier de configuration par defaut "application.properties" ou "application.yaml"
2.  Vous pouvez aussi opter pour une approche de spécialisation des fichiers de configuration et déplacer ces propriétés dans un fichier dédié et le charger de manièere spécifique via l'annotation ``@PropertySource(name = "kafkaEmbeddedProperties", value = {"/chemin/vers/votre/fichier/de/proprietes"})``

A noter qu'un producteur interne, disponible Out-Of-The-Box, via une API Rest est déjà fournit par le simulateur (voir doc swagger http://votre-app/votre-contexte/swagger-ui.html), mais vous pouvez 
configurer d'autres producteurs en vous inspirant de la configuration du consomateur

* **Mettre en place et charger les propriétés d'un producteur**

Un exemple de propriétés pour un producteur basé sur l'utilitaire KafkaTemplate de spring :

```
# Producer Bootstrap Server
producer.bootstrap-servers=localhost:9190

# Producer Client ID
producer.client-id=kafka-producer

# Producer topic
producer.topic=HCPA
```

Un exemple de chargement des propriétés du producteur

```
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
private String topic = "HCPA";

```

* **Mettre en place et charger les propriétés d'un consomateur**

* Un exemple de propriétés pour un producteur basé sur l'utilitaire KafkaTemplate de spring :

```
# Consumer Bootstrap Server
consumer.bootstrap-servers=localhost:9190

# Consumer Client ID
consumer.client-id=kafka-consumer

# Consumer Group ID
consumer.group-id=kafka-consumer-group

# Consumer topic
consumer.topic=HCPA
```

Un exemple de chargement des propriétés du producteur

```
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

* Allez à l'adresse du Swagger UI et visualiser la documentation des services offerts

## Pour plus d'informations, veuillez regarder les codes sources de l'application [kafka-embedded-spring-boot-sample] ainsi que les classes de tests contenues dans les codes sources du module [kafka-embedded-core]
