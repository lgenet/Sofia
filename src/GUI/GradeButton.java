package GUI;

import Lab.LabManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Logan on 10/15/2017.
 */
public class GradeButton extends JButton {
    private LabManager subscriber;

    public GradeButton(LabManager m) {
        super();
        init();
        subscriber = m;
    }

    private void init() {
        this.setText("Grade");
        this.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Thread one = new Thread() {
                    public void run() {
                        subscriber.receivedGradeEvent();
                    }
                };

                one.start();
            }
        });
    }
}