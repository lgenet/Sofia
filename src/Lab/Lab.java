package Lab;

import GUI.MainFrame;
import sun.applet.Main;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Lab {
    private ArrayList<QuestionGroup> questionGroups;
    private int labNumber;
    private String studentName;
    private int score;
    private String comments;
    private int latePenalty;
    private String labDocument;

    private boolean isGraded;
    private boolean gradeWasFoundOnFile;
    private String foundLabGrade;

    private boolean wasRegraded;

    private TestResult unitTestResult;

    public Lab(int number, String name, ArrayList<QuestionGroup> questions) {
        assignMembers(number, name, questions, 0);
    }

    public Lab(int number, String name, ArrayList<QuestionGroup> questions, int late) {
        assignMembers(number, name, questions, late);
    }

    public void setLabDocument(String doc) {
        labDocument = doc;
        if(labDocument.equals("")) {
            labDocument = "Could not load lab document for " + studentName;
        }
    }
    public String getLabDocument() {
        return labDocument;
    }

    private void assignMembers(int number, String name, ArrayList<QuestionGroup> questions, int late) {
        questionGroups = questions;
        studentName = name;
        labNumber = number;
        score = 0;
        latePenalty = late;
        labDocument = "";
        foundLabGrade = "";
        unitTestResult = null;
        isGraded = false;
        gradeWasFoundOnFile = false;
        wasRegraded = false;
    }

    public void foundGrade(String grade) {
        isGraded = true;
        gradeWasFoundOnFile = true;
        foundLabGrade = grade;
    }

    public String getUnitTestRawResults() {
        return unitTestResult.getResultString();
    }
    public TestResult getUnitTestResults() {
        return unitTestResult;
    }
    public boolean hasUnitTestResults() {
        return unitTestResult != null;
    }
    public void setUnitTestResult(TestResult tr) {
        unitTestResult = tr;
    }


    public void saveToFile(MainFrame context) {
        try {
            String gradePath = context.config.getStudentGradePath() + "/Lab" + labNumber + "/" + studentName + "-Grade.txt";
            new File(gradePath).getParentFile().mkdirs();
            PrintWriter writer = new PrintWriter(gradePath, "UTF-8");
            writer.println(this);
            writer.close();
        } catch (Exception e) {
            context.displayError("Something went wrong while trying to save " + studentName + "'s grade. " + e);
        }
    }

    public void grade(MainFrame context) {
        if (isGraded) {
            String message = "This lab is already graded.\n" +
                    "Do you wish to re-grade it?";
            context.appendGradingScreenText(message);
            boolean shouldRegrade = context.confirm(message);

            if (!shouldRegrade) {
                context.appendGradingScreenText("Skipping regrade...");
                return;
            }
            this.wasRegraded = true;
            context.clearGradingScreen();
        }
        score = 0;
        score -= latePenalty;
        for (int i = 0; i < questionGroups.size(); i++) {
            questionGroups.get(i).grade(context, this.unitTestResult);
            score += questionGroups.get(i).getScore();
        }
        context.appendGradingScreenText("Comments: ");
        comments = context.getInput();

        postGradeActivities(context);
    }

    private void postGradeActivities(MainFrame context) {
        isGraded = true;
        context.appendGradingScreenText(this.toString());
        saveToFile(context);
    }

    public void printStatus(MainFrame context) {
        if (isGraded && (!gradeWasFoundOnFile || wasRegraded)) {
            context.appendGradingScreenText(this.toString());
        } else if(isGraded && gradeWasFoundOnFile) {
            context.appendGradingScreenText("This lab belongs to: " + studentName);
            context.appendGradingScreenText("\nA grade was found on file for them.\n\n");
            context.appendGradingScreenText(foundLabGrade);
        } else {
            context.appendGradingScreenText("This lab belongs to: " + studentName);
            context.appendGradingScreenText("\nIt has not yet been graded.  If you wish to grade it, press the grade  button to the left.");
        }
    }

    public String toString() {
        String res = "Lab " + labNumber + " Grade Report:\nStudent Name: " + studentName + " (95): " + score + "\n";
        for (int i = 0; i < questionGroups.size(); i++) {
            res += (i + 1) + " " + questionGroups.get(i) + "\n";
        }
        res += "Late Penalty: " + latePenalty + "\n";
        res += "Total Score: " + score + "\n\n";
        res += "Comments: " + comments;
        return res;
    }

    public String getStudentName() {
        return studentName;
    }
}
