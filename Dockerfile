# Etapa 1: compila a aplicação
FROM maven:3.9-eclipse-temurin-17-alpine AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: imagem final com apenas o jar
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar rokamoka.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "rokamoka.jar"]
