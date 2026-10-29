# Suyubek Alisher Student API

Student: Suyubek Alisher | IT2-2306 | Student ID 37773.
Documentation status: initial project.

Spring Boot 3.5.7, Java 21, Gradle wrapper 8.14.3, PostgreSQL 17.

## Run

Create a private .env from .env.example, choose a fresh DB_PASSWORD, then:

```bash
docker compose config --quiet
docker compose up -d --build --wait
curl -fsS http://127.0.0.1:8081/info
```

The database hostname is db inside the Compose network. The API is published
only on 127.0.0.1:8081. PostgreSQL has no host port. Do not commit .env.

## Verification

```bash
./gradlew --no-daemon clean test bootJar
python3 scripts/verify_api.py
```

CRUD routes: POST/GET /students and GET/PUT/DELETE /students/{id}.
Invalid input returns 400, duplicate student IDs return 409, absent records 404.
The /info endpoint identifies the project owner and the actual build version.

## Data and lifecycle

Use docker compose down and up without -v to preserve the database volume.
The verify_api.py script cleans up only its own test records. Persistent owner
records for the restart demonstration are managed separately.

## Jenkins

The main job Suyubek_Alisher_Student_API reads Jenkinsfile from GitHub.
Checkout, Build, Test, Docker Build and Verify execute in order. SCM polling
checks for source changes once per minute. Deployment is demonstrated through
Compose; Jenkins verification checks the image and does not deploy it.
