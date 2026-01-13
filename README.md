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

## 🗄️ Database Configuration (MSSQL)
By default, the application is configured for development. To connect to a production **Microsoft SQL Server**, follow these steps:
1.  **Add Dependency:**
    Ensure the MSSQL JDBC driver is present in your `pom.xml`:
    ```xml
    <dependency>
        <groupId>com.microsoft.sqlserver</groupId>
        <artifactId>mssql-jdbc</artifactId>
        <scope>runtime</scope>
    </dependency>
    ```
2.  **Update Configuration:**
    Modify `src/main/resources/application.properties` with your credentials:
    ```properties
    # MSSQL Connection Settings
    spring.datasource.url=jdbc:sqlserver://YOUR_HOST:1433;databaseName=AquariusDB;encrypt=true;trustServerCertificate=true
    spring.datasource.username=YOUR_USERNAME
    spring.datasource.password=YOUR_PASSWORD
    spring.datasource.driverClassName=com.microsoft.sqlserver.jdbc.SQLServerDriver

    # JPA / Hibernate Settings
    spring.jpa.show-sql=true
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.SQLServer2016Dialect
    ```
    *> **Note:** `ddl-auto=update` will automatically create the `DOCUMENT` table if it does not exist.*
    *> **SQL SCRIPT FOR MANUAL CREATION OF DATABASE AND TABLE:** 
    ```sql
    -- 1. Create the Database
    CREATE DATABASE AquariusDev;
    GO
    
    -- 2. Switch to the new Database
    USE AquariusDev;
    GO
    
    -- 3. Create the Document Table
    CREATE TABLE DOCUMENT (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        document_type VARCHAR(50) NOT NULL,
        document_number VARCHAR(50),
        document_date DATE NOT NULL,
        issuer_ruc VARCHAR(11) NOT NULL,
        amount DECIMAL(19, 2) NOT NULL,
        image_base64 VARCHAR(MAX), 
        created_at DATETIME2 DEFAULT GETDATE()
    );
    GO
    ```

## 🛠 Prerequisites
* **Java 17 JDK** or higher
* **Maven 3.8+**

## ⚙️ Installation

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/Shalabi98/document-api.git](https://github.com/Shalabi98/document-api.git)
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
