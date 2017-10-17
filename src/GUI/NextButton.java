package GUI;

import Lab.LabManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Logan on 10/15/2017.
 */
public class NextButton extends JButton {
    private CountDownLatch latch;
    private LabManager subscriber;

    public NextButton (LabManager m) {
        super();
        init();
        subscriber = m;
    }

    private void init() {
        latch = new CountDownLatch(1);
        this.setText("Next >>");
        this.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    latch.countDown();
                    subscriber.receivedNextEvent();
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        });
    }
}
