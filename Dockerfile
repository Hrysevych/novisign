FROM openjdk:17-jdk-slim
COPY build/libs/novisign-*SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]