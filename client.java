import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class ServerExample {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(4444);
        System.out.println("Server is listening on port 4444");

        Socket clientSocket = serverSocket.accept();
        DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
        dataOutputStream.writeUTF("Hello, Client!");
        dataOutputStream.flush();
        clientSocket.close();
        serverSocket.close();
    }
}
