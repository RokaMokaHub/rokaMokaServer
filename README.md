# RokaMoka Server

RokaMoka is an educational project that provides a RESTful API for managing users, art exhibitions, and collections.
It's built with Spring Boot and uses PostgreSQL for data storage.

## Project Overview

RokaMoka Server is a backend application that allows users to:

- Register and authenticate (both normal and anonymous users)
- Manage user profiles and permissions
- Browse art exhibitions and artworks
- Create personal collections (MokaDex)

## Prerequisites

Before you begin, ensure you have the following installed:

- Java 17 or higher
- Maven
- Docker and Docker Compose
- Git

## Installation and Setup

1. Clone the repository (ssh only):
   ```
   git clone git@github.com:your-username/rokaMokaServer.git
   ```

2. Configure environment variables for the database:
   Create a `.env` file in the project root with the following, example, variables:
   ```
    DB_USER=rokamoka # nome do usuário do banco
    DB_PASSWORD=teste123 # senha do banco
    DB_NAME=RokaMokaDb # nome do banco de dados
    DB_PORT=5432 # porta padrão do postgres
    DB_PORT_EXPOSE=5432 # porta que será exposta no container
   
    SERVER_PORT=8080 # porta padrão da aplicação
    SERVER_PORT_EXPOSE=8081 # porta que será exposta no container
   ```

## Running the Application

1. Start the database (if not already running):
   ```
   docker compose up -d
   ```

2. Run the Spring Boot application:
   ```
   ./mvnw spring-boot:run
   ```

3. Access the application at:
   ```
   http://localhost:8080
   ```

## API Documentation

The API documentation is available through Swagger UI: http://localhost:8080/swagger-ui/index.html

- Note: remember to run the API.

## Project Structure

- `src/main/java/br/edu/ufpel/rokamoka/` - Main source code
    - `controller/` - REST controllers
    - `service/` - Business logic
    - `repository/` - Data access layer
    - `core/` - Domain models
    - `dto/` - Data transfer objects
    - `security/` - Authentication and authorization
    - `exceptions/` - Custom exceptions
    - `config/` - Application configuration

## Technologies Used

- **Spring Boot** - Application framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Data access
- **PostgreSQL** - Database
- **JWT** - Token-based authentication
- **Swagger/OpenAPI** - API documentation
- **Lombok** - Boilerplate code reduction
- **Docker** - Containerization

## Development

### Timezone

The application uses the "America / Sao_Paulo" timezone by default.
