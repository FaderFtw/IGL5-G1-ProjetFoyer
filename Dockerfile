ARG VERSION
FROM openjdk:17-jdk-alpine
ARG VERSION
ADD target/tpFoyer-17-$VERSION.jar tpFoyer-17-$VERSION.jar
ENV VERSION=$VERSION
EXPOSE 8080
ENTRYPOINT java -jar /tpFoyer-17-${VERSION}.jar