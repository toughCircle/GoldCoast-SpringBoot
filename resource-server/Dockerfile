# 공통 설정
FROM openjdk:17-jdk-slim
WORKDIR /app

# wait-for-it.sh 스크립트 복사 및 실행 권한 부여
COPY deploy/wait-for-it.sh /app/wait-for-it.sh
RUN chmod +x /app/wait-for-it.sh

# resource-server JAR 파일 복사
COPY resource-server-0.0.1-SNAPSHOT.jar app.jar

# ENTRYPOINT 수정: wait-for-it.sh 스크립트를 통해 데이터베이스 대기 후 JAR 파일 실행
ENTRYPOINT ["./wait-for-it.sh", "resource-db:3307", "--", "java", "-jar", "app.jar"]
