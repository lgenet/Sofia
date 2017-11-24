package Lab;

import Builder.Runner;
import GUI.GraderEvent;
import GUI.MainFrame;

/**
 * Created by Logan on 10/16/2017.
 */
public class LabManager implements GraderEvent {
    private Lab[] listOfLabs;
    private String[] studentList;
    private int latePenalty;
    private int currentIndex;
    private MainFrame context;

    public LabManager(String[] sl, int lp) {
        currentIndex = -1;
        setStudentListAction(sl);
        latePenalty = lp;
    }

    public void setContext(MainFrame c) {
        context = c;
        String labRubricPath = context.config.getLabRubricPath() + "/lab" + context.config.getLabNumber() + "Questions.txt";
        listOfLabs = LabLoader.buildLabLists(context, studentList, labRubricPath, latePenalty);
    }

    private void setStudentListAction(String[] list) {
        studentList = list;
        for(int i = 0; i < studentList.length; i++){
            studentList[i] = studentList[i].replaceAll(" ", "_");
        }
    }
    public void setStudentList(String[] list) {
        setStudentListAction(list);
        rebuildLabs();
    }
    public String[] getStudentList() {
        return studentList;
    }

    public void rebuildLabs() {
        currentIndex = -1;
        String labRubricPath = context.config.getLabRubricPath() + "lab" + context.config.getLabNumber() + "Questions.txt";
        listOfLabs = LabLoader.buildLabLists(context, studentList, labRubricPath, latePenalty);
        receivedPreviousEvent();
    }

    public void receivedGradeEvent() {
        if (currentIndex < 0 || currentIndex >= listOfLabs.length) {
            context.appendGradingScreenText("You have not selected a lab, please use the next and previous buttons to select a valid lab to grade.");
            return;
        }
        runUnitTest();
        context.appendGradingScreenText("");

        listOfLabs[currentIndex].grade(context);

        if (context.config.isCanAutoContinue()) {
            receivedNextEvent();
        }
    }

    public void receivedPreviousEvent() {
        currentIndex--;
        if (currentIndex >= 0) {
            context.loadLab(listOfLabs[currentIndex].getLabDocument());
            context.clearGradingScreen();
            listOfLabs[currentIndex].printStatus(context);
        } else {
            handelOutOfBounds(-1, "You are at the beginning, you can not go any further back.");
        }
    }

    public void receivedNextEvent() {
        currentIndex++;
        context.updateStatus("Now viewing lab " + (currentIndex + 1) + " out of " + listOfLabs.length + " labs.");
        if (currentIndex < listOfLabs.length) {
            context.loadLab(listOfLabs[currentIndex].getLabDocument());
            context.clearGradingScreen();
            listOfLabs[currentIndex].printStatus(context);
            if (context.config.isRunTestOnSwitch()) {
                runUnitTest();
            }
        } else {
            handelOutOfBounds(listOfLabs.length, "There are no more labs to grade.  Feel free to go back and review the graded labs.");
        }
    }

    private void handelOutOfBounds(int index, String message) {
        context.clearGradingScreen();
        currentIndex = index;
        context.appendGradingScreenText(message);
        context.loadLab("");
    }

    private void runUnitTest() {
        Lab current = listOfLabs[currentIndex];
        if (current.hasUnitTestResults()) {
            context.appendGradingScreenText(current.getUnitTestResults());
            return;
        }
        String fileName;
        if(context.config.getCompilerPreference().equals("UnitTest")) {
            context.appendGradingScreenText("Running unit tests...");
            fileName = "unitTest" + context.config.getUnitTestExeName();
        }
        else {
            context.appendGradingScreenText("Running the lab...");
            fileName = "runnable" + context.config.getUnitTestExeName();
        }
        String studentExePath = context.config.getStudentInputPath() + "Lab" + context.config.getLabNumber() + "/"
                + current.getStudentName() + "/" + fileName;
        String results = Runner.runAndGetResults(context, studentExePath);
        current.setUnitTestResult(new TestResult(results));
        context.appendGradingScreenText(current.getUnitTestResults());
    }
}