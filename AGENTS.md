# Repository Guidelines

## Project Structure & Module Organization
This is a Spring Boot 3.4 backend on Java 17. Main code lives in `src/main/java/br/edu/ufpel/rokamoka/` and is organized by layer:
`controller/`, `service/`, `repository/`, `core/`, `dto/`, `security/`, `config/`, and `exceptions/`.
Supporting code is under `utils/`, `filter/`, `context/`, `component/`, and `wrapper/`.
Tests live in `src/test/java/br/edu/ufpel/rokamoka/`, with unit tests grouped by package and integration tests in `integration/`.
Environment-specific config is in `src/main/resources/` and test config in `src/test/resources/`.

## Build, Test, and Development Commands
- `./mvnw spring-boot:run` starts the API locally.
- `docker compose up -d` starts the PostgreSQL and other required services used by the app/tests.
- `./mvnw test` runs the full test suite.
- `./mvnw clean package` builds the application JAR and runs tests.
- `./mvnw -DskipTests package` builds only when you need a fast artifact.

## Coding Style & Naming Conventions
Use Java 17 conventions: 4-space indentation, `PascalCase` for classes, `camelCase` for methods/fields, and lower-case package names.
Keep package names aligned with responsibility, for example `service/exhibition/ExhibitionService.java`.
DTOs should stay in the matching `dto/<domain>/input` or `dto/<domain>/output` package.
There is no repo-local formatter or lint config, so follow the existing style and keep imports clean.

## Testing Guidelines
The project uses JUnit 5, Spring Boot Test, Mockito, Instancio, and Testcontainers. Name tests with the `*Test` suffix; integration tests use `*IT` or live under `integration/`.
Prefer focused service/controller tests with mocked dependencies, and use Testcontainers for database or RabbitMQ behavior.
Run `./mvnw test` before opening a PR; Docker must be available for container-based tests.

## Commit & Pull Request Guidelines
Recent commits use a mix of conventional prefixes and short descriptive messages, such as `fix(email): ...`, `refactor: ...`, and `test(...): ...`.
Keep commit messages concise and action-oriented. For PRs, include a short summary, the main change set, and any setup notes for reviewers.
Link related issues when available and add screenshots or sample requests only when the API behavior changes.

## Security & Configuration Tips
Do not commit secrets or local environment overrides. Use `.env` for database and port settings, and keep `src/main/resources/app.key` and `app.pub` aligned with the current environment.
When changing auth, mail, or broker config, verify the matching files in `application-*.yml` and the Docker setup together.
