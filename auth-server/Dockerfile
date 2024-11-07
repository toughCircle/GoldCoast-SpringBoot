FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/auth-server.jar auth-server.jar
ENTRYPOINT ["java", "-jar", "auth-server.jar"]
