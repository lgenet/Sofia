package SofiaCore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ConfigManager implements Serializable {
    private String studentListPath;     // Directory path where the Student List file can be found
    private String studentInputPath;    // Directory path where the Student Inputs can be found
    private String studentGradePath;    // Directory path where the Student Grade reports can be found
    private String labRubricPath;       // Directory path where the Lab Questions can be found

    private String cppCompilerPath;     // Path to the g++ compiler - Only needed for Windows, will default to g++ for linux
    private String unitTestExeName;     // Test file name.  Lab.exe for Windows or a.out for Linux
    private String languageExt;         // Extension of the students work.  This is cpp for me, but  could be java or py, etc
    private String compilerPreference;  // Whether to compile for Running, Unit Testing, or both

    private boolean autoSanitize;       // Will automatically perform sanitize action without prompting the user
    private boolean canAutoContinue;    // Will automatically go to the next lab after one is graded
    private boolean runTestOnSwitch;    // will run the test as soon as the next (or previous) lab is loaded
    private boolean debugMode;          // Will allow debug messages to print

    private int labNumber;              // The current Lab number
    private String inputFile;           // The name of the input file to be copied into each submission folder

    public ConfigManager() {
        studentListPath = "";
        studentInputPath = "./Inputs/";
        studentGradePath = "./Grades/";
        labRubricPath= "./Resources/rubrics/";

        unitTestExeName = "lab.exe";
        languageExt = ".cpp";
        cppCompilerPath = "g++";
        compilerPreference = "UnitTesting";

        autoSanitize = false;
        canAutoContinue = false;
        runTestOnSwitch = false;
        debugMode = false;

        labNumber = 1;
        inputFile = "";
    }

    public String toString() {
        return "studentListPath = " + studentListPath + "\n"
                + "studentInputPath = " + studentInputPath + "\n"
                + "studentGradePath = " + studentGradePath + "\n"
                + "labRubricPath = " + labRubricPath + "\n"
                + "cppCompilerPath = " + cppCompilerPath + "\n"
                + "compilerPreference = " + compilerPreference + "\n"
                + "unitTestExeName = " + unitTestExeName + "\n"
                + "languageExt = " + languageExt + "\n"
                + "autoSanitize = " + autoSanitize + "\n"
                + "canAutoContinue = " + canAutoContinue + "\n"
                + "runTestOnSwitch = " + runTestOnSwitch + "\n"
                + "labNumber = " + labNumber + "\n"
                + "inputFile = " + inputFile + "\n"
                + "debugMode = " + debugMode + "\n";
    }
    public void writeConfig() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("config.ser")));
            oos.writeObject(this);
            oos.close();
        }
        catch (Exception e) {
            System.err.println("Could not write config to file!" + e);
        }
    }
    /**************************************************
     * Auto Generated Getters and Setters
     *
     */
    public String getStudentListPath() {
        return studentListPath;
    }

    public void setStudentListPath(String studentListPath) {
        this.studentListPath = studentListPath;
    }

    public String getStudentInputPath() {
        return studentInputPath;
    }

    public void setStudentInputPath(String studentInputPath) {
        this.studentInputPath = studentInputPath;
    }

    public String getStudentGradePath() {
        return studentGradePath;
    }

    public void setStudentGradePath(String studentGradePath) {
        this.studentGradePath = studentGradePath;
    }

    public String getUnitTestExeName() {
        return unitTestExeName;
    }

    public void setUnitTestExeName(String unitTestExeName) {
        this.unitTestExeName = unitTestExeName;
    }

    public boolean isCanAutoContinue() {
        return canAutoContinue;
    }

    public void setCanAutoContinue(boolean canAutoContinue) {
        this.canAutoContinue = canAutoContinue;
    }

    public boolean isRunTestOnSwitch() {
        return runTestOnSwitch;
    }

    public void setRunTestOnSwitch(boolean runTestOnSwitch) {
        this.runTestOnSwitch = runTestOnSwitch;
    }

    public int getLabNumber() {
        return labNumber;
    }

    public void setLabNumber(int labNumber) {
        this.labNumber = labNumber;
    }

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public String getLabRubricPath() {
        return labRubricPath;
    }

    public void setLabRubricPath(String labRubricPath) {
        this.labRubricPath = labRubricPath;
    }

    public boolean isAutoSanitize() {
        return autoSanitize;
    }

    public void setAutoSanitize(boolean autoSanitize) {
        this.autoSanitize = autoSanitize;
    }

    public String getLanguageExt() {
        return languageExt;
    }

    public void setLanguageExt(String languageExt) { this.languageExt = languageExt; }

    public String getCppCompilerPath() { return cppCompilerPath; }

    public void setCppCompilerPath(String cppCompilerPath) { this.cppCompilerPath = cppCompilerPath; }

    public String getCompilerPreference() {
        return compilerPreference;
    }

    public void setCompilerPreference(String compilerPreference) {
        this.compilerPreference = compilerPreference;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }
}
