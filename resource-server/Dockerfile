FROM openjdk:17-jdk-slim
WORKDIR /app
COPY resource-server-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["./wait-for-it.sh", "resource-db:3307", "--", "java", "-jar", "app.jar"]
