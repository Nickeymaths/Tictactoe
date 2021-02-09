package Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerSocketHandler {
    private static final int PORT = 7;
    public static HashMap<Integer, Socket> socketList = new HashMap<>();
    private static final int NUMBER_THREAD = 50;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            ExecutorService executors = Executors.newFixedThreadPool(NUMBER_THREAD);

            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("Socket:" + socket);

                ServerWorkComponent component = new ServerWorkComponent(socket);
                socketList.put(socket.getPort(), socket);
                executors.execute(component);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
