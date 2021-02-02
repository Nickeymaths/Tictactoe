package Server;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ServerComponent extends Thread {
    private Socket socket;
    private ObjectInputStream is;
    private ObjectOutputStream os;

    public ServerComponent(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            os = new ObjectOutputStream(socket.getOutputStream());
            os.flush();
            is = new ObjectInputStream(socket.getInputStream());

            while (socket.isConnected()) {
                String message = (String) is.readObject();
                System.out.println("Client say: " + message);
                String response = message;
                os.writeObject(response);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
