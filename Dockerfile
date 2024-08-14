
# Use an official Maven image as the base image
FROM maven:3.9.8-eclipse-temurin-17 AS build
# Set the working directory in the container
WORKDIR /
# Copy the pom.xml and the project files to the container
COPY pom.xml .
COPY src ./src
# Build the application using Maven
RUN mvn clean package -DskipTests
# Use an official OpenJDK image as the base image
FROM eclipse-temurin:17-jre-alpine

# Set the working directory in the container
WORKDIR /
# Copy the built JAR file from the previous stage to the container
COPY .env ./
COPY --from=build /target/jangkau-0.0.1-SNAPSHOT.jar ./
EXPOSE 8081
# Set the command to run the application
CMD ["java", "-jar", "jangkau-0.0.1-SNAPSHOT.jar"]