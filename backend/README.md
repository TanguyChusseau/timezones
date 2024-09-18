## Etapes pour lancer le projet

Le backend est développé en Java 21 avec Spring Boot, Spring Data JPA et une base de données en mémoire H2.
La gestion des dépendances est assurée par maven.

1. Avoir Java 21 installé sur votre poste et ajouté au `PATH` (lancer la commande `java -v` pour vérifier la version installée)

2. Avoir Maven installé sur votre poste et ajouté au `PATH` (lancer la commande `mvn -v` pour vérifier la version installée)

3. Build le projet : se placer dans le dossier *backend/*, puis lancer la commande `mvn clean install`,
et s'assurer que le build est en succès

4. Démarrer le serveur : Se placer dans le dossier *target/*, puis lancer le serveur avec la commande `java -jar timezones-0.0.1-SNAPSHOT.jar`

5. Une fois le serveur démarré, un Swagger UI est disponible à l'adresse suivante : http://localhost:8080/swagger-ui.html

6. Une fois le serveur démarré, une documentation OpenAPI est disponible à l'adresse suivante : http://localhost:8080/v3/api-docs

7. L'API est désormais testable (via des appels `curl`, un client Postman ou encore le Swagger)
