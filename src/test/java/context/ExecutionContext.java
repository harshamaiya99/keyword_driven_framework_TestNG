package context;

import java.util.concurrent.ConcurrentHashMap;

public class ExecutionContext {

    private static ConcurrentHashMap<String, String> statusMap = new ConcurrentHashMap<>();

    public static void markPass(String tdId) {
        statusMap.put(tdId, "PASS");
    }

    public static void markFail(String tdId) {
        statusMap.put(tdId, "FAIL");
    }

    public static String getStatus(String tdId) {
        return statusMap.get(tdId);
    }
}