# 🍃 Spring Boot Document API

The central nervous system of the Distributed OCR platform. It acts as an ingestion point for the OCR service, a persistent storage layer, and a real-time event broadcaster for the frontend.

## 🏗 Architecture
Built on **Spring Boot 3** using an N-Tier Layered Architecture:
* **Controller Layer:** Exposes REST endpoints and validates input using DTOs (`DocumentRequest`).
* **Service Layer:** Handles business logic and manages **Server-Sent Events (SSE)** subscriptions.
* **Mapper Layer:** Decouples the internal Database Entity from the external API contract.
* **Repository Layer:** JPA interface for H2/SQL persistence.

## 🚀 Features
* **Real-Time Streaming:** Uses `SseEmitter` to push new data to the frontend instantly (no page refreshes required).
* **DTO Pattern:** Protects the internal database structure from external API calls.
* **Validation:** Strict `@Valid` annotations ensure no bad data (negative amounts, invalid RUCs) enters the system.
* **CORS Config:** Centralized security configuration to allow trusted frontend communication.

## 🛠 Prerequisites
* **Java 17 JDK** or higher
* **Maven 3.8+**

## ⚙️ Installation

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/your-username/document-api.git](https://github.com/your-username/document-api.git)
    cd document-api
    ```

2.  **Build the Project:**
    ```bash
    mvn clean install
    ```

## 🏃‍♂️ Execution

1.  **Run the Application:**
    ```bash
    mvn spring-boot:run
    ```

2.  **API Endpoints:**
    * `POST /api/documents`: Receives JSON from Python OCR.
    * `GET /api/documents`: Returns paginated list of documents.
    * `GET /api/documents/stream`: Subscribes to the live event stream.

## 📂 Project Structure
```text
src/main/java/com/aquarius/document_api
  ├── /config       # CORS & Security
  ├── /controller   # REST Endpoints
  ├── /domain       # Database Entities
  ├── /dto          # Data Transfer Objects
  ├── /mapper       # Entity <-> DTO Conversion
  ├── /repository   # JPA Data Access
  └── /service      # Business Logic & SSE
