FROM openjdk:17-jdk-alpine
MAINTAINER @HokaMokaHub
COPY target/rokamokaServer-0.0.1-SNAPSHOT.jar rokamokaServer-0.0.1.jar
ENTRYPOINT ["java","-jar","/rokamokaServer-0.0.1.jar"]