# Build Stage
FROM gradle:8.13-jdk21 AS build

# Set the working directory inside the container
WORKDIR /home/gradle/src

# Copy only the necessary files to the container to minimize cache invalidation
COPY . /home/gradle/src

# Build the project without the Gradle daemon to avoid cache issues
RUN gradle clean build --no-daemon

# Final Stage: Create a minimal Docker image with just the JAR file
FROM openjdk:21-jdk-slim

# Install curl for debugging purposes
RUN apt update && apt install -y curl

# Set the working directory inside the container
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar

# Expose the application's port
EXPOSE 8083

# Set JVM options to handle memory issues
ENV JAVA_OPTS="-Xms2g -Xmx4g"

# Command to run the application
CMD ["java", "-jar", "app.jar"]
