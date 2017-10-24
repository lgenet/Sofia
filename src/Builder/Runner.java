package Builder;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

/**
 * Created by Logan on 10/18/2017.
 */
public class Runner {
    private static void printOutputSeparator(int n) {
        for(int i = 0; i < n; i++) {
            System.out.print("=");
        }
        System.out.println();
    }
    private static void waitFor(Process process, int limit) {
        long start = System.currentTimeMillis();
        while(process.isAlive() && System.currentTimeMillis() - start < limit);
    }
    private static BufferedReader runExecutableProcess (String fileName) {
        BufferedReader reader = null;
        try {
            ProcessBuilder processBuilder;
            processBuilder = new ProcessBuilder(fileName);
            Process process = processBuilder.start();
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            waitFor(process, 10000);

            process.destroy();
            process.waitFor(); // wait for the process to terminate
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null, "I am sorry, but I could not run the process you asked me to.  Here is some more details.\n" + e);
        }
        return reader;
    }
    public static String runAndGetResults(String fileName) {
        String results = "";
        try {
            BufferedReader reader = Runner.runExecutableProcess(fileName);
            String line;
            if(reader == null) {
                throw new Exception("Could not run process at " +fileName);
            }
            while ((line = reader.readLine()) != null) {
                results += line + "\n";
            }
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "I am sorry, but I could not get the output of the process like you asked me to.  " +
                    "Here is some more details.\n" + e);
        }
        return results;
    }
    public static void runAndPrint(String fileName) {
        try {
            BufferedReader reader = Runner.runExecutableProcess(fileName);
            String line;

            System.out.println("Results of Lab Run");
            printOutputSeparator(50);
            while ((line = reader.readLine()) != null) {
                System.out.println (line);
            }
            printOutputSeparator(50);
        } catch(Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    public static void compile() {
        try {
            //g++ -std=c++14 -isystem ../../googletest/googletest/include -pthread ./lab_test.cpp  ../../libgtest.a
            ProcessBuilder launcher = new ProcessBuilder("g++", "-std=c++14", "-isystem",
                    "../../googletest/googletest/include", "-pthread",
                    "lab_test.cpp", "../../libgtest.a", "-o", "unitTest");
            Process process = launcher.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            waitFor(process, 10000);

            process.destroy();
            process.waitFor(); // wait for the process to terminate
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "I am sorry, but I could compile the test file for you.  Here is some more details.\n" + e);
        }
//        return reader;
    }
}
