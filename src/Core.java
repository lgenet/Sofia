import GUI.MainFrame;
import Lab.LabLoader;
import Lab.LabManager;

import javax.swing.*;

/**
 * Created by Logan on 10/14/2017.
 */

public class Core extends JPanel {
    static MainFrame mf;

    public static void main(String args[]) {
        String[] studentList = {"logan", "kristy", "addison"};
        LabManager manager = new LabManager(studentList, 1, 0);

        mf = new MainFrame(manager);
        try {
            String welcome = LabLoader.readFile("welcomeMessage.txt");
            System.out.println(welcome);
        }
        catch(Exception e) {
            System.out.println("Good day my name is Sofia, and I am here to be you guide.");
        }

        // TODO: Prompt before starting grade
        // TODO: Make run unit tests and auto apply those graded points
    }
}
