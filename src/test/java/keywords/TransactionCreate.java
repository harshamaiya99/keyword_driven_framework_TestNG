package keywords;

import annotations.Keyword;
import model.TestRow;
import utils.ReportManager;

public class TransactionCreate {

    @Keyword("TXNPOST")
    public static void transferFunds(TestRow row) {
        // Extract the new columns from the Excel row
        String fromAcc = row.data.get("From Account");
        String toAcc = row.data.get("To Account");
        String amount = row.data.get("Amount");

        // Log the step to both the console and the ExtentReport
        ReportManager.info("Processing transfer of $" + amount + " from Account " + fromAcc + " to Account " + toAcc);

        // Simulate a business rule failure for demonstration
        if ("250".equals(amount)) {
            throw new RuntimeException("Transfer declined: Minimum transfer amount is $500");
        }
    }
}