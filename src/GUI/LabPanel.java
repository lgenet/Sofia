package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created by Logan on 10/15/2017.
 */
public class LabPanel extends JPanel {
    private JTextArea labView;

    public LabPanel(){
        super();
        init();
    }
    private void init(){
        labView = new JTextArea("I am the lab document");
//        labView.setColumns(75);
        labView.setFont(labView.getFont().deriveFont(14f)); // will only change size to 12pt
        labView.setBorder(new EmptyBorder(10, 10, 10, 10));

        setLabView("");
        JScrollPane js = new JScrollPane(labView);

        this.setLayout(new BorderLayout());
        this.add(new JLabel("Lab Window"), BorderLayout.NORTH);
        this.add(js, BorderLayout.CENTER);
//        this.setSize(400,800);
    }

    public void setLabView(String lab) {
        labView.setText(lab);
    }
}
