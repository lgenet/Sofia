import Builder.FileLoader;
import GUI.MainFrame;
import Lab.LabLoader;
import Lab.LabManager;
import SofiaCore.ConfigManager;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

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

        mf = new MainFrame(manager, cm);
        mf.rebuildLabs();

        try {
            String welcome = LabLoader.readFile("./Resources/welcomeMessage.txt");
            mf.setGradingScreenText(welcome);
        }
        catch(Exception e) {
            mf.setGradingScreenText("Good day my name is Sofia, and I am here to be you guide.");
        }

        // TODO: Update extractor to gather loose files and bucket thsoe as well (read student name)
        // TODO: Understand why multiple failures prevents print and causes time out (Jessica S is an example in lab 9)
        // TODO: Ask to continue if unit tests fail
        // TODO: add comment box separate so you can write ideas as you get them
        // TODO: Flag for regrade/Submission verification/ETC
        // TODO: Graphical representation of how far we are (ie who is graded, how many are left
        // TODO: additional lab sanitization ie sortDec to sortDes
        // TODO: Make grade window un-editable so I can not accidentally change shit
        // TODO: Add progress bar for compiler
        // TODO: Fix grade printouts so that they will update during the current run of the system.  Such that if you regrade a lab and then go back to it the loaded/shown grade file will be the one you just did and not the previous one.  It looks like it is just printing the results to a file and not updating the in memory reference
        // TODO: build tab view that will show all lab files not just the first one
    }
}
