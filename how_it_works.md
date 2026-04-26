### ⚙️ Phase 1: Pre-Execution (Framework Boot-Up)
When TestNG starts, it looks for the `@BeforeSuite` annotations to set up the global environment.
1. **Report Initialization:** `ReportManager.init()` is called. This creates the ExtentSparkReporter and prepares the HTML file (`reports/extent.html`), logging the startup to the console via SLF4J.
2. **Reflection Scanning (The Engine):** `ExecutionEngine.init()` is called. The `org.reflections` library scans your entire `keywords` package. It searches for any method tagged with `@Keyword` (e.g., `@Keyword("TXNPOST")`).
3. **Memory Caching:** Every matched keyword method is stored in a highly efficient `HashMap` called `KEYWORD_CACHE`. The engine logs exactly how many keywords it successfully loaded into memory.

### 🗂️ Phase 2: Data Extraction (The TestNG DataProvider)
Before running any actual tests, TestNG needs to know what data it is working with.
1. **Excel Parsing:** TestNG triggers the `@DataProvider` in `KDFTest`, which calls `ExcelReader.readExcel()`.
2. **Hierarchical Tracking:** As Apache POI reads `testdata.xlsx` row by row:
    * If it sees a **TestScenario** or **TestCase** row, it temporarily saves those names in memory (`currentScenario`, `currentTestCase`).
    * If it sees a **TestData** row, it creates a new `TestRow` POJO. It injects the saved Scenario and TestCase names into this object, alongside the actual test data (`TD_ID`, `Function`, `DependsOnTD`, and parameters).
3. **Data Feed:** A massive Array of these `TestRow` objects is passed back to TestNG.

### 🚦 Phase 3: Routing & Dependency Checks (Row-by-Row Execution)
TestNG begins executing the `@Test` method (`runRow`) for every single `TestRow` object provided by the Excel sheet.
1. **Report Node Creation:** `ReportManager.createTestHierarchy()` is triggered. It checks if the parent Scenario or TestCase folders already exist in the report. If they don't, it creates them. Then, it creates a specific "Node" for the current test step (e.g., `TD_3.1.3 | TXNPOST`).
2. **Multiple Dependency Verification:** The runner checks the `DependsOnTD` column.
    * If dependencies exist (e.g., `TD_3.1.1, TD_3.1.2`), it splits the string by commas.
    * It loops through each dependency and checks the `ExecutionContext` memory map.
    * If *any* of those previous test IDs are not marked as `"PASS"`, a `SkipException` is immediately thrown, safely aborting this row without failing it blindly.

### ⚡ Phase 4: Dynamic Keyword Invocation (The Execution)
If the dependencies pass (or if there are none), the row is sent to the `ExecutionEngine`.
1. **Instant Lookup:** `ExecutionEngine.execute(row)` takes the action name (e.g., `CUSTPOST`) and instantly pulls the corresponding Java `Method` out of the memory cache.
2. **Reflection Execution:** The engine dynamically invokes that method, passing the `TestRow` payload directly into it (`targetMethod.invoke(null, row)`).
3. **Business Logic & Dual-Logging:** The specific keyword class (e.g., `CustomerKeywords`) runs its automation logic (API calls, Selenium interactions, etc.). Every time `ReportManager.info()` is called inside the keyword, it simultaneously writes to the ExtentReports UI and prints a clean SLF4J log to your IDE console.
4. **Pass/Fail State Recording:**
    * **If successful:** `ExecutionContext.markPass()` is called to allow future dependent rows to run. `ReportManager.pass()` adds a green checkmark to the report.
    * **If an exception occurs:** The `catch` block in the runner intercepts it. `ExecutionContext.markFail()` is called (blocking future dependents). `ReportManager.fail()` logs the red error, and the exception is re-thrown so TestNG officially marks the test as a failure.

### 🧹 Phase 5: Post-Execution (Tear Down)
Once every row from the Excel sheet has been processed, TestNG reaches the `@AfterSuite` annotation.
1. **Report Flushing:** `ReportManager.flush()` is called. This compiles all the Scenarios, Test Cases, and Test Data nodes into the final, interactive `extent.html` dashboard and saves it to your hard drive.
2. **Console Wrap-Up:** A final log is printed to the console indicating the execution is completely finished.