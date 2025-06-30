# Stage 1: Build the application using Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set working directory inside the container
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy all source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application with a lightweight JRE
FROM eclipse-temurin:17-jre-alpine

# Set working directory
WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/blogify-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080 (or change if your app uses another port)
EXPOSE 8080

# Command to run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
