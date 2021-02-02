package Server;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private static final int PORT = 455;
    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);

            ServerSocket serverSocket = new ServerSocket(PORT);
            Socket socket = serverSocket.accept();
            System.out.println("Socket:" + socket);

            socket.getOutputStream().flush();

            while (true) {
                byte[] reader = new byte[4096];
                socket.getInputStream().read(reader);

                ByteArrayInputStream byteIs = new ByteArrayInputStream(reader);
                ObjectInputStream ObjIs = new ObjectInputStream(byteIs);

                String comeData = (String) ObjIs.readObject();
                System.out.println("Client say: " + comeData);

                ByteArrayOutputStream byteOs = new ByteArrayOutputStream(4096);
                ObjectOutputStream ObjOs = new ObjectOutputStream(byteOs);

                String message = new String(sc.nextLine());
                ObjOs.writeObject(message);
                socket.getOutputStream().write(byteOs.toByteArray());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
