FROM openjdk:17-jdk-alpine

ARG VERSION
ENV VERSION=${VERSION}

RUN echo "Version is: ${VERSION}"

COPY target/tpFoyer-17-${VERSION}.jar /app.jar

RUN ls -la /app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]