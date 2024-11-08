FROM openjdk:17-jdk-slim
WORKDIR /app
COPY ./wait-for-it.sh /app/wait-for-it.sh
RUN chmod +x /app/wait-for-it.sh
COPY auth-server-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["./wait-for-it.sh", "auth-db:3306", "--", "java", "-jar", "app.jar"]
