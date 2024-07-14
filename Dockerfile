FROM maven:2.7.14-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/jangkau-0.0.1-SNAPSHOT.jar jangkau.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","jangkau.jar"]