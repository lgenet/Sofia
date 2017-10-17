package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Logan on 10/16/2017.
 */
public class MainMenuBar extends MenuBar{

    Menu config;
    Menu file;
    Menu help;
    Menu report;

    MainFrame context;
    final JFileChooser fc = new JFileChooser();

    public MainMenuBar(MainFrame jf) {
        super();
        context = jf;
        buildFileMenu();
        buildConfigMenu();
        buildReportMenu();
        buildHelpMenu();
    }
    private void buildHelpMenu() {
        help = new Menu("Help");

        MenuItem about = new MenuItem("About");
        about.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               JOptionPane.showMessageDialog(null, "This is a story about Sofia");
            }
        });

        help.add(about);

        this.add(help);
    }
    private void buildConfigMenu() {
        config = new Menu("Config");

        MenuItem setLabNumber = new MenuItem("Set lab number");
        setLabNumber.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    String s = JOptionPane.showInputDialog(null, "When I grow up, I will generate a message report");
                    context.setLabNumber(Integer.parseInt(s));
                }
                catch(Exception ex) {
                    JOptionPane.showMessageDialog(null, "Failed to set lab number to " + ex);
                }

            }
        });
        MenuItem setAutoContinue = new MenuItem("Auto Continue On/Off");
        setLabNumber.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                context.setAutoContinue(!context.canAutoContinue());
            }
        });
        config.add(setAutoContinue);
        config.add(setLabNumber);

        this.add(config);
    }
    private void buildReportMenu() {
        report = new Menu("Reports");

        MenuItem generateGradeReport = new MenuItem("Generate Grade Report");
        generateGradeReport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "When I grow up, I will generate a message report");
            }
        });

        report.add(generateGradeReport);

        this.add(report);
    }
    private void buildFileMenu() {
        file = new Menu("File");

        MenuItem exit = new MenuItem("Exit");
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        MenuItem loadStudentList = new MenuItem("Load Student List");
        loadStudentList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int returnVal = fc.showOpenDialog(fc);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        File file = fc.getSelectedFile();
                        Scanner sf = new Scanner(file);
                        ArrayList<String> temp = new ArrayList<String>();
                        while (sf.hasNext())
                            temp.add(sf.nextLine());
                        // TODO: Figure out wtf to do with this data;
                        JOptionPane.showMessageDialog(null, "The data\n\n" + temp.toString());
//                        context.setStudents(temp.toArray());
                    }
                    catch(Exception ex){
                        System.out.println("Error getting student list: " + ex);
                    }
                    System.out.println("Done reading");
                }
            }
        });

        file.add(loadStudentList);
        file.add(exit);

        this.add(file);
    }
}
