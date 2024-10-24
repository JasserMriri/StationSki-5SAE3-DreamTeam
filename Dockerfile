# Utiliser une image de base avec JDK 8 pour construire l'application
FROM maven:3.8.5-openjdk-8 AS build

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier le fichier pom.xml et le répertoire src
COPY pom.xml .
COPY src ./src

# Construire l'application
RUN mvn clean package -DskipTests

# Utiliser une image de base avec JRE 8 pour exécuter l'application
FROM openjdk:8-jre-slim

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier le jar construit à partir de l'étape de build
COPY --from=build /app/target/gestion-station-ski-1.0.jar gestion-station-ski.jar

# Exposer le port sur lequel l'application s'exécute
EXPOSE 8080

# Commande pour exécuter l'application
ENTRYPOINT ["java", "-jar", "gestion-station-ski.jar"]
