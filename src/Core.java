import Builder.FileLoader;
import GUI.MainFrame;
import Lab.LabLoader;
import Lab.LabManager;
import SofiaCore.ConfigManager;

import javax.swing.*;
import java.io.*;
import java.util.Scanner;

/**
 * Created by Logan on 10/14/2017.
 */

public class Core extends JPanel {
    static MainFrame mf;

    private static ConfigManager getConfig() {
        ConfigManager ss;
        try {
            FileInputStream fin = new FileInputStream(new File("config.ser"));
            ObjectInputStream oos = new ObjectInputStream(fin);
            ss = (ConfigManager) oos.readObject();
            fin.close();
            oos.close();
        } catch (Exception e) {
            System.err.println("Could not open config file! " + e);
            ss = new ConfigManager();
        }
        return ss;
    }

    private static String getUnitTestName(String line) {
        line = line.split("Test.")[1];
        return line.split("\\s")[0];
    }
    public static void readExampleUnitFile() {
        File f = new File("Resources/test.out");
        try {
            Scanner scan = new Scanner(new FileInputStream(f));
            while(scan.hasNext()) {
                String line = scan.nextLine();
                if(line.contains("[       OK ]")) {
                    line = getUnitTestName(line);
                    System.out.println(line);
                }
                else if(line.contains("[  FAILED  ]") && line.contains(".")) {
                    line = getUnitTestName(line);
                    System.err.println(line);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    // TODO: build tab view that will show all lab files not just the first one
    public static void main(String args[]) {
        ConfigManager cm = getConfig();
        String[] studentList = {"logan", "kristy", "jane doe", "addison"};

        if(cm.isDebugMode()){
            JOptionPane.showMessageDialog(null, "You are currently running a debug build.");
        }
        if(!cm.getStudentListPath().equals("")) {
            studentList = FileLoader.loadStudentList(new File(cm.getStudentListPath()));
        }
        LabManager manager = new LabManager(studentList, 0);

        readExampleUnitFile();
        mf = new MainFrame(manager, cm);
        try {
            String welcome = LabLoader.readFile("./Resources/welcomeMessage.txt");
            mf.setGradingScreenText(welcome);
        }
        catch(Exception e) {
            mf.setGradingScreenText("Good day my name is Sofia, and I am here to be you guide.");
        }

        // TODO: Make run unit tests and auto apply those graded points
    }
}
