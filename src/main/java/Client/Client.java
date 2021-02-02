package Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 455;
    private static boolean hasNewTurn = true;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            Socket socket = new Socket(HOST, PORT);

            socket.getOutputStream().flush();

            while (true) {
                if (hasNewTurn) {
                    String message = new String(sc.nextLine());

                    ByteArrayOutputStream byteOs = new ByteArrayOutputStream(4096);
                    ObjectOutputStream ObjOs = new ObjectOutputStream(byteOs);
                    ObjOs.writeObject(message);
                    socket.getOutputStream().write(byteOs.toByteArray());
                }
                if (socket.getInputStream().available() != 0) {
                    byte[] reader = new byte[4096];
                    socket.getInputStream().read(reader);
                    ByteArrayInputStream byteIs = new ByteArrayInputStream(reader);
                    ObjectInputStream ObjIs = new ObjectInputStream(byteIs);
                    String comeData = (String) ObjIs.readObject();
                    System.out.println("Server say: " + comeData);
                    hasNewTurn = true;
                } else {
                    hasNewTurn = false;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
