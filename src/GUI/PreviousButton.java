package GUI;

import Lab.LabManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Logan on 10/15/2017.
 */
public class PreviousButton extends JButton {
    private CountDownLatch latch;
    private LabManager subscriber;

    public PreviousButton(LabManager m) {
        super();
        init();
        subscriber = m;
    }

    private void init() {
        latch = new CountDownLatch(1);
        this.setText("<< Previous");
        this.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    latch.countDown();
                    subscriber.receivedPreviousEvent();
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        });
    }
}
