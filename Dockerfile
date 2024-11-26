# Start with a lightweight base image with Java runtime
FROM amazoncorretto:21-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the Spring Boot application JAR file into the container
COPY target/mss-content-service-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your application runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]



## Stage 1: Build the application
#FROM maven:3.9.9-amazoncorretto-21-alpine AS content-service
#WORKDIR /app
#COPY pom.xml .
#COPY src ./src
#RUN mvn clean package -DskipTests
#
## Stage 2: Create a minimal JRE image
#FROM openjdk:17-jdk-alpine
#WORKDIR /app
#COPY --from=content-service /app/target/mss-content-service-0.0.1-SNAPSHOT.jar content-service
#
#EXPOSE 8080
#
## Specify the default command to run on startup
#CMD ["java", "-jar", "content-service"]