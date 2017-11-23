package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Logan on 10/15/2017.
 */
public class QuestionPane extends JPanel {
    private JTextArea gradeView;
    private JTextArea inputView;

    public QuestionPane(){
        super();
        init();
    }
    private void init(){
        gradeView = new JTextArea("I am the Question List");
        gradeView.setFont(gradeView.getFont().deriveFont(14f)); // will only change size to 12pt
        gradeView.setBorder(new EmptyBorder(10, 10, 10, 10));
        DefaultCaret caret = (DefaultCaret)gradeView.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        setText("");
        JScrollPane js = new JScrollPane(gradeView);
        JPanel displayArea = new JPanel();
        displayArea.setLayout(new BorderLayout());
        displayArea.add(new JLabel("Grade Window"), BorderLayout.NORTH);
        displayArea.add(js, BorderLayout.CENTER);

        JPanel inputArea = new JPanel();
        inputView = new JTextArea();
        inputView.setLineWrap(true);
        inputArea.setLayout(new BorderLayout());
        inputArea.add(new JLabel("Input"), BorderLayout.NORTH);
        inputArea.add(new JScrollPane(inputView), BorderLayout.CENTER);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(displayArea);
        this.add(inputArea);
    }

    /*****************************
     * Get Input Functions
     *
     * Used to get user input from the input box and return it to the caller for use
     * @return User Input from input box
     */
    public String getInput() {
        String s = "";
        try {
            s = getInputWorker();
        }
        catch(InterruptedException e){
            System.out.println("Error getting input: " + e);
        }
        return s.trim();
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
        String value = inputView.getText();
        inputView.setCaretPosition(0);
        inputView.setText("");
        return value;
    }

    /*****************************
     * Update Question Pane Actions
     *
     */
    public void setText(String text) {
        gradeView.setText(text);
    }
    public void clearScreen() { gradeView.setText("");}
    public void appendText(String text) { gradeView.setText(gradeView.getText() + text);}
}
