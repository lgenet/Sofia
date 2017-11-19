package GUI;

import GUI.Menu.MainMenuBar;
import SofiaCore.ConfigManager;
import Lab.LabManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * Created by Logan on 10/15/2017.
 */
public class MainFrame extends JFrame {
// NOTES!  UNDO CHANGES TO THIS FILE TO PUT BACK IT WORKING STATE.
    // CURRENTLY UNDER MAINTENANCE TO REPLACE GRADE pNAE WITH SCALAbLE SOlUTION
    private ConsolePanel consolePane; // Will eventually be another toggleable frame
    private QuestionPane questionPane;
    private LabPanel labPane;
    private LabManager manager;

    public ConfigManager config;

    private final JFileChooser fc = new JFileChooser();

    public MainFrame(LabManager m, ConfigManager c) {
        super();
        config = c;
        manager = m;
        manager.setContext(this);
        init();
    }

    private JPanel getButtonPane() {
        JPanel buttonPane = new JPanel(new GridLayout(1,3,10,10));
        buttonPane.setBorder(new EmptyBorder(15, 10, 0, 10));

        GradeButton gradeLab = new GradeButton(manager);
        NextButton nextLab = new NextButton(manager);
        PreviousButton previousLab = new PreviousButton(manager);

        buttonPane.add(previousLab);
        buttonPane.add(gradeLab);
        buttonPane.add(nextLab);

        return buttonPane;
    }

    private void init() {
        setMenuBar(new MainMenuBar(this));
//        consolePane = new ConsolePanel();
        questionPane = new QuestionPane();
        labPane = new LabPanel();
        JPanel mainPane = new JPanel(new BorderLayout());
        JPanel centralPane = new JPanel(new GridLayout(1,2,10,10));
        centralPane.add(labPane);
        centralPane.add(questionPane);
//        centralPane.add(consolePane);

        mainPane.add(centralPane, BorderLayout.CENTER);
        mainPane.add(getButtonPane(), BorderLayout.SOUTH);

        mainPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                shutDown();
            }
        });
        this.add(mainPane, BorderLayout.CENTER);
        this.setTitle("Sofia");
        this.pack();
        this.setVisible(true);
        this.setSize(1200, 800);
    }
    // Shut down function for the window
    public void shutDown() {
        config.writeConfig();
        System.exit(0);
    }

    /***********************************
     * Methods for interacting with the file chooser
     * @return
     */

    // @deprecated
    public JFileChooser getFileChooser() {
        return fc;
    }

    public String getPath(boolean directoryOnly) {
        fc.setCurrentDirectory(new File("."));

        if(directoryOnly) {
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        }
        else {
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        }

        int returnVal = fc.showOpenDialog(fc);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile().getPath();
        }
        return "";
    }

    /**********************************
     * Methods for interacting with the Question Pane
     */
    public void clearGradingScreen() {
        questionPane.clearScreen();
    }
    public void setGradingScreenText(String t) {
        clearGradingScreen();
        questionPane.setText(t);
    }
    public void appendGradingScreenTextSameLine(String t) {
        if(questionPane == null) {
            System.out.println(t);
        }
        else {
            questionPane.appendText(t);
        }
    }
    public void appendGradingScreenTextSameLine(Object o) {
        appendGradingScreenText(o.toString());
    }
    public void appendGradingScreenText(String t) {
        appendGradingScreenTextSameLine(t + "\n");
    }
    public void appendGradingScreenText(Object o) {
        appendGradingScreenTextSameLine(o.toString() + "\n");
    }

    // Method for Interacting with the Lab Pane
    public void loadLab(String lab) {
        labPane.setLabView(lab);
    }

    public String[] getStudentList () { return manager.getStudentList(); }
    public void setStudentList(String[] list) {
        manager.setStudentList(list);
    }
    public void rebuildLabs() {
        manager.rebuildLabs();
    }

    /******************************
     * Methods for displaying Messages to the user
     * @param message
     */
    public void displayError(String message) {
        JOptionPane.showMessageDialog(null, message, "Sofia - Error", JOptionPane.ERROR_MESSAGE);
    }
    public void displayError(String message, String title){
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public void displayMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Sofia", JOptionPane.INFORMATION_MESSAGE);
    }
    public void displayMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    public void displayDebugMessage(String message) {
        if(this.config.isDebugMode()){
            JOptionPane.showMessageDialog(null, "DEBUG: " + message, "DEBUG - SOFIA - DEBUG", JOptionPane.WARNING_MESSAGE);
        }
    }
    public boolean confirm(String message) {
        int choice = JOptionPane.showConfirmDialog(null, message,"Sofia - Question", JOptionPane.YES_NO_OPTION);
        return choice == JOptionPane.YES_OPTION;
    }
    public String getInputMessage(String message) {
        return JOptionPane.showInputDialog(null, message, "Sofia - Question", JOptionPane.QUESTION_MESSAGE);
    }
    public String getInput() {
        return questionPane.getInput();
    }
}
