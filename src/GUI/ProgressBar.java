package GUI;

import javax.swing.*;
import java.awt.*;

public class ProgressBar extends JFrame {

    private JProgressBar pbar;
    private JPanel contentPane;
    private StatusBar title;

    private int localTotal;
    private int updates;

    static final int MY_MINIMUM = 0;

    static final int MY_MAXIMUM = 100;

    public ProgressBar() {
        super("Progress Bar");

        // Init counters
        updates = 0;
        localTotal = 0;

        // initialize Progress Bar
        pbar = new JProgressBar();
        pbar.setMinimum(MY_MINIMUM);
        pbar.setMaximum(MY_MAXIMUM);
        title = new StatusBar();

        // add to JPanel
        contentPane = new JPanel();
        contentPane.add(title, BorderLayout.NORTH);
        contentPane.add(pbar, BorderLayout.CENTER);

        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setContentPane(contentPane);
        this.pack();
        this.setSize(200, 100);
    }

    public void showModal(String t, int local) {
        title.setMessage(t);
        showModal(local);
    }
    public void showModal(int local) {
        localTotal = local;
        updates = 0;
        this.setVisible(true);
    }
    public void hideModal() {
        pbar.setValue(0);
        this.setVisible(false);
    }
    public void updateBar() {
        updates++;
        updateBar((int)((double)updates/localTotal * 100.0));
    }
    public void updateBar(int newValue) {
        pbar.setValue(newValue);
    }
}
