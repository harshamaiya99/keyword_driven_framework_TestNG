package keywords;

import model.TestRow;
import utils.ReportManager;

public class CustomerKeywords {

    public static void createCustomer(TestRow row) {

        String cust = row.data.get("Customer Number");

        ReportManager.info("Creating Customer: " + cust);

        // simulate failure
        if ("1200002".equals(cust)) {
            throw new RuntimeException("Customer creation failed");
        }
    }

    public static void updateCustomer(TestRow row) {

        String cust = row.data.get("Customer Number");

        ReportManager.info("Updating Customer: " + cust);
    }
}