# ============================================================
# Stage 1: Build the Spring Boot Application
# ============================================================
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

# Copy Maven wrapper, settings & pom.xml
COPY pom.xml mvnw mvnw.cmd ./
COPY .mvn .mvn
RUN chmod +x ./mvnw

# Copy source code and package application using Google Maven Mirror
COPY src ./src
RUN ./mvnw clean package -DskipTests -s .mvn/settings.xml -B

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

ENV PORT=8080
EXPOSE 8080 10000

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
