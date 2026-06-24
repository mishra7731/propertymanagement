Ongoing Project > Working on doing  full extent of backend for now, and will be giving a frontend structure soon. 
# Property Management Service

A backend service for managing a **commercial real estate (CRE) portfolio** —
properties, the loans secured against them, tenants, leases, and payment records.

Built as a REST API with Java and Spring Boot over PostgreSQL, with schema-as-code
migrations, stateless JWT security, and containerised local development. This is the
full-stack-Java half of a two-project portfolio; a React frontend is planned (see
[Roadmap](#roadmap)).

> **Status:** the **Property** domain is implemented end-to-end and verified against
> a live PostgreSQL database. The full schema for all five domain tables is in place
> via Flyway. The remaining domain slices (Loan, Tenant, Lease, Payment), the test
> suite/CI, and the frontend are in progress.


## Tech stack

| Layer        | Technology |
|--------------|------------|
| Language     | Java 21 |
| Framework    | Spring Boot 4.1 (Web MVC, Data JPA, Validation, Security, Actuator) |
| Database     | PostgreSQL 16 |
| Migrations   | Flyway |
| Security     | OAuth2 Resource Server (JWT), stateless |
| Persistence  | Spring Data JPA / Hibernate 7 |
| Build / run  | Maven (wrapper), Docker, Docker Compose |
| Frontend     | React (planned)


## Architecture

The service follows a standard layered design. Each request flows through clearly
separated responsibilities:
HTTP request

→ Controller        (REST endpoints, request/response handling)

→ Service           (business logic, transactions, filtering)

→ Repository        (data access via Spring Data JPA)

→ PostgreSQL

← Mapper            (converts entities ↔ DTOs)

← JSON response


The database schema is owned entirely by Flyway migrations (`ddl-auto: validate`),
so Hibernate never alters the schema — it only validates that the entities match.

### Domain model
property 1──* loan

property 1──* lease ──1 tenant

lease    1── payment_record

loan     1──* payment_record


A `payment_record` belongs to **either** a lease (rent) **or** a loan (debt service),
enforced by a database check constraint. All monetary values use `NUMERIC(14,2)`,
and every foreign key and searched column is indexed.

## Running locally

Requires Java 21 and Docker.

Start PostgreSQL:

```bash
docker compose up -d db
```

Run the application (from your IDE, or via the Maven wrapper):

```bash
./mvnw spring-boot:run
```

On startup, Flyway applies the schema and seed data automatically. The API is then
available at `http://localhost:8080`, and `GET /actuator/health` returns `UP`.

## API — Property

| Method | Path | Description |
|--------|------|-------------|
| GET    | `/api/properties` | Search/list with optional filters: `?q=`, `?type=`, `?city=`, plus `?page=`, `?size=`, `?sort=` |
| GET    | `/api/properties/{id}` | Fetch a single property |
| POST   | `/api/properties` | Create a property |
| PUT    | `/api/properties/{id}` | Update a property |
| DELETE | `/api/properties/{id}` | Delete a property |

Example:

```bash
curl "http://localhost:8080/api/properties?type=office&city=Salt%20Lake%20City"
```

Responses are paginated and wrapped in a stable envelope:

```json
{
  "content": [ ... ],
  "page": 0,
  "size": 20,
  "totalElements": 3,
  "totalPages": 1,
  "last": true
}
```

### Security

Read endpoints and `/actuator/health` are public; write operations require a valid
JWT. The issuer is configured via `JWT_ISSUER_URI` and left unset for local
development, so the app boots without an identity provider attached.

### Error handling

Errors return RFC 7807-style problem details — a missing resource yields a `404`
with a descriptive message, and invalid request bodies yield a `400` listing the
offending fields.


## Project structure
src/main/java/com/propertyproject/propertyservice/

├── PropertyserviceApplication.java

├── config/        SecurityConfig

├── common/        exception handling, PageResponse

└── property/      entity, repository, service, controller, dto/   ← reference slice

src/main/resources/

├── application.yml

└── db/migration/  V1__init_schema.sql, V2__seed_data.sql


## Roadmap

- [x] Database schema for all five domain tables (Flyway migrations + seed data)
- [x] Property slice: entity, repository, DTOs, mapper, service, controller
- [x] CRUD + filtered search + pagination, verified end-to-end against PostgreSQL
- [x] Stateless JWT security configuration
- [x] Global error handling (problem details)
- [x] Containerised PostgreSQL via Docker Compose
- [ ] Automated test suite (unit + Testcontainers integration) and CI
- [ ] Loan slice
- [ ] Tenant + Lease slices
- [ ] Payment slice + portfolio summary endpoint
- [ ] React frontend (search, dashboards, CRUD workflows)
- [ ] OpenAPI / Swagger UI documentation