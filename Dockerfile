ARG VERSION
FROM openjdk:17-jdk-alpine
ADD target/tpFoyer-17-${VERSION}.jar tpFoyer-17-${VERSION}.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/tpFoyer-17-${VERSION}.jar"]