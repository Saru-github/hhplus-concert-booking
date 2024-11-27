FROM openjdk:17-jdk-slim as build

# Set working directory
WORKDIR /workspace/app

# Copy gradle files first for better caching
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Fix gradlew permissions and line endings
RUN chmod +x gradlew
RUN sed -i 's/\r$//' gradlew

# Copy source code
COPY src src

# Run build
RUN ./gradlew build -x test

FROM debian:bookworm-slim
RUN apt-get update && apt-get install -y openjdk-17-jdk libssl3
VOLUME /tmp
COPY --from=build /workspace/app/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

