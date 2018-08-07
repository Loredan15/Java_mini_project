import javax.swing.*;
import java.awt.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Chat extends JFrame {
    private JTextArea taMain;
    private JTextField tfMsg;
    private JButton btnSend;
    private JScrollPane spMain;

    private final String FRM_TITLE = "Our Tiny Chat";
    private final int FRM_LOC_X = 100;
    private final int FRM_LOC_Y = 100;
    private final int FRM_WIDTH = 400;
    private final int FRM_HEIGHT = 400;
    private final int PORT = 9876;
    private final String IP_BROADCAST = "127.0.0.1";



    private class thdReceiver extends Thread{
        @Override
        public void start(){
            super.start();
            try {
                customize();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        private void customize() throws Exception {
            DatagramSocket receiveSocket = new DatagramSocket(PORT);
            Pattern regex = Pattern.compile("[\u0020-\uFFFF]");

            while (true){
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                receiveSocket.receive(receivePacket);
                InetAddress IPAdress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                String sentence = new String(receivePacket.getData());
                Matcher m = regex.matcher(sentence);


                taMain.append(IPAdress.toString() + ":" + port + ": ");
                while (m.find())
                    taMain.append(sentence.substring(m.start(), m.end()));
                taMain.append("\r\n");
                taMain.setCaretPosition(taMain.getText().length());
            }
        }
    }

    private void antistatic(){
        frameDraw(new Chat());
        new thdReceiver().start();
    }

    private void bntSendHandler() throws Exception{
        DatagramSocket sendSocket = new DatagramSocket();
        InetAddress IPAdress = InetAddress.getByName(IP_BROADCAST);
        byte[] sendData;
        String sentence = tfMsg.getText();
        tfMsg.setText("");
        sendData = sentence.getBytes("UTF-8");
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAdress, PORT);
        sendSocket.send(sendPacket);
    }

    private void frameDraw(JFrame frame) {
        tfMsg = new JTextField();
        taMain = new JTextArea(FRM_HEIGHT / 19, 50);
        btnSend = new JButton();
        spMain = new JScrollPane(taMain);
        spMain.setLocation(0,0);
        //перенос слов в форме
        taMain.setLineWrap(true);
        taMain.setEnabled(false);

        btnSend.setText("Send");
        btnSend.setToolTipText("Broadcast a message");
        btnSend.addActionListener(e -> {
            try{
                bntSendHandler();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });

        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setTitle(FRM_TITLE);
        frame.setLocation(FRM_LOC_X, FRM_LOC_Y);
        frame.setSize(FRM_WIDTH, FRM_HEIGHT);
        //запрет изменять размеры формы
        frame.setResizable(false);
        frame.getContentPane().add(BorderLayout.NORTH, spMain);
        frame.getContentPane().add(BorderLayout.CENTER, tfMsg);
        frame.getContentPane().add(BorderLayout.EAST, btnSend);
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        new Chat().antistatic();

    }

}
