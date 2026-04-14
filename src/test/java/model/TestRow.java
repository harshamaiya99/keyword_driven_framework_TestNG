package model;

import java.util.Map;

public class TestRow {
    public String scenarioName; // Added
    public String testCaseName; // Added
    public String tdId;
    public String action;
    public String dependsOn;
    public Map<String, String> data;
}