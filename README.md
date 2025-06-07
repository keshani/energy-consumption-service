# 🔋 Enefit Energy Consumption Service

This is a Spring Boot-based service calculating users' monthly electricity consumption and related costs. 
The application integrates with a public market price API to calculate cost per kWh and uses JWT-based security for user authentication.

---

## 📦 Features

- 🔐 Secure login with JWT authentication
- 📊 Monthly energy consumption cost calculation
- 💰 Integration with market electricity pricing API
- 🧾 PostgreSQL + Liquibase for data and schema management
- 🐳 Dockerized deployment

---

## 🛠 Technologies

- Java 21
- Spring Boot 3.5
- Spring Security (JWT)
- PostgreSQL
- Liquibase
- WebClient (for external API calls)
- Docker & Docker Compose

---

## 🚀 Getting Started

### 1. Clone the Repo

```bash
git clone https://github.com/your-org/energy-consumption-service.git
cd energy-consumption-service
``` 

### 2. Build the application by running below command

```bash
./gradlew build
```

### 3. Run via Docker Compose
```bash
docker-compose up --build
```

