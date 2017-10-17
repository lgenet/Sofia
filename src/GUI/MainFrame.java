package GUI;

import Lab.LabManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.ExpandVetoException;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Logan on 10/15/2017.
 */
public class MainFrame extends JFrame {

    ConsolePanel consolePane;
    LabPanel labPane;
    boolean autoContinue; // used to determine if grading one lab should load the next or wait for the next button to be pressed
    LabManager manager;
    final JFileChooser fc = new JFileChooser();

    public MainFrame(LabManager m) {
        super();
        autoContinue = false; // TODO: Flip to true
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
                System.exit(0);
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

    public void clearGradingScreen() {
        consolePane.clearScreen();
    }

    public String getInput() {
        return consolePane.getInput();
    }

    public void loadLab(String lab) {
        labPane.setLabView(lab);
    }

    public boolean canAutoContinue() {
        return autoContinue;
    }

    public void setAutoContinue(boolean s) {
        autoContinue = s;
    }

    public void setLabNumber(int n) {
        manager.setLabNumber(n);

    }
}
