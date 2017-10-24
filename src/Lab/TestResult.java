package Lab;

/**
 * Created by Logan on 10/18/2017.
 */
public class TestResult {
    private String output;
    private int score;

    public TestResult(String o, int s) {
        output = o;
        score = s;
    }
    public TestResult(String res) {
        output = res;
        score = calculateScore(res);
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
        return sep + "Tests results: \n" + sep + output + "\n" + sep;
    }

    public String getResultString() {
        return output;
    }
    public int getScore() {
        return score;
    }
}
