package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class StatusBar extends JLabel {

    /** Creates a new instance of StatusBar */
    public StatusBar() {
        super();
        setBorder(new EmptyBorder(0,10,3,10));
        setMessage("Ready");
    }

    public void setMessage(String message) {
        setText(" "+message);
    }
}