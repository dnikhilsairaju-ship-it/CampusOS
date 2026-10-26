# CampusOS

CampusOS is a campus operations MVP with a React frontend and Spring Boot REST API.

## Requirements
- Java 21
- Maven 3.9+
- Node.js 20+ and npm

## Run backend
```bash
cd backend
mvn spring-boot:run
```
API: http://localhost:8080/api/v1  
H2 console (demo profile): http://localhost:8080/h2-console  
JDBC URL: `jdbc:h2:mem:campusos`, username `sa`, blank password.

## Run frontend
In another terminal:
```bash
cd frontend
npm install
npm run dev
```
Open the URL Vite prints (normally http://localhost:5173).

## Demo accounts
This MVP uses a demo login selector, not production-grade credential authentication:
- Student: `student@campusos.demo`
- Faculty: `faculty@campusos.demo`
- Campus Admin: `admin@campusos.demo`
- Service Staff: `staff@campusos.demo`

Choose a role and continue. Do not deploy the demo login as-is. Before production, implement verified credentials, password hashing, token/session lifecycle, CSRF strategy, rate limits, and stronger authorization tests.

## Implemented in this MVP
- Role-aware dashboard and demo role selection
- Ticket creation/listing/status updates
- Potential incident grouping by category + location
- Resource listing and reservation conflict checks
- Campus knowledge search with source titles
- Opportunity listing and bookmarking
- Basic aggregated analytics
- H2 persistence for the running application; MySQL configuration profile
- OpenAPI endpoint and basic integration tests

## Production configuration
The default profile uses H2. To use MySQL, set environment variables and activate `mysql`:
```bash
export SPRING_PROFILES_ACTIVE=mysql
export DB_URL='jdbc:mysql://localhost:3306/campusos'
export DB_USERNAME='campusos'
export DB_PASSWORD='change-me'
mvn spring-boot:run
```
See `.env.example`. The MySQL database must already exist.
