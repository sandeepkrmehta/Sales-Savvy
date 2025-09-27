# Step 1: Use official OpenJDK image
FROM openjdk:17-jdk-slim

# Step 2: Set working directory inside the container
WORKDIR /app

# Step 3: Copy the jar file from target folder to container
COPY target/com.salesSavvy-0.0.1-SNAPSHOT.jar app.jar

# Step 4: Expose the port on which Spring Boot runs
EXPOSE 8080

# Step 5: Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
