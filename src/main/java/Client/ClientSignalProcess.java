package Client;

import Client.Processor.UpdateAccountSignalProcessor;
import Client.Processor.UpdateRoomSignalProcessor;
import Client.Processor.ProcessComponent;
import Client.TransferObject.Receiving;
import Data.*;

import java.net.Socket;

public class ClientSignalProcess extends Thread {
    private Receiving receiver;
    private Socket socket;
    private ProcessComponent updateAccountSignal_processor;
    private ProcessComponent updateRoomSignal_processor;

    public ClientSignalProcess(Socket socket) {
        this.socket = socket;
        receiver = new Receiving(socket);
        updateAccountSignal_processor = new UpdateAccountSignalProcessor();
        updateRoomSignal_processor = new UpdateRoomSignalProcessor();
    }

    @Override
    public void run() {
        try {
            while (!socket.isClosed()) {
                Data data = receiver.receive();
                if (data instanceof LoginSignal || data instanceof LogoutSignal) {
                    updateAccountSignal_processor.process(data);

                    if (data instanceof LogoutSignal && socket.getLocalPort() == data.getSenderPORT()) {
                        socket.close();
                    }
                } else if (data instanceof CreateRoomSignal || data instanceof QuitRoomSignal) {
                    updateRoomSignal_processor.process(data);
                }
            }
            System.out.println("Close socket" + socket);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
