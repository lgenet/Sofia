import Builder.FileLoader;
import GUI.MainFrame;
import Lab.LabLoader;
import Lab.LabManager;
import SofiaCore.ConfigManager;

import javax.swing.*;
import java.io.*;

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

    // TODO: build tab view that will show all lab files not just the first one
    public static void main(String args[]) {
        ConfigManager cm = getConfig();
        String[] studentList = {"logan", "kristy", "addison"};

        if(!cm.getStudentListPath().equals("")) {
            studentList = FileLoader.loadStudentList(new File(cm.getStudentListPath()));
        }
        LabManager manager = new LabManager(studentList, 0);

        mf = new MainFrame(manager, cm);
        try {
            String welcome = LabLoader.readFile("./Resources/welcomeMessage.txt");
            System.out.println(welcome);
        }
        catch(Exception e) {
            System.out.println("Good day my name is Sofia, and I am here to be you guide.");
        }

        // TODO: Prompt before starting grade
        // TODO: Make run unit tests and auto apply those graded points
    }
}
