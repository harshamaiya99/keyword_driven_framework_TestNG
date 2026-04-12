package keywords;

import annotations.Keyword;
import model.TestRow;
import utils.ReportManager;

public class AccountCreate {

    @Keyword("ACCTPOST")
    public static void createAccount(TestRow row) {
        String cust = row.data.get("Customer Number");
        ReportManager.info("Creating Account for: " + cust);
    }
}