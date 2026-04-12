package keywords;

import model.TestRow;
import utils.ReportManager;

public class AccountKeywords {

    public static void createAccount(TestRow row) {

        String cust = row.data.get("Customer Number");

        ReportManager.info("Creating Account for: " + cust);
    }
}