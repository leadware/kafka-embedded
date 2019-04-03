# [Renovation][SGA][TECHNIQUE / feature-kafka-simulator] Mise en place d'un simulateur KAFKA embarqué pour les tests de consommation ACIV

Il s'agit ici de mettre en place un Simulateur, offrant les services nominaux du serveur KAFKA, et embarquable de manière sélective dans une application
JAVA/Spring Boot.

## Présentation du Simulateur

* Le simulateur a été monté comme une starter Spring Boot configurable via un objet de propriété alimenté par des propriétés système spécifique chargées
à partir d'un fichier de configuration ou d'une atre source, par l'application utilisatrice.

* Le simulateur offre un sous ensemble assez complet de configuration des brokers KAFKA autour d'un Orchestrateur de cluster Zookeeper démarré en localhost 
sur le premier port libre de la machine hôte.

* Le simulateur expose des APIs REST permettant d'effectuer des opération d'administration, de gestion des topics, ainsi que de production de messages.

* L'ensemble des APIs REST proposés par le simulateur sont documentés via SWAGGER

---

## Présentation des composants peincipaux du simulateur

### La classe de configuration [fr.grouperatp.ratp.sga.kafka.simulator.properties.SimulatorProperties]

* Cette classe représente le contenu du fichier de propriétés de configuration du simulateur.
* Elle permet de valider le contenu du fichier après l'avoir associé à chacun de ses champs structurés grâce 
à la **JSR 303 Bean Validation** prise en charge par Spring Boot **@Validated**. 
* Cette classe sera utilisée par le Simulateur Kafka afin d'initialiser le serveur ZooKeeper et les brokers configurés

### La classe de simulation [fr.grouperatp.ratp.sga.kafka.simulator.KafkaSimulator]

* Cette classe représente le simulateur Kafka embarqué.
* Le simulateur propose une méthode d'initialisation **initialize** permettant de configurer et démarrer tous les composants du cluster Kafka
**ZooKeeper**, **Brokers**, **Client d'administration des brokers** et **Client d'administration ZooKeeper**
* Le simulateur offre aussi des méthodes permettant de créer/supprimer/lister des topics, lister les groupes de consommateurs connectés, 
lister les offsets des groupes de consommateurs.

### Les contrôleurs

Il s'agit des contrôleurs exposant les APIs REST d'exploitation du simulateur kafka

* **fr.grouperatp.ratp.sga.kafka.simulator.controller.TopicController** expose les opérations d'administration sur les topics
* **fr.grouperatp.ratp.sga.kafka.simulator.controller.ConsumerGroupController** expose les opérations de lecture d'informations sur les groupes de consommateurs
* **fr.grouperatp.ratp.sga.kafka.simulator.controller.ProducerController** expose les opérations de production de message Kafka pour des consommateurs comme le serveur d'affichage (en recette)

### La classe d'auto-configuration [fr.grouperatp.ratp.sga.kafka.simulator.config.KafkaSimulatorAutoConfiguration]

Cette classe permet de connstruire l'ensemble des beans et de services nécessaires au bon fonctionnement du simulateur de manière conditionnée.

* Condition de présence des classes de simulation dans le classpath : Celle condition a été mise en place en vue du détachement de la classe d'auto-configuration dans un projet dédié comme le prévoit les bonnes pratiques Spring Boot en matière de création de starters et d'auto-configurateurs. Le détachementse fera plus tard, une fois le starter stabilisé.

* Condition d'activation du simulateur via la propiété ``embedded.kafka.simulator.enabled`` qui doit être à true pour permettre la création des beans du simulateur Kafka

---

## Installation Configuration & Exécution en standalone

* Cloner le projet ``git clone git@ulice01.info.ratp:SGA/kafka-simulator.git``
* Allez dans le répertoire du projet ``cd kafka-simulator``
* Construisez le projet ``mvn clean install``
* Démarrer l'application Spring Boot ``java -ja target/kafka-simulator-0.0.1-SNAPSHOT.jar``
* Allez à l'adresse du Swagger UI ``http://localhost:8080/swagger-ui.html``

---

## Intégration dans le SGA

* La suite au prochain numéro...... :-)