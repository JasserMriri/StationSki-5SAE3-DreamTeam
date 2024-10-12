# Utilise une image de base contenant OpenJDK 17
FROM openjdk:17-jdk-alpine

# Crée un répertoire pour l'application dans le conteneur
WORKDIR /app

# Copie le fichier JAR généré par votre projet Spring Boot dans le conteneur
COPY target/mon-projet-spring-boot.jar /app/mon-projet-spring-boot.jar

# Expose le port sur lequel l'application Spring Boot s'exécute
EXPOSE 8080

# Définit la commande à exécuter lorsque le conteneur démarre
ENTRYPOINT ["java", "-jar", "/app/mon-projet-spring-boot.jar"]
