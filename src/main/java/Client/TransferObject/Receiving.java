package Client.TransferObject;

import Data.Data;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Receiving {
    private Socket socket;

    public Receiving(Socket socket) {
        this.socket = socket;
    }

    public Data receive() throws IOException, ClassNotFoundException {
        byte[] reader = new byte[4096];
        socket.getInputStream().read(reader);
        ByteArrayInputStream byteIs = new ByteArrayInputStream(reader);
        ObjectInputStream ObjIs = new ObjectInputStream(byteIs);
        return (Data) ObjIs.readObject();
    }
}
