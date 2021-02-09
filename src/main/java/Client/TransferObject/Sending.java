package Client.TransferObject;

import Data.Data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Sending {
    private Socket socket;

    public Sending(Socket socket) {
        this.socket = socket;
    }

    public void send(Data message) throws IOException {
        ByteArrayOutputStream byteOs = new ByteArrayOutputStream(4096);
        ObjectOutputStream ObjOs = new ObjectOutputStream(byteOs);
        ObjOs.writeObject(message);
        socket.getOutputStream().write(byteOs.toByteArray());
    }
}
