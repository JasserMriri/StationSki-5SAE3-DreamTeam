# Use OpenJDK 17 as the base image
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the target directory into the container
COPY target/gestion-station-ski-1.0.jar /app/mon-projet-spring-boot.jar

# Set the command to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/mon-projet-spring-boot.jar"]
