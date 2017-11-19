package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.PrintStream;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Logan on 10/15/2017.
 */
// TODO: Refactor this so that it functions more like the Lab Display Pane
public class ConsolePanel extends JPanel{
    private JTextField input;
    private JTextArea ta;
    public ConsolePanel() {
        super();
        init();
    }
    private void init() {
        ta = new JTextArea();
        ta.setFont(ta.getFont().deriveFont(14f)); // will only change size to 12pt

        TextAreaOutputStream taos = new TextAreaOutputStream( ta, 60 );
        PrintStream ps = new PrintStream( taos );
        System.setOut(ps);
        System.setErr(ps);

        JScrollPane main = new JScrollPane(ta);
        ta.setBorder(new EmptyBorder(10, 10, 10, 10));

        input = new JTextField();
        input.setColumns(50);

        this.setLayout(new BorderLayout());
        this.add(new JLabel("Grade Output"), BorderLayout.NORTH);
        this.add(main, BorderLayout.CENTER);
        this.add(input, BorderLayout.SOUTH);
        this.setSize(800, 600);
    }
    public void clearScreen() {
        ta.setText("");
    }
    public String getInput() {
        String s = "";
        try {
            s = getInputWorker();
        }
        catch(InterruptedException e){
            System.out.println("Error getting input: " + e);
        }
        return s;
    }
    private String getInputWorker() throws InterruptedException
    {
        final CountDownLatch latch = new CountDownLatch(2);
        KeyEventDispatcher dispatcher = e -> {
            if (e.getKeyCode() == KeyEvent.VK_ENTER)
                latch.countDown();
            return false;
        };
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(dispatcher);
        latch.await();  // current thread waits here until countDown() is called
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(dispatcher);
        String value = input.getText();
        input.setCaretPosition(0);
        input.setText("");
        return value;
    }
}
