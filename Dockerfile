# Utiliser une image Gradle avec JDK 21 si disponible, sinon utiliser une image générique et installer JDK 21
# Assurez-vous de vérifier si une image officielle gradle-jdk21 est disponible à ce moment-là
FROM gradle:jdk21 AS dependency_cache

# Définir l'environnement de travail
WORKDIR /home/gradle/src

# Copier uniquement les fichiers de configuration de Gradle pour résoudre les dépendances
COPY --chown=gradle:gradle build.gradle settings.gradle /home/gradle/src/

# Copier le répertoire contenant les scripts Gradle pour résoudre les dépendances
COPY --chown=gradle:gradle gradle /home/gradle/src/gradle

# Exécuter une tâche de résolution des dépendances pour mettre en cache les dépendances
RUN gradle --no-daemon dependencies

# Définir l'étape de build
FROM gradle:jdk21 AS build

# Définir l'environnement de travail
WORKDIR /home/gradle/src

# Copier le reste des fichiers du projet Gradle dans le conteneur
COPY --chown=gradle:gradle . .

# Copier les dépendances précédemment mises en cache
COPY --from=dependency_cache /home/gradle/.gradle /home/gradle/.gradle

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


######

# Utiliser une image Gradle avec JDK 21 si disponible, sinon utiliser une image générique et installer JDK 21
# Assurez-vous de vérifier si une image officielle gradle-jdk21 est disponible à ce moment-là
#FROM gradle:jdk21 AS build

# Définir l'environnement de travail
#WORKDIR /home/gradle/src

# Copier les fichiers du projet Gradle dans le conteneur
#COPY --chown=gradle:gradle . /home/gradle/src

# Exécuter la construction avec Gradle
#RUN gradle build --no-daemon

# Démarrer une nouvelle étape avec une image JDK 21 pour l'exécution
#FROM openjdk:21-jdk-slim

# Définir le répertoire de travail pour l'application
#WORKDIR /app

# Copier le jar construit à partir de l'étape de build
#COPY --from=build /home/gradle/src/build/libs/*.jar app.jar

# Exposer le port utilisé par l'application
#EXPOSE 8080

# Définir la commande pour démarrer l'application Java
#CMD ["java", "-jar", "app.jar"]

