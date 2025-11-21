# ===========================
# STAGE 1: Build Spring Boot App
# ===========================
FROM eclipse-temurin:17 AS builder

WORKDIR /app

# Copy all project files (including mvnw)
COPY . .

# Make Maven wrapper executable
RUN chmod +x mvnw

# Build the Spring Boot JAR
RUN ./mvnw -ntp -DskipTests package


# ===========================
# STAGE 2: Run Application
# ===========================
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy built JAR from previous stage
COPY --from=builder /app/target/*.jar app.jar

# Cloud Run uses the PORT env var
ENV PORT=8080
EXPOSE 8080

# Start the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
