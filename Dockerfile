# Build Stage
FROM gradle:8.13-jdk21 AS build
WORKDIR /home/gradle/src
COPY . .
RUN gradle clean build --no-daemon

# Runtime Stage (JRE only)
FROM eclipse-temurin:21-jre
WORKDIR /app

RUN apt-get update \
  && apt-get install -y --no-install-recommends curl \
  && rm -rf /var/lib/apt/lists/*

# Copy the built jar from the build stage
COPY --from=build /home/gradle/src/build/libs/*.jar /app/app.jar

EXPOSE 8083

# JVM options (optional)
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75"

# Use a shell form to expand JAVA_OPTS
CMD ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
