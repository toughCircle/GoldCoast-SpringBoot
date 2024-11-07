FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/resource-server.jar resource-server.jar
ENTRYPOINT ["java", "-jar", "resource-server.jar"]
