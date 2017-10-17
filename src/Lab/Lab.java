package Lab;

import GUI.GraderEvent;
import GUI.MainFrame;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

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
        isGraded = false;
        gradeWasFoundOnFile = false;
    }

    public void foundGrade(String grade) {
        isGraded = true;
        gradeWasFoundOnFile = true;
        foundLabGrade = grade;
    }

    public void saveToFile() {
        try {
            PrintWriter writer = new PrintWriter("./Grades/Lab" + labNumber + "/" + studentName + "-Grade.txt", "UTF-8");
            writer.println(this);
            writer.close();
        } catch (Exception e) {
            System.out.println("Something went wrong while trying to save " + studentName + "'s grade. " + e);
        }
    }

    public void grade(MainFrame context) {
        if (isGraded) {
            System.out.println("This lab is already graded.\n");
            System.out.println("Do you wish to re-grade it? Y/N");
            String con = context.getInput();

            if (!con.equalsIgnoreCase("y")) {
                System.out.println("Skipping regrade...");
                return;
            }
            context.clearGradingScreen();
        }
        score = 0;
        score -= latePenalty;
        for (int i = 0; i < questionGroups.size(); i++) {
            questionGroups.get(i).grade(context);
            score += questionGroups.get(i).getScore();
        }
        System.out.println("Comments: ");
        comments = context.getInput();

        postGradeActivities();
    }

    private void postGradeActivities() {
        isGraded = true;
        System.out.println(this);
        saveToFile();
    }

    public void printStatus() {
        if (isGraded && !gradeWasFoundOnFile) {
            System.out.println(this);
        } else if(isGraded && gradeWasFoundOnFile) {
            System.out.println("This lab belongs to: " + studentName);
            System.out.println("\nA grade was found on file for them.\n\n");
            System.out.println(foundLabGrade);
        } else {
            System.out.println("This lab belongs to: " + studentName);
            System.out.println("\nIt has not yet been graded.  If you wish to grade it, press the grade  button to the left.");
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
}