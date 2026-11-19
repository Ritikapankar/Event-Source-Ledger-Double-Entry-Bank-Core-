# Event Sourcing Ledger – Double-Entry Banking Core

A backend banking application built using **Java and Spring Boot**, demonstrating Event Sourcing, Double-Entry Accounting, Transaction Management, and Ledger Architecture.

The project focuses on maintaining a reliable financial transaction history by storing every business operation as an immutable event rather than simply updating the current account balance.

---

## 1. What is Event Sourcing?

Event Sourcing is an architectural pattern in which every state-changing operation is stored as an event.

Instead of storing only the latest state of an entity, the system maintains a sequence of events that represents the complete history of changes.

For example, consider a bank account:

| Event | Amount | Balance |
|---|---:|---:|
| Account Opened | - | ₹10,000 |
| Money Deposited | +₹5,000 | ₹15,000 |
| Money Withdrawn | -₹2,000 | ₹13,000 |
| Money Deposited | +₹3,000 | ₹16,000 |

The system stores these events and can reconstruct the current account state by replaying them.

**Key Idea:** Store what happened, not just what the current state looks like.

---

## 2. Why Do We Use Event Sourcing?

Event Sourcing is useful when an application needs complete historical tracking, auditability, and reliable state reconstruction.

Key benefits:

- **Complete Audit History:** Maintains a record of every business operation.
- **State Reconstruction:** Rebuilds the current account state by replaying events.
- **Debugging:** Helps identify when and why a particular change occurred.
- **Transaction Tracking:** Maintains a traceable history of financial operations.
- **Recovery:** Allows application state to be reconstructed from stored events.
- **Event-Driven Communication:** Enables integration with other services through event publishing.

---

## 3. Where Is Event Sourcing Used?

Event Sourcing is commonly useful in systems where historical changes are important.

| Industry | Use Cases |
|---|---|
| Banking | Deposits, withdrawals, money transfers |
| Payment Systems | Payment processing, refunds, failed transactions |
| E-commerce | Order creation, shipment, cancellation |
| Stock Trading | Buy orders, sell orders, trade execution |
| Insurance | Policy updates and claim processing |
| Financial Systems | Transaction history and audit records |

---

## 4. Traditional CRUD vs Event Sourcing

| Traditional CRUD | Event Sourcing |
|---|---|
| Stores the current state | Stores a sequence of events |
| Previous changes may be overwritten | Historical events are preserved |
| History requires separate tracking | Event history is built into the architecture |
| Directly updates entity state | Reconstructs state from events |
| Suitable for simple applications | Useful for audit-heavy and event-driven systems |

Event Sourcing does not eliminate the need for current-state tables. Read models and snapshots can be maintained to improve query performance.

---

## 5. Double-Entry Accounting

This project also demonstrates the concept of Double-Entry Accounting.

In double-entry accounting, every financial transaction affects at least two ledger entries:

- **Debit:** Records the amount on the debit side.
- **Credit:** Records the amount on the credit side.

The total debit amount must always equal the total credit amount.

### Example: Money Transfer

Suppose Account A transfers ₹2,000 to Account B.

| Account | Debit | Credit |
|---|---:|---:|
| Account A | ₹2,000 | - |
| Account B | - | ₹2,000 |
| **Total** | **₹2,000** | **₹2,000** |

This ensures that financial transactions remain balanced.

---

## 6. How Event Sourcing Works

The general application workflow:

1. A client sends a business request.
2. The Controller receives the request.
3. The Service validates business rules.
4. The application generates a domain event.
5. The event is persisted in the Event Store.
6. Event handlers process the event.
7. The account state or read model is updated.

### Example Event Types

- ACCOUNT_OPENED
- MONEY_DEPOSITED
- MONEY_WITHDRAWN
- MONEY_TRANSFERRED
- TRANSACTION_COMPLETED
- TRANSACTION_FAILED

Events represent facts that have already happened and should be treated as immutable.

---

## 7. Architecture Overview

```text
                 Client / REST API
                        |
                        v
                   Controller
                        |
                        v
                  Service Layer
                        |
                        v
                 Business Validation
                        |
                        v
                  Event Generation
                        |
                        v
                  Event Store
                   (PostgreSQL)
                        |
                        v
                  Event Processing
                        |
              +---------+---------+
              |                   |
              v                   v
        Account State         Ledger Entries
              |                   |
              +---------+---------+
                        |
                        v
                    Read Model
```

Kafka can additionally be integrated for asynchronous event communication between services.

---

## 8. Technology Stack

| Technology | Purpose |
|---|---|
| Java 17 | Backend programming language |
| Spring Boot 3.5.5 | Backend application framework |
| Spring Data JPA | Database persistence |
| PostgreSQL | Relational database and event storage |
| Maven | Dependency and build management |
| Hibernate | ORM framework |
| REST APIs | Client-server communication |
| Git & GitHub | Version control |

---

## 9. Core Concepts Covered

This project focuses on learning and implementing:

- Event Sourcing Architecture
- Double-Entry Accounting
- Event Store Design
- Domain Events
- Account State Reconstruction
- Ledger Management
- Database Transactions
- Data Consistency
- Immutable Event History
- CQRS Architecture
- Event Replay
- Idempotency
- Kafka Integration

---

## 10. Project Structure

```text
bank-core/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/eventsourcing/bank_core/
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── entity/
│   │   │       ├── repository/
│   │   │       ├── dto/
│   │   │       └── BankCoreApplication.java
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│
├── pom.xml
└── README.md
```

*The structure may evolve as additional modules and features are implemented.*

---

## 11. Getting Started

### Prerequisites

- Java 17 or above
- Maven
- PostgreSQL
- IntelliJ IDEA or any Java IDE

### Clone the Repository

```bash
git clone <YOUR_GITHUB_REPOSITORY_URL>
```

### Navigate to the Project

```bash
cd bank-core
```

### Configure PostgreSQL

Update your `application.properties` file:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bankdb
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### Run the Application

```bash
mvn spring-boot:run
```

The application will start on:

```text
http://localhost:8080
```

---

## 12. Learning Objectives

The main objective of this project is to understand how financial backend systems manage account operations, maintain transaction history, and ensure ledger consistency.

Through this project, I aim to gain practical experience with:

- Designing a Spring Boot backend application.
- Implementing Event Sourcing concepts.
- Understanding financial transaction workflows.
- Maintaining balanced debit and credit entries.
- Working with PostgreSQL and JPA.
- Building reliable and auditable backend systems.
- Understanding production-oriented backend architecture.


Built to explore Event Sourcing, Double-Entry Accounting, and Spring Boot backend architecture.

