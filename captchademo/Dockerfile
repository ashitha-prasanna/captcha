
# Stage 1: Build Java app from GitHub using Maven
FROM maven:3.9-eclipse-temurin-17 as builder

# Set working directory
WORKDIR /app

# Clone your GitHub repo
RUN git clone https://github.com/ashitha-prasanna/captcha.git .

# Build the project (you can also skip tests)
RUN mvn clean package -DskipTests

# Stage 2: Run the JAR with Java 17
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy the JAR from build stage
COPY --from=builder /app/target/*.jar app.jar

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
