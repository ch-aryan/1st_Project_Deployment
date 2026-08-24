# ============================================================
# Stage 1: Build the Spring Boot Application
# ============================================================
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

# Copy Maven wrapper & pom.xml first to leverage Docker layer caching
COPY pom.xml mvnw mvnw.cmd ./
COPY .mvn .mvn
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline -B || true

# Copy source code and package application (skipping tests for fast container build)
COPY src ./src
RUN ./mvnw clean package -DskipTests

# ============================================================
# Stage 2: Minimal, Secure JRE Runtime Image
# ============================================================
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Create a non-root system user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copy executable jar from builder stage
COPY --from=builder /app/target/springboot-banking-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
