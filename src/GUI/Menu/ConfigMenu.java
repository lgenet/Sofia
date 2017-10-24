package GUI.Menu;

import GUI.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by Logan on 10/18/2017.
 */
public class ConfigMenu extends Menu {
    private MainFrame context;

    public ConfigMenu(MainFrame context) {
        super("Config");
        this.context = context;
        this.add(initSetLabNumber());
        this.add(initSetInputFile());
        this.add(initUnitTestExeName());
        this.add(initSetAutoContinue());
        this.add(initSetRunTestOnSwitch());
        this.add(initSetInputPaths());
        this.add(initSetGradePath());
        this.add(initSetRubricPath());
    }

    private MenuItem initSetInputPaths() {
        MenuItem setInputPath = new MenuItem("Set Student Submission Directory");
        setInputPath.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                context.config.setStudentInputPath(getPath()); // Update config to look at this new path
            }
        });
        return setInputPath;
    }
    private MenuItem initSetGradePath() {
        MenuItem setGradePath = new MenuItem("Set Student Grade Directory");
        setGradePath.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                context.config.setStudentGradePath(getPath()); // Update config to look at this new path
            }
        });
        return setGradePath;
    }
    private MenuItem initSetRubricPath() {
        MenuItem setRubricPath = new MenuItem("Set Lab Rubric Directory");
        setRubricPath.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                context.config.setLabRubricPath(getPath()); // Update config to look at this new path
            }
        });
        return setRubricPath;
    }
    private String getPath() {
        JFileChooser fc = context.getFileChooser();
        fc.setCurrentDirectory(new File("."));
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fc.showOpenDialog(fc);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile().getPath();
        }
        return "";
    }

    private MenuItem initUnitTestExeName() {
        MenuItem setExeName = new MenuItem("Set executable extension");
        setExeName.addActionListener(e -> {
            String s = JOptionPane.showInputDialog(null, "What is the name of the executable file? (lab.exe or a.out etc)");
            context.config.setUnitTestExeName(s);
        });
        return setExeName;
    }

    private MenuItem initSetAutoContinue() {
        MenuItem setAutoContinue = new MenuItem("Auto Continue On/Off");
        setAutoContinue.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                context.config.setCanAutoContinue(!context.config.isCanAutoContinue());
                if(context.config.isCanAutoContinue()) {
                    JOptionPane.showMessageDialog(null, "Okay, as soon as you finish grading a lab I will load the next one for you now.");
                }
                else {
                    JOptionPane.showMessageDialog(null, "Okay, I will wait for you to press next before moving to the next lab after grading");
                }
            }
        });

        return setAutoContinue;
    }
    private MenuItem initSetRunTestOnSwitch() {
        MenuItem setRunTestOnSwitch = new MenuItem("Run Unit test on lab next/prev");
        setRunTestOnSwitch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                context.config.setRunTestOnSwitch(!context.config.isRunTestOnSwitch());
                if(context.config.isRunTestOnSwitch()) {
                    JOptionPane.showMessageDialog(null, "I will now run the unit tests as soon as I load a lab. " +
                            "I will still wait for you to press 'grade' before grading it though.");
                }
                else {
                    JOptionPane.showMessageDialog(null, "I will no longer run the unit tests as soon as I load a lab. " +
                            "I will wait for you to press the grade button");
                }
            }
        });

        return setRunTestOnSwitch;
    }

    private MenuItem initSetLabNumber() {
        MenuItem setLabNumber = new MenuItem("Set lab number");
        setLabNumber.addActionListener(e -> {
            try {
                String s = JOptionPane.showInputDialog(null, "When I grow up, I will generate a message report");
                context.config.setLabNumber(Integer.parseInt(s));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to set lab number to " + ex);
            }

        });
        return setLabNumber;
    }
    private MenuItem initSetInputFile() {
        MenuItem setLabNumber = new MenuItem("Set Input File");
        setLabNumber.addActionListener(e -> {
            String s = JOptionPane.showInputDialog(null, "What input file do you want to use? (leave empty for none)");
            context.config.setInputFile(s);
        });
        return setLabNumber;
    }
}