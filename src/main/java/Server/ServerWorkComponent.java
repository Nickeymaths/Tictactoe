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
    private boolean exit = false;

    @Override
    public void run() {
        try {
            socket.getOutputStream().flush();

            while (!exit && !socket.isClosed()) {
                Data comeData = receive(socket);
                send(comeData);
                if (comeData instanceof LoginSignal || comeData instanceof LogoutSignal) {
                    System.out.println(ServerSocketHandler.socketList);
                    if (comeData instanceof LogoutSignal) {
                        ServerSocketHandler.socketList.remove(comeData.getSenderPORT());
                        exit = true;
                        System.out.println("New socket list");
                        System.out.println(ServerSocketHandler.socketList);
                    }
                }
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

        if (message instanceof LoginSignal || message instanceof LogoutSignal) {
            for (Socket socket : ServerSocketHandler.socketList.values()) {
                if (!socket.isClosed()) socket.getOutputStream().write(byteOs.toByteArray());
            }
            if (message instanceof LogoutSignal) exit();
        } else if (message instanceof CreateRoomSignal || message instanceof QuitRoomSignal) {
            for (Socket socket : ServerSocketHandler.socketList.values()) {
                if (!socket.isClosed()) socket.getOutputStream().write(byteOs.toByteArray());
            }
        } else {
            ServerSocketHandler.socketList.get(message.getReceiverPORT()).getOutputStream().write(byteOs.toByteArray());
        }
    }

    public void exit() {
        exit = true;
    }

    public boolean isExit() {
        return exit;
    }
}
