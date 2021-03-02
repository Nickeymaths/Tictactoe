package Client;

import Client.Processor.ProcessComponent;
import Client.TransferObject.*;
import Data.Data;
import Data.*;

import java.io.IOException;
import java.net.Socket;

public class ClientSocketHandler {
    private static final String HOST = "localhost";
    private static final int PORT = 7;
    private Socket socket;
    private Sending clientSendingObj;
    private Receiving clientReceivingObj;
    private ClientSignalProcess signalProcess;

    public ClientSocketHandler() {
        try {
            socket = new Socket(HOST, PORT);
            socket.getOutputStream().flush();

            clientSendingObj = new Sending(socket);
            clientReceivingObj = new Receiving(socket);

            signalProcess = new ClientSignalProcess(socket);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void activeSignalProcess() {
        signalProcess.start();
    }

    public void send(Data message) throws IOException {
        clientSendingObj.send(message);
    }

    public Data receive() throws ClassNotFoundException, IOException {
        Data message =  clientReceivingObj.receive();
        System.out.println(message);
        return message;
    }

    public Socket getSocket() {
        return socket;
    }
}
