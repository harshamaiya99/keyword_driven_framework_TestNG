package tests;

import context.ExecutionContext;
import engine.ExecutionEngine;
import model.TestRow;
import org.testng.SkipException;
import org.testng.annotations.*;
import utils.ExcelReader;
import utils.ReportManager;

import java.util.List;

public class KDFTest {

    @BeforeSuite
    public void setupReport() {
        ReportManager.init();
        ExecutionEngine.init(); // Pre-load all keywords before tests start
    }

    @AfterSuite
    public void tearDownReport() {
        ReportManager.flush();
    }

    @DataProvider(name = "excelData")
    public Object[][] getData() throws Exception {

        List<TestRow> list = ExcelReader.readExcel("src/test/resources/testdata.xlsx");

        return list.stream()
                .map(r -> new Object[]{r})
                .toArray(Object[][]::new);
    }

    @Test(dataProvider = "excelData")
    public void runRow(TestRow row) throws Exception {

        ReportManager.createTestHierarchy(row.scenarioName, row.testCaseName, row.tdId, row.action);
        ReportManager.info("Starting execution");

        // 🔵 UPDATED: Multiple Dependency check
        if (row.dependsOn != null && !row.dependsOn.trim().isEmpty()) {

            // Split by comma in case there are multiple dependencies
            String[] dependencies = row.dependsOn.split(",");

            for (String dep : dependencies) {
                dep = dep.trim(); // Remove any accidental spaces from Excel
                String status = ExecutionContext.getStatus(dep);

                // If ANY dependency is not PASS, skip this test
                if (!"PASS".equals(status)) {
                    ReportManager.skip("Skipped due to failing dependency: " + dep);
                    throw new SkipException("Skipping due to failing dependency: " + dep);
                }
            }
        }

        try {
            ExecutionEngine.execute(row);

            ExecutionContext.markPass(row.tdId);
            ReportManager.pass("Execution PASSED");

        } catch (Exception e) {

            ExecutionContext.markFail(row.tdId);
            ReportManager.fail("Execution FAILED: " + e.getMessage());

            throw e;
        }
    }
}