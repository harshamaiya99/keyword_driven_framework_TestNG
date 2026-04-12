package utils;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ReportManager {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public static void init() {
        ExtentSparkReporter reporter = new ExtentSparkReporter("reports/extent.html");
        reporter.config().setReportName("KDF Automation Report");
        reporter.config().setDocumentTitle("Execution Report");

        extent = new ExtentReports();
        extent.attachReporter(reporter);
    }

    public static void createTest(String name) {
        test.set(extent.createTest(name));
    }

    public static void info(String message) {
        test.get().info(message);
    }

    public static void pass(String message) {
        test.get().pass(message);
    }

    public static void fail(String message) {
        test.get().fail(message);
    }

    public static void skip(String message) {
        test.get().skip(message);
    }

    public static void flush() {
        if (extent != null) {
            extent.flush();
        }
    }
}