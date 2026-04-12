package engine;

import model.TestRow;
import keywords.*;

public class ExecutionEngine {

    public static void execute(TestRow row) throws Exception {

        switch (row.action) {

            case "CUSTPOST":
                CustomerKeywords.createCustomer(row);
                break;

            case "CUSTUPDT":
                CustomerKeywords.updateCustomer(row);
                break;

            case "ACCTPOST":
                AccountKeywords.createAccount(row);
                break;

            default:
                throw new RuntimeException("Unknown action: " + row.action);
        }
    }
}