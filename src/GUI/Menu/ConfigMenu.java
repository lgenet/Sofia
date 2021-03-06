package GUI.Menu;

import Builder.FileLoader;
import GUI.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Logan on 10/18/2017.
 */
public class ConfigMenu extends Menu {
    private MainFrame context;

    public ConfigMenu(MainFrame context) {
        super("Config");
        this.context = context;
        this.add(initDebugMode());
        this.add(initSuppressError());
        this.addSeparator();
        this.add(initSetLabNumber());
        this.add(initSetInputFile());
        this.add(initUnitTestExeName());
        this.addSeparator();
        this.add(initSetAutoContinue());
        this.add(initSetAutoSanitize());
        this.add(initSetRunTestOnSwitch());
        this.addSeparator();
        this.add(initSetCompilerPrefs());
        this.add(initSetCPPCompiler());
        this.addSeparator();
        this.add(initSetInputPaths());
        this.add(initSetGradePath());
        this.add(initSetRubricPath());
        this.add(initLanguageExt());
        this.add(initGetLibgFile());
    }

    private MenuItem initSetInputPaths() {
        MenuItem setInputPath = new MenuItem("Set Student Submission Directory");
        setInputPath.addActionListener(e -> {
            context.config.setStudentInputPath(context.getPath(true)); // Update config to look at this new path
        });
        return setInputPath;
    }
    private MenuItem initSetGradePath() {
        MenuItem setGradePath = new MenuItem("Set Student Grade Directory");
        setGradePath.addActionListener(e -> {
            context.config.setStudentGradePath(context.getPath(true)); // Update config to look at this new path
        });
        return setGradePath;
    }
    private MenuItem initSetRubricPath() {
        MenuItem setRubricPath = new MenuItem("Set Lab Rubric Directory");
        setRubricPath.addActionListener(e -> {
            context.config.setLabRubricPath(context.getPath(true)); // Update config to look at this new path
        });
        return setRubricPath;
    }
    private MenuItem initSetCPPCompiler() {
        MenuItem cppCompilerPath = new MenuItem("Set C++ Compiler Path");
        cppCompilerPath.addActionListener(e -> {
            context.config.setCppCompilerPath(context.getPath(true) + File.separator + "g++"); // Update config to look at this new path
        });
        return cppCompilerPath;
    }


    private MenuItem initUnitTestExeName() {
        MenuItem setExeName = new MenuItem("Set executable extension");
        setExeName.addActionListener(e -> {
            String s = context.getInputMessage("What is the name of the executable file? (lab.exe or a.out etc)");
            context.config.setUnitTestExeName(s);
        });
        return setExeName;
    }
    private MenuItem initLanguageExt() {
        MenuItem setExeName = new MenuItem("Set Programming Language");
        setExeName.addActionListener(e -> {
            String s = context.getInputMessage( "What is type of file should I look for (.cpp, .java, .py, etc)");
            context.config.setLanguageExt(s);
        });
        return setExeName;
    }

    private MenuItem initSetCompilerPrefs() {
        MenuItem compilerPref = new MenuItem("Set Compiler Preference");
        compilerPref.addActionListener(e -> {
            String message = "Please select your compiler preference.  I will use this when determining how to compile your lab documents later on.";
            String[] options = new String[] {"Unit Tests Only", "Run Code Only", "Both", "Cancel"};
            int response = JOptionPane.showOptionDialog(null, message, "Sofia - Compiler Preference",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, options[0]);
            switch (response) {
                case 0:
                    context.config.setCompilerPreference("UnitTest");
                    break;
                case 1:
                    context.config.setCompilerPreference("Runnable");
                    break;
                case 2:
                    context.config.setCompilerPreference("Both");
                    break;
                case 3:
                    String curr = context.config.getCompilerPreference();
                    context.displayMessage("Oh, okay I wont change your preference!  I will continue to compile with " + curr);
                    break;
            }
        });
        return compilerPref;
    }
    private MenuItem initDebugMode() {
        MenuItem debug = new MenuItem("Enable Debug Mode");
        debug.addActionListener(e -> {
            context.config.setDebugMode(!context.config.isDebugMode());
            if(context.config.isDebugMode()){
                context.displayMessage("okay!  You are now running in debug mode!  These messages will appear slightly different... " +
                        "They also might not sound like me, but they should provide additional diagnostic info...");
            }
            else {
                context.displayMessage("Okay!  I wont show you those pesky debug messages anymore.");
            }
        });
        return debug;
    }
    private MenuItem initSuppressError() {
        MenuItem debug = new MenuItem("Ignore Error Msg");
        debug.addActionListener(e -> {
            context.config.setSuppressCompileErr(!context.config.isSuppressCompileErr());
            if(context.config.isSuppressCompileErr()){
                context.displayMessage("Okay... if you are sure about this.  I will not alert you to any errors in the compilation step!");
            }
            else {
                context.displayMessage("Okay!  I wont ignore the errors during compiling anymore.");
            }
        });
        return debug;
    }

    private MenuItem initGetLibgFile() {
        MenuItem libG = new MenuItem("Select libgtest.a file");
        libG.addActionListener(e -> {
            context.displayMessage("Okay, this one might be hard to explain but... I need a Lib gTest file to be able to run unit tests.\n" +
                    "If you would be so kind as to point me to where one is, I can record it in my memory for future use.  I am going to let you" +
                    " pick where it is in just a moment.");
            String libGPath = context.getPath(false);
            try {
                FileLoader.copyFile(libGPath, "./Resources/libgtest.a");
            } catch (IOException ex) {
                context.displayError("I am sorry!  I could not copy the libgtest file to my resource list.  Here are some more details\n" + ex);
            }
        });
        return libG;
    }
    private MenuItem initSetAutoContinue() {
        MenuItem setAutoContinue = new MenuItem("Auto Continue On/Off");
        setAutoContinue.addActionListener(e -> {
            context.config.setCanAutoContinue(!context.config.isCanAutoContinue());
            if(context.config.isCanAutoContinue()) {
                JOptionPane.showMessageDialog(null, "Okay, as soon as you finish grading a lab I will load the next one for you now.");
            }
            else {
                JOptionPane.showMessageDialog(null, "Okay, I will wait for you to press next before moving to the next lab after grading");
            }
        });

        return setAutoContinue;
    }
    private MenuItem initSetAutoSanitize() {
        MenuItem setAutoSanitize = new MenuItem("Auto Sanitize On/Off");
        setAutoSanitize.addActionListener(e -> {
            context.config.setAutoSanitize(!context.config.isAutoSanitize());
            if(context.config.isAutoSanitize()) {
                JOptionPane.showMessageDialog(null, "Okay, I will go ahead and sanitize the labs for you!  Im glad you have this much faith in me!");
            }
            else {
                JOptionPane.showMessageDialog(null, "Okay, I will not change a thing during sanitization and will ask you before every move.");
            }
        });

        return setAutoSanitize;
    }
    private MenuItem initSetRunTestOnSwitch() {
        MenuItem setRunTestOnSwitch = new MenuItem("Run Unit test on lab next/prev");
        setRunTestOnSwitch.addActionListener(e -> {
            context.config.setRunTestOnSwitch(!context.config.isRunTestOnSwitch());
            if(context.config.isRunTestOnSwitch()) {
                JOptionPane.showMessageDialog(null, "I will now run the unit tests as soon as I load a lab. " +
                        "I will still wait for you to press 'grade' before grading it though.");
            }
            else {
                JOptionPane.showMessageDialog(null, "I will no longer run the unit tests as soon as I load a lab. " +
                        "I will wait for you to press the grade button");
            }
        });

        return setRunTestOnSwitch;
    }

    private MenuItem initSetLabNumber() {
        MenuItem setLabNumber = new MenuItem("Set lab number");
        setLabNumber.addActionListener(e -> {
            try {
                String s = JOptionPane.showInputDialog(null, "Please enter the lab number you wish for me to consider.");
                context.config.setLabNumber(Integer.parseInt(s));
                context.rebuildLabs();
            } catch (Exception ex) {
                context.displayError("I am sorry but I could not set the lab number for you, here is some more info:\n" + ex);
            }

        });
        return setLabNumber;
    }
    private MenuItem initSetInputFile() {
        MenuItem setLabNumber = new MenuItem("Set Input File");
        setLabNumber.addActionListener(e -> {
            String s = context.getInputMessage("What input file do you want to use? (leave empty for none)");
            context.config.setInputFile(s);
        });
        return setLabNumber;
    }
}