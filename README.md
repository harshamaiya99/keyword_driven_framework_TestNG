# Keyword-Driven Test Automation Framework (KDF)

A lightweight, scalable, and highly maintainable Keyword-Driven testing framework built using **Java, TestNG, Apache POI, and Reflection API**. 

This framework completely abstracts the test execution logic from the test scenarios. Test cases, test data, and execution flows are defined entirely within an Excel spreadsheet, while the Java backend dynamically routes and executes the corresponding keyword implementations.

## 🚀 Key Features

* **Zero-Maintenance Execution Engine:** Utilizes the `org.reflections` library to dynamically scan and load `@Keyword` annotated methods at startup. No massive `switch` statements or hardcoded keyword arrays required.
* **Custom Dependency Management:** Built-in state tracking via `ExecutionContext`. If a parent test step fails, any dependent test steps are automatically skipped rather than failing blindly.
* **Data-Driven via Excel:** All test scenarios, keywords, and test data are driven from a single `testdata.xlsx` file using Apache POI.
* **Dual-Logging System:** Centralized logging through SLF4J/Log4j2 for real-time console debugging, automatically paired with HTML report logging.
* **Rich HTML Reporting:** Integrates ExtentReports 5 for beautiful, thread-safe, and detailed test execution dashboards.

## 🛠️ Tech Stack

* **Language:** Java 21+ (or 11+)
* **Build Tool:** Maven
* **Test Runner:** TestNG
* **Data Handling:** Apache POI (Excel)
* **Dynamic Routing:** Reflections API
* **Reporting:** ExtentReports
* **Logging:** SLF4J & Log4j2

## 📂 Project Structure

```text
src/
 ├── test/
 │   ├── java/
 │   │   ├── annotations/   # Contains custom @Keyword annotation
 │   │   ├── context/       # ExecutionContext for dependency state tracking
 │   │   ├── engine/        # ExecutionEngine (Reflections scanner)
 │   │   ├── keywords/      # All actual test step implementations live here
 │   │   ├── model/         # TestRow POJO for mapping Excel columns
 │   │   ├── tests/         # KDFTest (TestNG runner with DataProvider)
 │   │   └── utils/         # ExcelReader & ReportManager
 │   └── resources/
 │       └── testdata.xlsx  # Master test suite and test data
pom.xml
testng.xml
```
