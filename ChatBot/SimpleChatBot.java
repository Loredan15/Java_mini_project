import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SimpleChatBot extends JFrame implements ActionListener {

    final String TITLE_OF_PROGRAM = "Chatter: simple chatbot";
    final int START_LOCATION = 200;
    final int WINDOW_WIDTH = 350;
    final int WINDOW_HEIGHT = 450;

    JTextArea dialogue;
    JCheckBox ai;
    JTextField message;
    SimpleBot sbot;

    public static void main(String[] args) {
        new SimpleChatBot();
    }

    SimpleChatBot(){
        setTitle(TITLE_OF_PROGRAM);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(START_LOCATION, START_LOCATION, WINDOW_WIDTH, WINDOW_HEIGHT);

        dialogue = new JTextArea();
        dialogue.setLineWrap(false);
        JScrollPane scrollBar = new JScrollPane(dialogue);

        JPanel bp = new JPanel();
        bp.setLayout(new BoxLayout(bp, BoxLayout.X_AXIS));
        ai = new JCheckBox("AI");
        message = new JTextField();
        message.addActionListener(this);
        JButton enter = new JButton("Enter");
        enter.addActionListener(this);

        bp.add(ai);
        bp.add(message);
        bp.add(enter);

        add(BorderLayout.CENTER, scrollBar);
        add(BorderLayout.SOUTH, bp);

        setVisible(true);
        sbot = new SimpleBot();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(message.getText().trim().length() > 0){
            try{
                dialogue.append("Human: " + message.getText() + "\n");
                dialogue.append(TITLE_OF_PROGRAM.substring(0, 9) + sbot.sayInReturn(message.getText(), ai.isSelected()) + "\n");
            }catch (Exception e){}
        }
        message.setText("");
        message.requestFocusInWindow();
    }
}
