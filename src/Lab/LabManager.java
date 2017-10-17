package Lab;

import GUI.GraderEvent;
import GUI.MainFrame;

import javax.swing.*;
import java.io.PrintWriter;

/**
 * Created by Logan on 10/16/2017.
 */
public class LabManager implements GraderEvent {
    private Lab[] listOfLabs;
    private String[] studentList;
    private int labNumber;
    private int latePenalty;
    private int currentIndex;
    private MainFrame context;

    public LabManager(String[] sl, int ln, int lp) {
        currentIndex = -1;
        labNumber = ln;
        studentList = sl;
        latePenalty = lp;
        listOfLabs = LabLoader.buildLabLists(studentList, labNumber, "./rubrics/lab" + labNumber + "Questions.txt", latePenalty);
    }

    public void setContext(MainFrame c) {
        context = c;
    }

    public void setLabNumber(int n) {
        currentIndex = 1;
        labNumber = n;
        listOfLabs = LabLoader.buildLabLists(studentList, labNumber, "./rubrics/lab" + labNumber + "Questions.txt", latePenalty);
        receivedPreviousEvent();
    }


    public void receivedGradeEvent() {
        if (currentIndex < 0 || currentIndex >= listOfLabs.length) {
            System.out.println("You have not selected a lab, please use the next and previous buttons to select a valid lab to grade.");
            return;
        }
        listOfLabs[currentIndex].grade(context);

        if (context.canAutoContinue()) {
            receivedNextEvent();
        }
    }

    public void receivedPreviousEvent() {
        currentIndex--;
        if (currentIndex >= 0) {
            context.loadLab(listOfLabs[currentIndex].getLabDocument());
            context.clearGradingScreen();
            listOfLabs[currentIndex].printStatus();
        } else {
            handelOutOfBounds(-1, "You are at the beginning, you can not go any further back.");
        }
    }

    public void receivedNextEvent() {
        currentIndex++;
        if (currentIndex < listOfLabs.length) {
            context.loadLab(listOfLabs[currentIndex].getLabDocument());
            context.clearGradingScreen();
            listOfLabs[currentIndex].printStatus();
        } else {
            handelOutOfBounds(listOfLabs.length, "There are no more labs to grade.  Feel free to go back and review the graded labs.");
        }
    }

    private void handelOutOfBounds(int index, String message) {
        context.clearGradingScreen();
        currentIndex = index;
        System.out.println(message);
        context.loadLab("");
    }
}