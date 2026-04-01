# Use official OpenJDK 17 image
FROM openjdk:17-jdk-slim

# Set working directory inside container
WORKDIR /app

# Copy your JAR file into the container
COPY target/your-app-name.jar app.jar

# Expose port (change if your app uses different port)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
