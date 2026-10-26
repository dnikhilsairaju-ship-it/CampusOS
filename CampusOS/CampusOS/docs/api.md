# API quick reference

Base URL: `http://localhost:8080/api/v1`

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/dashboard?role=STUDENT` | Role-aware summary |
| GET, POST | `/tickets` | List/create tickets |
| PATCH | `/tickets/{id}/status?status=RESOLVED` | Update ticket status |
| GET | `/incidents` | Potential incidents grouped from reports |
| GET, POST | `/resources`, `/bookings` | Resources and reservations |
| GET | `/knowledge?q=exam` | Search articles with source title |
| GET, POST | `/opportunities`, `/opportunities/{id}/bookmark` | Opportunities/bookmarks |
| GET | `/analytics` | Aggregate metrics |
| GET | `/notifications` | Demo notifications |
| GET | `/swagger-ui.html` | OpenAPI UI (if available from springdoc version) |

Example ticket:
```json
{
  "title": "Wi-Fi not working",
  "description": "Unable to connect in the library",
  "category": "NETWORK",
  "location": "Central Library",
  "priority": "MEDIUM",
  "reporter": "student@campusos.demo"
}
```
