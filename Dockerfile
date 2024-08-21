# Build Stage
FROM gradle:jdk21 AS build

# Set the working directory inside the container
WORKDIR /home/gradle/src

# Copy only the necessary files to the container to minimize cache invalidation
COPY InfoCompanies-API /home/gradle/src

# Build the project without the Gradle daemon to avoid cache issues
RUN gradle clean build --no-daemon

# Final Stage: Create a minimal Docker image with just the JAR file
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar

# Optionally, if you have SSL certificates, uncomment the following lines
# and handle them securely
#COPY --from=build /home/gradle/src/config/certs /app/config
#RUN keytool -importcert -file /app/config/cert1.pem -alias myalias -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit -noprompt

# Expose the application's port
EXPOSE 8080

# Set JVM options to handle memory issues
ENV JAVA_OPTS="-Xms1g -Xmx4g"

# Command to run the application
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]
