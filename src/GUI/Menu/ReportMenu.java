package GUI.Menu;

import GUI.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Logan on 10/18/2017.
 */
public class ReportMenu extends Menu {
    private MainFrame context;

    public ReportMenu (MainFrame mf) {
        super("Reports");
        context = mf;
        this.add(initGenGradeReport());
    }

    private MenuItem initGenGradeReport() {
        MenuItem generateGradeReport = new MenuItem("Generate Grade Report");
        generateGradeReport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "When I grow up, I will generate a message report");
            }
        });
        return generateGradeReport;
    }
}
