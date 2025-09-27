# Step 1: Base image (Java 17)
FROM eclipse-temurin:17-jdk-alpine

# Step 2: Working directory inside container
WORKDIR /app

# Step 3: Copy your built jar from target folder
COPY target/*.jar app.jar

# Step 4: Expose port (Spring Boot default port 8080)
EXPOSE 8080

# Step 5: Command to run your jar
ENTRYPOINT ["java", "-jar", "app.jar"]
