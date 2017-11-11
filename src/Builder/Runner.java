package Builder;

import GUI.MainFrame;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Logan on 10/18/2017.
 */
public class Runner {

    private static void waitFor(Process process, int limit) {
        long start = System.currentTimeMillis();
        while (process.isAlive() && System.currentTimeMillis() - start < limit);
    }

    private static BufferedReader runExecutableProcess(String fileName) {
        BufferedReader reader = null;
        try {
            ProcessBuilder processBuilder;
            processBuilder = new ProcessBuilder(fileName);
            Process process = processBuilder.start();
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            waitFor(process, 10000);

            process.destroy();
            process.waitFor(); // wait for the process to terminate
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "I am sorry, but I could not run the process you asked me to.  Here is some more details.\n" + e);
        }
        return reader;
    }

    public static String runAndGetResults(MainFrame context, String fileName) {
        String results = "";
        try {
            BufferedReader reader = Runner.runExecutableProcess(fileName);
            results = getResults(reader);
            reader.close();
        } catch (Exception e) {
            context.displayError("I am sorry, but I could not get the output of the process like you asked me to.  " +
                    "Here is some more details.\n" + e);
        }
        return results;
    }

    private static String getResults(BufferedReader reader) throws IOException {
        String line, results = "";
        while (reader.ready() && (line = reader.readLine()) != null) {
            results += line + "\n";
        }
        return results;
    }

    // TODO: Add way to run both the file and unit tests upon request - Extra feature
    // TODO: Write compilers for things beyond C++ and add a switch that will use the right one
    // TODO: break this out to a compiler file that can compile into multiple langauges
    public static void compile(MainFrame context) {
        String[] studentList = context.getStudentList();
        String compilerPref = context.config.getCompilerPreference();
        if (!compilerPref.equals("Runnable")) {
            context.displayMessage("I am going to start the compilation step now... this is going to take a good amount of time.  " +
                    "I apologize in advance, but please just be patient, I will let you know when it's done!");
        } else {
            context.displayMessage("I am going to start the compilation step now... this might take a few moments...");
        }

        for (int i = 0; i < studentList.length; i++) {
            String labPath = context.config.getStudentInputPath() + "Lab" + context.config.getLabNumber() + "/" + studentList[i];
            switch (compilerPref) {
                case "Both":
                    compileForRunning(context, labPath);
                    compileForUnitTest(context, labPath);
                    break;
                case "UnitTest":
                    compileForUnitTest(context, labPath);
                    break;
                case "Runnable":
                    compileForRunning(context, labPath);
                    break;
                default:
                    context.displayMessage("Uh... I am not quite sure what happened but I dont think your compiler preference is valid." +
                            "I am sorry about that, I will stop trying to compile.  You should probably check this preference " +
                            "before trying to compile again...");
                    return;
            }
        }
    }

    private static void compileForRunning(MainFrame context, String labPath) {
        String labName = new File(labPath + "/lab.cpp").toString();
        String outputName = new File(labPath + "/runnable" + context.config.getUnitTestExeName()).toString();
        context.displayDebugMessage("Compiling runnable at " + labName + "\nTO\n" + outputName);
        ProcessBuilder proc = new ProcessBuilder(context.config.getCppCompilerPath(), labName, "-o", outputName);
        if(!runProcess(context, proc)) {
            context.displayError("I am sorry, but I could not compile the lab document for " + labPath);
        }
    }

    public static void compileForUnitTest(MainFrame context, String labPath) {
        //g++ -std=c++14 -isystem ../../googletest/googletest/include -pthread ./lab_test.cpp  ../../libgtest.a
        String outputName = new File(labPath + "/unitTest" + context.config.getUnitTestExeName()).toString();
        String testFile = new File(labPath + "/lab_test.cpp").toString();
        context.displayDebugMessage("Compiling unit test at " + testFile + "\nTO\n" + outputName);

        String googleTestResourcePath = new File("Resources/googletest/include").getAbsolutePath();
        String libgtestPath = new File("Resources/libgtest.a").getAbsolutePath();

        ProcessBuilder proc = new ProcessBuilder(context.config.getCppCompilerPath(), "-std=c++14", "-isystem",
                googleTestResourcePath, "-pthread",
                testFile,
                libgtestPath,
                 "-o", outputName);
        proc.directory(new File(labPath));
        if(!runProcess(context, proc)) {
            context.displayError("I am sorry, but I could not compile unit tests for " + labPath);
        }
    }

    private static boolean runProcess(MainFrame context, ProcessBuilder compileProcess) {
        try {
            compileProcess.directory(new File("./"));
            Process process = compileProcess.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            waitFor(process, 10000);

            process.destroy();
            process.waitFor(); // wait for the process to terminate

            String errors = getResults(reader);
            if(!errors.isEmpty()) {
                context.displayError("I found some errors while compiling... here's what happened:\n\n" + errors);
            }
            reader.close();
            return true;
        } catch (Exception e) {
            context.displayError("I am sorry, but something went really wrong while trying to compile that file for you.  " +
                    "Here are some more details:\n" + e);
            return false;
        }
    }
}
