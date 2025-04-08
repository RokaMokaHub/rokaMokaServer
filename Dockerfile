FROM openjdk:17-jdk-alpine
MAINTAINER @HokaMokaHub

COPY target/app-*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]