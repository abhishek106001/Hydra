import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Mathi extends Frame implements Runnable, ActionListener, KeyListener {
    TextField textField;
    TextArea textArea;
    Button send;
    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    Thread chatThread;

    Mathi() {
        textField = new TextField(30);
        textArea = new TextArea(20, 40);
        send = new Button("Send");

        send.addActionListener(this);
        textField.addKeyListener(this);

        textArea.setEditable(false);
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 14));

        send.setBackground(Color.CYAN);
        send.setForeground(Color.BLACK);
        send.setFont(new Font("Arial", Font.BOLD, 16));

        setLayout(new BorderLayout());
        Panel bottomPanel = new Panel(new FlowLayout());
        bottomPanel.add(textField);
        bottomPanel.add(send);

        add(textArea, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setSize(600, 500);
        setTitle("Mathi - Chat Client");
        setVisible(true);

        try {
            socket = new Socket("localhost", 12000);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        chatThread = new Thread(this);
        chatThread.setDaemon(true);
        chatThread.start();
    }

    public void actionPerformed(ActionEvent e) {
        sendMessage();
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            sendMessage();
        }
    }

    public void keyReleased(KeyEvent e) {}

    public void keyTyped(KeyEvent e) {}

    private void sendMessage() {
        String msg = textField.getText().trim();
        if (msg.isEmpty()) return;

        textArea.append(String.format("Mathi: %s%n", msg));
        textField.setText("");

        try {   
            dataOutputStream.writeUTF(msg);
            dataOutputStream.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                String msg = dataInputStream.readUTF();
                textArea.append(String.format("Abhi: %s%n", msg));
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public static void main(String[] args) {
        new Mathi();
    }
}
