# Book Management API

## Project Name & Description

**Book** is a RESTful Spring Boot application for managing a personal book inventory. The project demonstrates a full-stack backend implementation with JWT authentication, role-based access control, and user-scoped CRUD operations for books. It serves as a practical example of modern Java enterprise patterns including security, caching with Redis, API documentation with OpenAPI/Swagger, and a simple static frontend.

The application allows users to register, authenticate via JWT, and perform book management operations (create, read, update, delete) where each user sees only their own books. Role-based demo endpoints illustrate fine-grained authorization (ADD, GET, UPDATE, DELETE roles).

---

## Tech Stack

| Category | Technology |
|----------|------------|
| **Language** | Java 17 |
| **Framework** | Spring Boot 3.2.5 |
| **Build Tool** | Maven |
| **Web** | Spring Web (REST) |
| **Validation** | Bean Validation (Jakarta) |
| **Data Access** | Spring Data JPA |
| **Database** | H2 (in-memory, default) / MySQL (connector available) |
| **Security** | Spring Security + JWT (JJWT 0.11.5) |
| **Caching** | Redis (Spring Data Redis) |
| **API Documentation** | SpringDoc OpenAPI 3 (Swagger UI) |
| **Utilities** | Lombok |

### Key Dependencies

- **spring-boot-starter-web** – REST API
- **spring-boot-starter-security** – Authentication & Authorization
- **spring-boot-starter-data-jpa** – JPA/Hibernate
- **spring-boot-starter-data-redis** – Redis cache
- **spring-boot-starter-validation** – Validation
- **jjwt-api**, **jjwt-impl**, **jjwt-jackson** – JWT handling
- **springdoc-openapi-starter-webmvc-ui** – Swagger UI
- **h2** – In-memory database (runtime)
- **mysql-connector-j** – MySQL driver (runtime)
- **lombok** – Boilerplate reduction

---

## Features

- **User Registration** – Register new users with default `ROLE_GET`
- **JWT Authentication** – Login returns access token (1h) and refresh token (7 days)
- **Token Refresh** – Exchange refresh token for a new access token
- **Book CRUD** – Create, read, update, delete books (user-scoped)
- **Role-Based Access Control** – Demo endpoints for `ROLE_ADD`, `ROLE_GET`, `ROLE_UPDATE`, `ROLE_DELETE`
- **Request Validation** – Book constraints: name/author (not blank), price (1–1000), quantity (≥1)
- **Global Exception Handling** – Centralized handling for `BookNotFoundException`, `MyValidationException`
- **API Documentation** – Interactive Swagger UI with Bearer JWT support
- **Redis Cache** – Cache backend configured for future use
- **Static Frontend** – Served at `/frontend/` (simple HTML/JS/CSS)
- **H2 Console** – Embedded database console for development
- **Logging** – Logback with console and rolling file appenders
- **Docker** – `docker-compose` for Redis + application

---

## API Endpoints

### Authentication (`/apis`)

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/apis/login` | Public | Login with username/password; returns access token and refresh token |
| POST | `/apis/refresh-token` | Public | Exchange refresh token for new access token |
| GET | `/apis/add` | ROLE_ADD | Demo endpoint |
| GET | `/apis/get` | ROLE_GET | Demo endpoint |
| GET | `/apis/update` | ROLE_UPDATE | Demo endpoint |
| GET | `/apis/delete` | ROLE_DELETE | Demo endpoint |

### User Registration

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/reg-user` | Public | Register new user (default ROLE_GET) |

### Books (`/api/books`)

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/api/books` | Required | List all books for current user |
| GET | `/api/books/{id}` | Required | Get book by id (user-owned only) |
| POST | `/api/books` | Required | Create book |
| PUT | `/api/books/{id}` | Required | Update book |
| DELETE | `/api/books/{id}` | Required | Delete book |

### Other URLs

| URL | Description |
|-----|-------------|
| `/` | Redirects to `/frontend/index.html` |
| `/frontend/**` | Static frontend assets |
| `/h2-console` | H2 database console |
| `/swagger-ui.html` | Swagger API documentation |
| `/v3/api-docs/**` | OpenAPI JSON schema |

---

## How to Run

### Prerequisites

- **Java 17**
- **Maven 3.6+**
- **Redis** (for cache; app starts without it but cache will fail; optional for basic testing)

### Option 1: Local Run (Maven)

1. **Clone the repository**

   ```bash
   git clone https://github.com/Tural20000/book
   cd book/backend
   ```

2. **Start Redis** (if using cache)

   ```bash
   redis-server
   ```

   Or with Docker:
   ```bash
   docker run -d -p 6379:6379 redis
   ```

3. **Run the application**

   ```bash
   ./mvnw spring-boot:run
   ```

   On Windows:
   ```cmd
   mvnw.cmd spring-boot:run
   ```

4. **Access the application**

   - API base: http://localhost:8020
   - Frontend: http://localhost:8020/frontend/
   - Swagger UI: http://localhost:8020/swagger-ui.html
   - H2 Console: http://localhost:8020/h2-console  
     - JDBC URL: `jdbc:h2:mem:studentdb`  
     - Username: `root`  
     - Password: *(empty)*

### Option 2: Docker Compose

1. **Build and run**

   ```bash
   cd backend
   docker-compose up -d
   ```

   This starts Redis and the Spring Boot app. Ensure the JAR is built first:

   ```bash
   ./mvnw clean package -DskipTests
   ```

2. **Access**: Same URLs as above (http://localhost:8020).

### Quick Test

1. **Register** (or use seeded user `tural` / password from `data.sql`)
   ```bash
   curl -X POST http://localhost:8020/reg-user \
     -H "Content-Type: application/json" \
     -d '{"username":"test","password":"test123"}'
   ```

2. **Login**
   ```bash
   curl -X POST http://localhost:8020/apis/login \
     -H "Content-Type: application/json" \
     -d '{"username":"tural","password":"<your-password>"}'
   ```

3. **Use access token** in `Authorization: Bearer <token>` for `/api/books` and other protected endpoints.

### Seed Data

`data.sql` seeds:

- Roles: `ROLE_GET`, `ROLE_ADD`, `ROLE_UPDATE`, `ROLE_DELETE`
- User `tural` with all roles
- Sample books for user `tural`

---

## Project Structure

```
book/
├── backend/
│   ├── pom.xml
│   ├── Dockerfile
│   ├── docker-compose.yml
│   └── src/main/
│       ├── java/com/abdullayevtural/book/
│       │   ├── BookApplication.java
│       │   ├── config/          # Security, Redis, OpenAPI, WebMvc
│       │   ├── controller/      # AuthController, BookController, UserRegistrationController
│       │   ├── dto/             # AuthRequest, BookRequestDto, etc.
│       │   ├── entity/          # BookEntity, UserEntity, RoleEntity
│       │   ├── exception/       # BookNotFoundException, MyValidationException
│       │   ├── filters/         # JwtAuthenticationFilter
│       │   ├── handler/         # MyGlobalHandler (global exception handling)
│       │   ├── repository/     # JPA repositories
│       │   ├── service/         # BookService, UserDetailsServiceImpl
│       │   └── utils/           # JwtUtil, RefreshTokenUtil
│       └── resources/
│           ├── application.properties
│           ├── logback-spring.xml
│           ├── data.sql
│           └── static/frontend/
└── frontend/                    # Source for static assets
```

---

## Configuration

| Property | Default | Description |
|----------|---------|-------------|
| `server.port` | 8020 | Server port |
| `spring.datasource.url` | `jdbc:h2:mem:studentdb` | H2 in-memory database |
| `spring.data.redis.host` | localhost | Redis host |
| `spring.data.redis.port` | 6379 | Redis port |
| `spring.cache.type` | redis | Cache provider |

For Docker: `SPRING_DATA_REDIS_HOST=redis` and `SPRING_DATA_REDIS_PORT=6379` are set in `docker-compose` for the app service.

---

## License

This project is for educational and demonstration purposes.

## Deployment & Live Demo

The application is fully containerized and deployed on a **Contabo VPS** (Ubuntu).

- **Environment:** Docker & Docker Compose
- **Status:** Running on a remote server
- **Access:** The API is accessible via the server's public IP on port `8020`.
- **Infrastructure:** Managed with SSH and deployed using a custom Docker configuration for Spring Boot and Redis.
