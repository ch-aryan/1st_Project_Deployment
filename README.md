# 🏦 Spring Boot Core Banking System API

A secure, robust, and scalable backend RESTful Banking System built with **Spring Boot 3.4.2**, **Java 17**, **Spring Security 6**, **JWT (JSON Web Tokens)**, **OAuth2**, and **PostgreSQL**.

---

## 🚀 Features

### 🔐 1. Authentication & Security
- **JWT-Based Stateless Authentication**: Secure token generation, validation, and request filtering via `JwtAuthenticationFilter`.
- **OAuth2 Social Login**: Integrated Google & GitHub OAuth2 login flow with seamless profile linking.
- **PIN & Password Encryption**: Industry-standard BCrypt password hashing and custom secure PIN verification.
- **Role & Context Protection**: Sensitive endpoints (Deposit, Withdraw, Transfer, History) are strictly protected by Spring Security.

### 💳 2. Core Banking Engine
- **Customer Registration & Onboarding**: Validated registration with minimum age criteria, unique usernames, and automated account generation.
- **Deposit Operations**: Real-time deposit processing with PIN validation and transaction ledger recording.
- **Withdrawal Operations**: Balance verification, overdraft prevention, and debit transaction recording.
- **Atomic Fund Transfers**: Transactional multi-account money transfer ensuring debit and credit consistency.
- **Transaction History**: Real-time statement and audit log for all account activities.

---

## 🛠️ Tech Stack

- **Backend Framework**: Spring Boot 3.4.2
- **Language**: Java 17
- **Database**: PostgreSQL (Production) / H2 In-Memory (Test suite)
- **ORM / Persistence**: Spring Data JPA / Hibernate
- **Security**: Spring Security 6, JJWT (0.12.6), OAuth2 Client
- **Testing**: JUnit 5, Mockito, Spring Boot Starter Test (28 tests passing)
- **Build Tool**: Maven

---

## ⚙️ Configuration & Environment Setup

All sensitive credentials and database connection details are strictly externalized via environment variables.

### 1. Configure Environment Variables
You can copy `.env.example` to `.env` or pass the following variables in your execution environment:

```env
# Database Configuration
DB_URL=jdbc:postgresql://localhost:5432/banking_system_db
DB_USERNAME=postgres
DB_PASSWORD=your_secure_password

# JWT Configuration
JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
JWT_EXPIRATION_MS=86400000

# OAuth2 (Optional for local testing)
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret
GITHUB_CLIENT_ID=your_github_client_id
GITHUB_CLIENT_SECRET=your_github_client_secret
```

Alternatively, copy `src/main/resources/application.properties.example` to `src/main/resources/application-local.properties` (which is git-ignored) for local development overrides.

---

## 🧪 Running Tests & Building

Run the complete test suite (28 passing unit and controller tests):
```bash
./mvnw test
```

Build executable JAR:
```bash
./mvnw clean package
```

Run application:
```bash
./mvnw spring-boot:run
```

---

## 📡 Core API Endpoints

| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v3/register` | Register a new customer & account | No |
| `POST` | `/api/v3/auth/login` | User login & obtain JWT token | No |
| `POST` | `/api/banking/deposit` | Deposit funds into account | Yes (JWT) |
| `POST` | `/api/banking/withdraw` | Withdraw funds from account | Yes (JWT) |
| `POST` | `/api/banking/transfer` | Atomic transfer between accounts | Yes (JWT) |
| `GET` | `/api/banking/transactions` | Retrieve account transaction history | Yes (JWT) |
