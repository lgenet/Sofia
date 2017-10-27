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

    private ConsolePanel consolePane;
    private LabPanel labPane;
    private LabManager manager;

    public ConfigManager config;

    final JFileChooser fc = new JFileChooser();

    public MainFrame(LabManager m, ConfigManager c) {
        super();
        config = c;
        manager = m;
        manager.setContext(this);
        init();
    }

    private JPanel getButtonPane() {
        JPanel centerPane = new JPanel(new BorderLayout());
        centerPane.setBorder(new EmptyBorder(15, 10, 0, 10));

        GradeButton gradeLab = new GradeButton(manager);
        NextButton nextLab = new NextButton(manager);
        PreviousButton previousLab = new PreviousButton(manager);

        JPanel nextBackPane = new JPanel(new BorderLayout());
        nextBackPane.add(previousLab, BorderLayout.NORTH);
        nextBackPane.add(nextLab, BorderLayout.SOUTH);

        centerPane.add(gradeLab, BorderLayout.NORTH);
        centerPane.add(nextBackPane, BorderLayout.SOUTH);

        return centerPane;
    }

    private void init() {
        setMenuBar(new MainMenuBar(this));
        consolePane = new ConsolePanel();
        labPane = new LabPanel();
        JPanel mainPane = new JPanel(new BorderLayout());
        mainPane.add(labPane, BorderLayout.WEST);
        mainPane.add(consolePane, BorderLayout.EAST);

        mainPane.add(getButtonPane(), BorderLayout.CENTER);

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

    public void clearGradingScreen() {
        consolePane.clearScreen();
    }

    public String getInput() {
        return consolePane.getInput();
    }

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
    public void shutDown() {
        config.writeConfig();
        System.exit(0);
    }
}
