FROM openjdk:17-jdk-alpine
MAINTAINER @HokaMokaHub
# Define a variável de ambiente APP_VERSION
ARG APP_VERSION
ENV APP_VERSION=${APP_VERSION}

COPY target/rokamokaServer-${APP_VERSION}.jar rokaMokaServer.jar
ENTRYPOINT ["java","-jar","/rokaMokaServer.jar"]