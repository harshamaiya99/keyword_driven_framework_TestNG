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

        // 🔵 Create hierarchical report entry
        ReportManager.createTestHierarchy(row.scenarioName, row.testCaseName, row.tdId, row.action);

        ReportManager.info("Starting execution");

        // Dependency check
        if (row.dependsOn != null && !row.dependsOn.isEmpty()) {

            String status = ExecutionContext.getStatus(row.dependsOn);

            if (!"PASS".equals(status)) {
                ReportManager.skip("Skipped due to dependency: " + row.dependsOn);
                throw new SkipException("Skipping due to dependency: " + row.dependsOn);
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