# timezones
Test technique fullstack pour Tahiti Numérique

## Etapes pour lancer le projet

Le backend est développé en Java 21 avec Spring Boot, Spring Data JPA et une base de données en mémoire H2.
La gestion des dépendances est assurée par maven

1 - Avoir Java 21 et Maven installés sur votre poste (`java -v` et `mvn -v` pour vérifier la version installée)
2 - Build le backend : se placer dans le dossier backend, puis lancer la commande `mvn clean install` et s'assurer que
le build est en succès
3 - Exécuter les tests en lançant la commande `mvn test` et s'assurer que tous les tests sont en succès
