# Dockerfile for Test Automation Framework
FROM maven:3.9.5-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy built JAR
COPY --from=build /app/target/*.jar app.jar

# Install dependencies for Selenium (if running tests inside container)
RUN apk add --no-cache \
    chromium \
    chromium-chromedriver \
    firefox \
    ttf-freefont \
    font-noto-emoji

# Set environment variables
ENV JAVA_OPTS="-Xmx512m"

# Run tests
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
