import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    public static void main(String[] args) {
        ChatClient chatClient = new ChatClient();


        Socket socket = null;
        try (ServerSocket serverSocket = new ServerSocket(8181)) {
            System.out.println("Server start");
            socket = serverSocket.accept();
            System.out.println("Client ready");
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            while (true){
                String clientMessage = dis.readUTF();
                System.out.println(clientMessage);

                if (clientMessage.equalsIgnoreCase("/q")){
                    dos.writeUTF(clientMessage);
                    closeConnection(socket, dis, dos);
                    break;
                }
//                dos.writeUTF(clientMessage);
                dos.writeUTF("Echo" + clientMessage);

            }

        } catch (IOException ignored) {
        }

    }
    private static void closeConnection (Socket s, DataInputStream dis, DataOutputStream dos) {
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
            s.close();
        } catch (IOException e) {
        }

    }

}
