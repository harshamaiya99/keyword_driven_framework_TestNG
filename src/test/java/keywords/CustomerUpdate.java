package keywords;

import annotations.Keyword;
import model.TestRow;
import utils.ReportManager;

public class CustomerUpdate {

    @Keyword("CUSTUPDT")
    public static void updateCustomer(TestRow row) {
        String cust = row.data.get("Customer Number");
        ReportManager.info("Updating Customer: " + cust);
    }
}