package GUI.Menu;

import Builder.Extractor;
import Builder.FileLoader;
import Builder.Runner;
import Sanitizer.SanitizeCpp;
import GUI.MainFrame;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

/**
 * Created by Logan on 10/18/2017.
 */
public class FileMenu extends Menu {
    private MainFrame context;

    public FileMenu(MainFrame mf) {
        super("File");
        context = mf;

        this.add(initStudentListPicker());
        this.addSeparator();
        this.add(initMasterPrepare());
        this.addSeparator();
        this.add(initExtractZip());
        this.add(initSanitizer());
        this.add(initCompiler());
        this.add(initCleanupFiles());
        this.addSeparator();
        this.add(initExitMenu());
    }

    private MenuItem initExitMenu() {
        MenuItem exit = new MenuItem("Exit");
        exit.addActionListener(e -> context.shutDown());
        return exit;
    }

    private MenuItem initStudentListPicker() {
        MenuItem loadStudentList = new MenuItem("Load Student List");
        loadStudentList.addActionListener(e -> {
            JFileChooser fc = context.getFileChooser(); // TODO: Break out to a util in MainFrame
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int returnVal = fc.showOpenDialog(fc);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = fc.getSelectedFile();
                    String[] studentList = FileLoader.loadStudentList(file);
                    context.setStudentList(studentList); // Update the in memory student list
                    context.config.setStudentListPath(file.getPath()); // Update config to look at this new path
                } catch (Exception ex) {
                    context.displayError("I am sorry, but I could not load the student list that you asked for.  Here is some more info" + ex,
                            "Error Reading Student List");
                    System.out.println("Error getting student list: " + ex);
                }
            }
        });
        return loadStudentList;
    }

    private MenuItem initMasterPrepare() {
        MenuItem master = new MenuItem("Prepare Labs");
        master.addActionListener(e -> {
            String labNumber = context.getInputMessage("What lab do you want me to prepare for you?");
            int labNum = context.config.getLabNumber();
            try{
                labNum = Integer.parseInt(labNumber);
            }catch(Exception ex) {
                context.displayDebugMessage("I am sorry, but parsing the integer |" + labNumber + "| failed.  Exception: " + ex);
                labNum = context.config.getLabNumber();
            }
            context.config.setLabNumber(labNum);
            runExtractTasks();
            context.displayMessage("Okay... now I just need to sanitize the lab documents...");
            runSanitizeTasks();
            context.displayMessage("woo, done with sanitization, now just to clean up any remaining garbage....");
            Extractor.cleanupTmpFiles();
            context.displayMessage("Okay!  Thank you for being patient with me!  I am done preparing your labs for you!");
        });
        return master;
    }
    private MenuItem initCleanupFiles() {
        MenuItem cleanUp = new MenuItem("Clean up tmp files");
        cleanUp.addActionListener(e -> {
            context.displayMessage("Oh ya... Let me see here...");
            Extractor.cleanupTmpFiles();
            context.displayMessage("All done!  I removed all the old temporary files that " +
                    "were hanging around after the last extraction.");
        });
        return cleanUp;
    }
    private MenuItem initExtractZip() {
        MenuItem extractLabMenu = new MenuItem("Extract Labs");
        extractLabMenu.addActionListener(e -> {
            runExtractTasks();
        });
        return extractLabMenu;
    }

    private MenuItem initSanitizer() {
        MenuItem sanitizeLabs = new MenuItem("Sanitize Labs");
        sanitizeLabs.addActionListener(e -> {
            if(context.config.isAutoSanitize()) {
                context.displayMessage("Okay!  I am going start sanitizing these labs... I will go ahead and make the changes." +
                        " So there is no need for you to do anything.  I will let you know when I am done.");
            }
            else {
                context.displayMessage("Okay!  I am going start sanitizing these labs... I wont make a change without asking you.");
            }
            runSanitizeTasks();
            context.displayMessage("All done!  These files should be good to go now!");
        });
        return sanitizeLabs;
    }

    private MenuItem initCompiler() {
        MenuItem compiler = new MenuItem("Compile Lab Tests");
        compiler.addActionListener(e -> {
//            context.displayMessage("So I have here that you want me to compile for: " + context.config.getCompilePreference(), but I don't know the first thing about compilers!");

            Runner.compile(context); // TODO: make config setting
            context.displayMessage("Done compiling");
        });
        return compiler;
    }

    /******************************
     * Helper Functions
     */
    private void runExtractTasks() {
        JFileChooser fc = context.getFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter fnef = new FileNameExtensionFilter("Zip Files", "zip");
        fc.setFileFilter(fnef);
        int returnVal = fc.showOpenDialog(fc);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                JOptionPane.showMessageDialog(null, "Okay, this might take a few moments... Please be patient with me! " +
                        "I will let you know when I am done extracting the Lab data");
                File selected = fc.getSelectedFile();
                fc.removeChoosableFileFilter(fnef);
                Extractor.extract(selected, context);
                JOptionPane.showMessageDialog(null, "Done extraction... now just updating my registry of labs...");
                context.rebuildLabs();
                JOptionPane.showMessageDialog(null, "Okay!  All done with the extraction and Document Building.  Sorry for the delay!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "I am sorry, but I could not extract the lab file.  Here is more info:\n" + ex,
                        "Error Extracting", JOptionPane.ERROR_MESSAGE);
            }
            Extractor.cleanupTmpFiles();
        }
    }
    private void runSanitizeTasks() {
        try {
            SanitizeCpp.sanitize(context);
        } catch (Exception e) {
            context.displayError("I am terribly sorry, but I Failed to sanitize the labs like you asked... here is some more info\n" + e);
        }
    }
}
