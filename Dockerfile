# Utiliser une image Gradle avec JDK 21 si disponible, sinon utiliser une image générique et installer JDK 21
# Assurez-vous de vérifier si une image officielle gradle-jdk21 est disponible à ce moment-là
FROM gradle:jdk21 AS build

# Définir l'environnement de travail
WORKDIR /home/gradle/src

# Copier les fichiers du projet Gradle dans le conteneur
COPY --chown=gradle:gradle . /home/gradle/src

# Exécuter la construction avec Gradle
RUN gradle build --no-daemon

# Démarrer une nouvelle étape avec une image JDK 21 pour l'exécution
FROM openjdk:21-jdk-slim

# Définir le répertoire de travail pour l'application
WORKDIR /app

# Copier le jar construit à partir de l'étape de build
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar

# Exposer le port utilisé par l'application
EXPOSE 8080

# Définir la commande pour démarrer l'application Java
CMD ["java", "-jar", "app.jar"]

