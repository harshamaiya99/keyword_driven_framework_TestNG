package utils;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportManager {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static final Logger log = LoggerFactory.getLogger(ReportManager.class);

    public static void init() {
        ExtentSparkReporter reporter = new ExtentSparkReporter("reports/extent.html");
        reporter.config().setReportName("KDF Automation Report");
        reporter.config().setDocumentTitle("Execution Report");

        extent = new ExtentReports();
        extent.attachReporter(reporter);

        log.info("Initializing ExtentReports...");
    }

    public static void createTest(String name) {
        test.set(extent.createTest(name));
        log.info("=========================================");
        log.info("STARTING TEST: {}", name);
    }

    public static void info(String message) {
        test.get().info(message);
        log.info(message); // Prints to console
    }

    public static void pass(String message) {
        test.get().pass(message);
        log.info("✅ PASS: {}", message);
    }

    public static void fail(String message) {
        test.get().fail(message);
        log.error("❌ FAIL: {}", message);
    }

    public static void skip(String message) {
        test.get().skip(message);
        log.warn("⚠️ SKIP: {}", message);
    }

    public static void flush() {
        if (extent != null) {
            extent.flush();
            log.info("Execution complete. Report saved to reports/extent.html");
        }
    }
}