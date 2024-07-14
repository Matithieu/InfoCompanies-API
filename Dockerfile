FROM gradle:jdk21 AS build

ARG CONFIG_PATH
WORKDIR /home/gradle/src

COPY InfoCompanies-API /home/gradle/src
COPY ${CONFIG_PATH} /home/gradle/src/config/certs

RUN gradle build --no-daemon

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
COPY --from=build /home/gradle/src/config/certs /app/config

# Import the SSL certificate into the Java keystore
RUN keytool -importcert -file /app/config/server.crt -alias myalias -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit -noprompt

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]