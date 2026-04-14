package utils;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class ReportManager {

    private static ExtentReports extent;
    private static final Logger log = LoggerFactory.getLogger(ReportManager.class);

    // Maps to track existing Scenarios and TestCases to avoid duplicates in the report
    private static ConcurrentHashMap<String, ExtentTest> scenarioMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, ExtentTest> testCaseMap = new ConcurrentHashMap<>();

    // ThreadLocal specifically for the lowest level (the execution step)
    private static ThreadLocal<ExtentTest> stepTest = new ThreadLocal<>();

    public static void init() {
        ExtentSparkReporter reporter = new ExtentSparkReporter("reports/extent.html");
        reporter.config().setReportName("KDF Automation Report");
        reporter.config().setDocumentTitle("Execution Report");

        extent = new ExtentReports();
        extent.attachReporter(reporter);

        log.info("Initializing ExtentReports...");
    }

    // New method that builds the Scenario -> TestCase -> TestData hierarchy
    // New method that builds the Scenario -> TestCase -> TestData hierarchy
    public static void createTestHierarchy(String scenarioName, String testCaseName, String tdId, String action) {

        // 1. Create or get the Scenario (Only create if it doesn't exist)
        if (!scenarioMap.containsKey(scenarioName)) {
            scenarioMap.put(scenarioName, extent.createTest(scenarioName));
        }
        ExtentTest scenarioNode = scenarioMap.get(scenarioName);

        // 2. Create or get the TestCase (under the Scenario)
        String tcKey = scenarioName + "_" + testCaseName;
        if (!testCaseMap.containsKey(tcKey)) {
            testCaseMap.put(tcKey, scenarioNode.createNode(testCaseName));
        }
        ExtentTest testCaseNode = testCaseMap.get(tcKey);

        // 3. Create the TestData step (under the TestCase)
        String stepName = "TD: " + tdId + " | " + action;
        ExtentTest tdNode = testCaseNode.createNode(stepName);

        stepTest.set(tdNode);

        log.info("=========================================");
        log.info("STARTING: {}", stepName);
    }

    public static void info(String message) {
        stepTest.get().info(message);
        log.info(message);
    }

    public static void pass(String message) {
        stepTest.get().pass(message);
        log.info("✅ PASS: {}", message);
    }

    public static void fail(String message) {
        stepTest.get().fail(message);
        log.error("❌ FAIL: {}", message);
    }

    public static void skip(String message) {
        stepTest.get().skip(message);
        log.warn("⚠️ SKIP: {}", message);
    }

    public static void flush() {
        if (extent != null) {
            extent.flush();
            log.info("Execution complete. Report saved to reports/extent.html");
        }
    }
}