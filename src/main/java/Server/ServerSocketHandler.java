package Server;

import DB.DB;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerSocketHandler {
    private static final int PORT = 7;
    public static HashMap<Integer, Socket> socketList = new HashMap<>();
    public static HashMap<Integer, ServerWorkComponent> componentList = new HashMap<>();
    public static DB db = new DB();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);

            while (!serverSocket.isClosed()) {
                if (componentList.size() == 10) {
                    updateComponentList();
                }

                Socket socket = serverSocket.accept();
                socketList.put(socket.getPort(), socket);
                System.out.println("Socket:" + socket);

                ServerWorkComponent component = new ServerWorkComponent(socket);
                componentList.put(socket.getPort(), component);
                System.out.println(componentList);

                component.start();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateComponentList() {
        List<Integer> temporarySocketPort = new ArrayList<>(componentList.keySet());

        for (Integer port : temporarySocketPort) {
            if (!db.isContains(port)) {
                componentList.remove(port);
            }
        }
    }
}
