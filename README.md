### **Bookstore API Automation Project - README & Test Strategy**

This project automates the testing of an Online Bookstore API with comprehensive coverage for both User Management and Book Management functionalities. The API allows users to register, log in, and manage their books. Only authorized users can perform CRUD operations on books, while unauthorized users can only view books.

The framework is built on BDD (Behavior-Driven Development) using Cucumber and Rest Assured with TestNG for execution and Allure Reports for rich reporting.

### Tools & Technologies
* Java
* Maven for dependency management
* Rest Assured for API automation
* Cucumber (BDD) for feature-driven tests
* TestNG as test executor
* Allure Reports for test reporting
* Jenkins for CI/CD pipeline
* Git for version control

### Project Structure

/project-root
│
├── allure-reports/                          # Allure test reports
├── src
│   ├── main
│   │   └── java
│   │       ├── client/                      # API Client classes (BookClient, UserClient)
│   │       ├── config/                      # YAML configuration reader
│   │       ├── models/                      # Request/Response POJOs (Book, User, ScenarioContext)
│   │       └── utils/                       # Common utility classes (AuthorizationUtil, ConfigReader)
│   └── test
│       ├── java
│       │   ├── runners/                     # Cucumber runner classes
│       │   ├── stepdefinitions/             # Step definitions for features
│       └── resources
│           ├── config/                      # YAML configuration files (multi-env)
│           └── features/                    # Gherkin feature files for Users & Books
│
├── pom.xml                                  # Maven build & dependency configuration
├── testng.xml                               # TestNG suite configuration
├── Jenkinsfile                              # Jenkins pipeline configuration
├── .gitignore                               # Git ignored files
└── README.md                                # This documentation

### Features & Test Strategy Overview

* **Comprehensive API Coverage:** End-to-end testing of User Management and Book Management services including full CRUD operations.
* **Positive & Negative Testing:** Validation of expected behavior for valid scenarios and appropriate error handling for invalid inputs, unauthorized access, and conflict scenarios.
* **Request Chaining:** Seamless flow where outputs (such as tokens, user IDs, or cookies) from one step serve as inputs for subsequent operations—mirroring real user journeys.
* **Environment-Driven Configuration:** Flexible setup using a central YAML configuration file to support multiple environments (DEV, QA, SBX, PROD) without code changes.
* **Behavior-Driven Development (BDD):** Tests written in Gherkin syntax with Cucumber for better collaboration, readability, and business alignment.
* **Allure Reporting:** Rich, interactive reports generated automatically post-execution for actionable insights into test results and coverage.
* **Logging & Debugging:** SLF4J-based structured logging provides clear, real-time feedback on test execution and API interactions.
* **CI/CD Ready:** Fully integrated with Jenkins pipelines (or easily adaptable to other tools like GitHub Actions) for continuous integration, automated test execution, and reporting.
* **Reusable Utility Design**: Shared utilities for common operations such as authentication, environment management, and request handling ensure maintainability and scalability.

### Setup & Configuration
 
**Prerequisites**
Clone the Repository

`git clone https://github.com/your-org/bookstore-api-automation.git
 cd bookstore-api-automation`

**Install Required Tools**

* Java 11+
* Maven 3.x
* Allure Commandline (if needed)
* Configure Environment -> 
    Edit /src/test/resources/config/config.yaml 
* Set correct base URLs and endpoints for user and bookstore services.
* Set Active Environment
      You can pass the desired environment through Maven or your IDE VM options: **-Denv=QA**

#### How to Run Tests

* **Run All Scenarios** : mvn clean test -Denv=QA
* **Run Tests by Tag** : mvn clean test -Denv=QA -Dcucumber.filter.tags="@create"
* **Run via TestNG Suite** : mvn clean test -DsuiteXmlFile=testng.xml -Denv=QA

**Viewing Allure Reports**
* After execution allure reports will be generated in the allure-reports folder.
* Give the command allure serve allure-reports-folder-name 
* The allure reports will be opened in the server 
* Cucumber Reports: Cucumber also produces an HTML report in target/cucumber-html-report.html for BDD test overview.

### CI/CD Pipeline (Jenkins)
