FROM openjdk:17-jdk-slim
WORKDIR /app
# 프로젝트 루트의 deploy 디렉토리에서 auth-server JAR 파일 복사
COPY auth-server-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
