package Lab;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Logan on 10/18/2017.
 */
public class TestResult {
    private String rawOutput;
    private int score;
    private ArrayList<String> passedTests;
    private ArrayList<String> failedTests;


    private void init(String raw, int s) {
        passedTests = new ArrayList<>();
        failedTests = new ArrayList<>();

        score = s;
        rawOutput = raw;
        readExampleUnitFile();

    }
    public TestResult(String o, int s) {
        init(o, s);
    }
    public TestResult(String res) {
        init(res, calculateScore(res));
    }

    private String getUnitTestName(String line) {
        line = line.split("Test.")[1];
        return line.split("\\s")[0];
    }

    public void readExampleUnitFile() {
        Scanner scan = new Scanner(rawOutput);
        while (scan.hasNext()) {
            String line = scan.nextLine();
            if (line.contains("[       OK ]")) {
                passedTests.add(getUnitTestName(line));
            } else if (line.contains("[  FAILED  ]") && line.contains(".")) {
                failedTests.add(getUnitTestName(line));
            }
        }
    }
    private int calculateScore(String res) {
        return 75;
    }

    private static String getOutputSeparator(int n) {
        String line = "";
        for(int i = 0; i < n; i++) {
            line += "=";
        }
        return line + "\n";
    }
    public String toString() {
        String sep = getOutputSeparator(50);
        return sep + "Tests results: \n" + sep + rawOutput + "\n" + sep;
    }

    public String getResultString() {
        return rawOutput;
    }
    public int getScore() {
        return score;
    }
    public boolean isTestPassed(String testName) {
        return passedTests.contains(testName);
    }
    public boolean isTestFailed(String testName) {
        return failedTests.contains(testName);
    }
}
