package Client;

import Client.Processor.ProcessComponent;
import Client.TransferObject.*;
import Data.Data;
import Data.LogIOSignal;

import java.io.IOException;
import java.net.Socket;

public class ClientSocketHandler {
    private static final String HOST = "localhost";
    private static final int PORT = 7;
    private Socket socket;
    private Sending clientSendingObj;
    private Receiving clientReceivingObj;
    private ProcessComponent processor;
    private ClientSignalProcess signalProcess;

    public ClientSocketHandler() {
        try {
            socket = new Socket(HOST, PORT);

            signalProcess = new ClientSignalProcess(socket);

            socket.getOutputStream().flush();

            clientSendingObj = new Sending(socket);
            clientReceivingObj = new Receiving(socket);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void activeSignalProcess() {
        signalProcess.start();
    }

    public void processMessage(Data message) {
        processor.process(message);
    }

    public void setProcessor(ProcessComponent processor) {
        this.processor = processor;
    }

    public void send(Data message) throws IOException {
        clientSendingObj.send(message);
    }

    public Data receive() throws ClassNotFoundException, IOException {
        Data message =  clientReceivingObj.receive();
        System.out.println(message);
        return message;
    }

    public int getLocalPort() {
        return socket.getLocalPort();
    }
}
