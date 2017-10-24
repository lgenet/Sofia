package GUI.Menu;

import Builder.Extractor;
import GUI.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by Logan on 10/18/2017.
 */
public class HelpMenu extends Menu{
    private MainFrame context;

    public HelpMenu(MainFrame mf) {
        super("Help");
        context = mf;

        this.add(initAbout());
        this.add(initShowConfig());
        this.add(initShowExtractorErrors());
    }
    private MenuItem initAbout() {
        MenuItem about = new MenuItem("About");
        about.addActionListener(e -> JOptionPane.showMessageDialog(null, "This is a story about Sofia"));
        return about;
    }
    private MenuItem initShowExtractorErrors() {
        MenuItem extractorErrors = new MenuItem("Show Extractor Error Log");

        extractorErrors.addActionListener(e -> {
            String errorLog = "==============================\n";
            try {
                Scanner scan = new Scanner(new File(Extractor.getLogFilePath()));
                while(scan.hasNextLine()) {
                    errorLog += scan.nextLine() + "\n";
                }
            } catch (FileNotFoundException fnfe) {
                context.displayError("I am sooo sorry!  But I could not find the error log from the last extraction.  This might be why.\n" + fnfe,
                        "Error reading logs");
                return;
            }
            JOptionPane.showMessageDialog(null, "Sure thing these are my notes on issues from the last extraction process.\n" + errorLog);
        });
        return extractorErrors;
    }
    private MenuItem initShowConfig() {
        MenuItem showConfig = new MenuItem("Show Config");
        showConfig.addActionListener(e -> JOptionPane.showMessageDialog(null, "Sure thing, this is what I have in mind.  Does this look right to you?\n\n" + context.config));
        return showConfig;
    }
}
