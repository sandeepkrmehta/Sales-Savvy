# Step 1: Use Maven image to build the jar
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy all files from your backend folder to container
COPY . .

# Build the application (this will create target/com.salesSavvy-0.0.1-SNAPSHOT.jar)
RUN mvn clean package -DskipTests

# Step 2: Use lightweight JDK image for running the app
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy jar file from previous build stage
COPY --from=build /app/target/com.salesSavvy-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
