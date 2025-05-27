# Stage 1: Build Java app with Maven and Java 17
FROM maven:3.9-eclipse-temurin-17 AS builder

# Set working directory
WORKDIR /app

# Copy pom.xml and src folder from captcha directory (where Dockerfile lives)
COPY pom.xml .
COPY src ./src

# Download dependencies before build (cache optimization)
RUN mvn dependency:go-offline

# Build the project (skip tests for faster build)
RUN mvn clean package -DskipTests

# Stage 2: Run the built JAR with Java 17 runtime
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose port your app listens on (adjust if needed)
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
