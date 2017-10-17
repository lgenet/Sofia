package Lab;

/**
 * Created by Logan on 10/14/2017.
 */
public class Question {
    private int earnedPoints;
    private int maxPoints;
    private String graderQuestion;
    private String rubricStatement;

    public Question(String gQ, String rS, int mxPts) {
        maxPoints = mxPts;
        graderQuestion = gQ;
        rubricStatement = rS;
        earnedPoints = 0;
    }
    private void setPoints() {
        earnedPoints = maxPoints;
    }
    private void setPoints(int p) {
        earnedPoints = p;
    }
    public void grade(int p){
        setPoints(p);
    }
    public void grade(){
        setPoints();
    }

    public int getGrade() {
        return earnedPoints;
    }
    public int getMaxPoints() {
        return maxPoints;
    }
    public String displayToGrader() {
        return graderQuestion + "[" + maxPoints + "]: ";
    }
    public String displayForPrinting() {
        return this.toString();
    }

    public String toString() {
        return "" + rubricStatement + " (" + maxPoints + "): " + earnedPoints;
    }
}
