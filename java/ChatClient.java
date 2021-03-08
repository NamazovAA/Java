import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;



public class ChatClient extends JFrame {
    private JTextArea textArea;
    private JTextField textField;
    private final static String IP_ADDRESS = "localhost";
    private final static  int SERVER_PORT = 8181;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    public ChatClient (){
        try {
            connection(); // ПРОБЛЕМА С КОННЕКТОМ. НЕ ПОДКЛЮЧАЕТСЯ КЛИЕНТ
        } catch (IOException ignored) {
        }
        prepareGUI();
    }

    private void connection () throws IOException {

        socket = new Socket(IP_ADDRESS, SERVER_PORT);
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
        new Thread(() -> {
            while (true) {
                try {
                    String serverMessage = dis.readUTF();
                    if(serverMessage.equalsIgnoreCase("/q")){
                        break;
                    }
                    textArea.append(serverMessage + "\n");
                } catch (IOException ignored) {
                }
            }
            closeConnection();
        }).start();

    }
    private void sendMessageToServer(){
        if (!textField.getText().trim().isEmpty()){
            try {
                String messageToServer = textField.getText();
                dos.writeUTF(textField.getText());
                textArea.append(messageToServer + "\n");
                textField.setText("");
            } catch (IOException e) {
            }
        }
    }
    public void prepareGUI() {

        JFrame frame = new JFrame();
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setTitle("WhatsApp на минималках");
        JButton submit = new JButton("Send");
        JPanel buttonP = new JPanel();
        JPanel pnl = new JPanel();

//      pnl.setBackground(Color.BLACK);
        buttonP.setBounds(330, 230, 55, 30);
        buttonP.add(submit);

        JTextField textField = new JTextField();
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        pnl.setLayout(new BorderLayout());
        pnl.setBounds(0, 0, 330, 260);

        pnl.add(textArea, BorderLayout.CENTER);
        pnl.add(textField, BorderLayout.SOUTH);

        frame.add(pnl);
        frame.add(buttonP);
        frame.setVisible(true);

        ActionListener listener = e -> {
            if (!textField.getText().isEmpty()) {
                System.out.println("Вы: " + textField.getText());
                textArea.setText(textArea.getText().concat(textField.getText()).concat("\n"));
                textField.setText("");
            }
        };
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    dos.writeUTF("/q");
                } catch (IOException ignored) {
                }
            }
        });
        textField.addActionListener(listener);
        submit.addActionListener(listener);

        submit.addActionListener(e ->{
            sendMessageToServer(); // ПРОБЛЕМА С ОТПРАВКОЙ СООБЩЕНИЯ НА СЕРВЕР.
        });
        textField.addActionListener(e ->{
            sendMessageToServer(); // ПРОБЛЕМА С ОТПРАВКОЙ СООБЩЕНИЯ НА СЕРВЕР.
        });
    }
    private void closeConnection () {
        try {
            dis.close();
        } catch (IOException e) {
        }
        try {
            dos.flush();
        } catch (IOException e) {
        }

        try {
            dos.close();
        } catch (IOException e) {
        }
        try {
            socket.close();
        } catch (IOException e) {
        }

    }
}

