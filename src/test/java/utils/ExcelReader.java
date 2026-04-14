package utils;

import model.TestRow;
import org.apache.poi.ss.usermodel.*;
import java.io.FileInputStream;
import java.util.*;

public class ExcelReader {

    public static List<TestRow> readExcel(String path) throws Exception {

        List<TestRow> rows = new ArrayList<>();

        FileInputStream fis = new FileInputStream(path);
        Workbook workbook = WorkbookFactory.create(fis);
        Sheet sheet = workbook.getSheetAt(0);

        String[] headerNames = null;

        // Track the hierarchy state as we read down the spreadsheet
        String currentScenario = "Default Scenario";
        String currentTestCase = "Default Test Case";

        for (Row row : sheet) {

            Cell typeCell = row.getCell(0);
            if (typeCell == null) continue;

            String type = typeCell.toString().trim();

            // Update hierarchy trackers
            if (type.equalsIgnoreCase("TestScenario")) {
                currentScenario = row.getCell(1).toString().trim();
                continue;
            }
            if (type.equalsIgnoreCase("TestCase")) {
                currentTestCase = row.getCell(1).toString().trim();
                continue;
            }

            if (type.equalsIgnoreCase("Keyword")) {
                headerNames = new String[row.getLastCellNum()];
                for (int i = 1; i < row.getLastCellNum(); i++) {
                    headerNames[i] = row.getCell(i).toString().trim();
                }
                continue;
            }

            if (type.equalsIgnoreCase("TestData")) {

                TestRow tr = new TestRow();
                tr.scenarioName = currentScenario; // Assign parent scenario
                tr.testCaseName = currentTestCase; // Assign parent test case
                tr.data = new HashMap<>();

                for (int i = 1; i < row.getLastCellNum(); i++) {

                    String header = headerNames[i];
                    String value = row.getCell(i) != null ? row.getCell(i).toString() : "";

                    if ("TD_ID".equalsIgnoreCase(header)) {
                        tr.tdId = value;
                    } else if ("Function".equalsIgnoreCase(header)) {
                        tr.action = value;
                    } else if ("DependsOnTD".equalsIgnoreCase(header)) {
                        tr.dependsOn = value;
                    } else {
                        tr.data.put(header, value);
                    }
                }

                rows.add(tr);
            }
        }

        workbook.close();
        return rows;
    }
}