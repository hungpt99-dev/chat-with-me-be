# ============================================
# Multi-stage Docker build for chat-with-me-be
# ============================================

# Stage 1: Build
FROM eclipse-temurin:25-jdk AS builder
WORKDIR /workspace

# Copy project files (context is now the repo root)
COPY . .

# Build with Gradle configurations
# - settings.gradle will switch to published artifact if fast-framework is missing
# - build.gradle will use GITHUB_TOKEN to fetch it from GitHub Packages
RUN chmod +x gradlew && ./gradlew bootJar -x test --no-daemon

# Stage 2: Runtime
FROM eclipse-temurin:25-jre
WORKDIR /app

# Copy the built JAR (path relative to project root in workspace)
COPY --from=builder /workspace/build/libs/*.jar app.jar

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
