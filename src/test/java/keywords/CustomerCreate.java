package keywords;

import annotations.Keyword;
import model.TestRow;
import utils.ReportManager;

public class CustomerCreate {

    @Keyword("CUSTPOST")
    public static void createCustomer(TestRow row) {
        String cust = row.data.get("Customer Number");
        ReportManager.info("Creating Customer: " + cust);

        // simulate failure
        if ("1200002".equals(cust)) {
            throw new RuntimeException("Customer creation failed");
        }
    }
}