FROM openjdk:17-jdk-slim
WORKDIR /app
RUN yum -y install curl && \
    curl -o /app/wait-for-it.sh https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh && \
    chmod +x /app/wait-for-it.sh
COPY resource-server-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["./wait-for-it.sh", "resource-db:3307", "--", "java", "-jar", "app.jar"]
