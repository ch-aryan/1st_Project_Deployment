# ============================================================
# Stage 1: Build the Spring Boot Application
# Using pre-installed Maven image (avoids downloading maven wrapper at build time)
# ============================================================
FROM maven:3.9-eclipse-temurin-17-alpine AS builder
WORKDIR /app

# Copy pom.xml and settings
COPY pom.xml ./
COPY .mvn/settings.xml /root/.m2/settings.xml
COPY .mvn/settings.xml ./.mvn/settings.xml

# Copy source code and package application directly with pre-installed mvn
COPY src ./src
RUN mvn clean package -DskipTests -s /root/.m2/settings.xml -B

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
