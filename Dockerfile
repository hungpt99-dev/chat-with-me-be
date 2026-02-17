# ============================================
# Multi-stage Docker build for chat-with-me-be
# ============================================

# Stage 1: Build
FROM eclipse-temurin:25-jdk AS builder

WORKDIR /workspace

# Copy fast-framework (composite build dependency)
COPY fast-framework/ fast-framework/

# Copy backend project
COPY chat-with-me-be/ chat-with-me-be/

WORKDIR /workspace/chat-with-me-be

# Build the application (skip tests in Docker build)
RUN chmod +x gradlew && ./gradlew bootJar -x test --no-daemon

# Stage 2: Runtime
FROM eclipse-temurin:25-jre

WORKDIR /app

# Copy the built JAR
COPY --from=builder /workspace/chat-with-me-be/build/libs/*.jar app.jar

# Non-root user for security
RUN groupadd -r appuser && useradd -r -g appuser appuser
USER appuser

EXPOSE 8081

# JVM tuning for containers
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
