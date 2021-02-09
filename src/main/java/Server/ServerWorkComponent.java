package Server;

import Client.Main;
import Data.*;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ServerWorkComponent extends Thread {
    public ServerWorkComponent(Socket socket) {
        this.socket = socket;
    }

    private Socket socket;

    @Override
    public void run() {
        try {
            socket.getOutputStream().flush();

            while (socket.isConnected()) {
                Data comeData = receive(socket);
                send(comeData);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public Data receive(Socket socket) throws IOException, ClassNotFoundException {
        byte[] reader = new byte[4096];
        socket.getInputStream().read(reader);

        ByteArrayInputStream byteIs = new ByteArrayInputStream(reader);
        ObjectInputStream ObjIs = new ObjectInputStream(byteIs);

        return (Data) ObjIs.readObject();
    }

    public void send(Data message) throws IOException {
        ByteArrayOutputStream byteOs = new ByteArrayOutputStream(4096);
        ObjectOutputStream ObjOs = new ObjectOutputStream(byteOs);

        ObjOs.writeObject(message);

        if (message instanceof LogIOSignal || message instanceof CreateRoomSignal) {
            for (Socket socket : ServerSocketHandler.socketList.values()) {
                socket.getOutputStream().write(byteOs.toByteArray());
            }
        }
    }
}
