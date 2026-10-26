# Architecture

- `frontend/`: React + TypeScript + Vite single-page application.
- `backend/`: Spring Boot REST API, JPA persistence, validation, H2 demo profile and MySQL profile.
- API base path: `/api/v1`.
- Main flow: browser UI → REST controller → Spring Data repository → relational database.
- Incident detection in this MVP groups tickets with the same category and normalized location. Such groups are explicitly labelled **potential / unverified**; staff must verify them.
- Resource bookings reject overlapping time ranges for the same resource.
- Knowledge search uses keyword matching and returns the article title as the source reference.
- Demo login is for local demonstration only and is not a production authentication system.
