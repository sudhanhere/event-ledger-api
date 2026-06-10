## Event Ledger API
Event Ledger is a high-performance Spring Boot API designed to ingest financial transactions from multiple upstream systems. The system natively resolves out-of-order event delivery using event timestamps and sequence tracking, ensuring strict data consistency and accurate ledger balances in an H2 database.
## System Prerequisites
Ensure you have the following software installed locally:

* Java Development Kit (JDK): Version 17
* Development IDE: VS Code  

Note: You do not need to install Gradle manually. The project includes the Gradle Wrapper (gradlew)
## Getting Started## 1. Clone the Repository

git clone https://github.com/sudhanhere/event-ledger-api.git
cd event-ledger-api

## 2. Configure Environment Variables
The application uses an in-memory H2 database by default. Review or update configuration settings in src/main/resources/application.yml 

## 3. Build and Run the Application
Execute the Gradle Wrapper to compile the source code, resolve dependencies, and launch the embedded Tomcat server 

# On Linux / macOS
./gradlew bootRun
# On Windows
gradlew.bat bootRun

The server will start by default on port 8080.
## Running Test Cases
The project includes automated suite execution to validate ledger calculations and out-of-order processing logic. Run the following command:

# On Linux / macOS
./gradlew test
# On Windows
gradlew.bat test

Test execution reports will generate locally at build/reports/tests/test/index.html.
## API Usage## Ingest a Transaction

* Endpoint: POST /api/v1/events
* Content-Type: application/json

## Request Payload Example  
Upstream systems must provide an idempotent transaction ID and a precise event timestamp to handle out-of-order resolution.

{
  "eventId": "evt-001",
  "accountId": "acc-002",
  "type": "CREDIT",
  "amount": 10,
  "currency": "USD",
  "eventTimestamp": "2026-06-09T21:25:59Z",
  "metadata": {"source": "api", "region": "US"}
}
## Response Lifecycle

* 201 Created: Event received and queued for out-of-order ledger validation.
* 400 Bad Request: Missing mandatory fields or malformed payload.

## View All Events
* Endpoint: GET /api/v1/events
* Content-Type: application/json

## View Events by Account Id
* Endpoint: GET /api/v1/events/account/{Acc-Id}
* Content-Type: application/json

## Get Balance for Account Id
* Endpoint: GET api/v1/events/account/{Acc-Id}/balance
* Content-Type: application/json
