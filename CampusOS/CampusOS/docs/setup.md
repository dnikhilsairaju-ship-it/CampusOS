# Setup

1. Install Java 21, Maven, Node.js 20+.
2. In `backend`, run `mvn spring-boot:run`.
3. In `frontend`, run `npm install` then `npm run dev`.
4. Use the demo role selector to enter the UI.
5. API health check: `GET http://localhost:8080/api/v1/dashboard?role=STUDENT`.

The backend seeds fictional campus data on startup. H2 is in-memory by default, so data persists while the application is running but resets after restart. Use the MySQL profile for durable local persistence.
