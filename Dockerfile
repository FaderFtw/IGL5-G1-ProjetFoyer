FROM openjdk:17-jdk-alpine

COPY target/tpFoyer-17-*.jar /app.jar

RUN ls -la /app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]