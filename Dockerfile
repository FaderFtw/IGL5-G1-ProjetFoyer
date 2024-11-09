FROM openjdk:17-jdk-alpine

ARG VERSION
ENV VERSION=${VERSION}

COPY target/tpFoyer-17-${VERSION}.jar /app.jar


EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]