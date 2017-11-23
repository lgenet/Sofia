package GUI.Menu;

import GUI.MainFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Logan on 10/16/2017.
 */
public class MainMenuBar extends MenuBar{
    MainFrame context;
    final JFileChooser fc = new JFileChooser();

    public MainMenuBar(MainFrame jf) {
        super();
        context = jf;
        this.add(new FileMenu(context));
        this.add(new ConfigMenu(context));
        this.add(new ReportMenu(context));
        this.add(new HelpMenu(context));
    }
}
