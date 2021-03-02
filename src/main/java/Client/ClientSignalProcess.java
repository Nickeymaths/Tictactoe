package Client;

import Client.Processor.PlayerTurnProcessor;
import Client.Processor.UpdateAccountTableSignalProcessor;
import Client.Processor.UpdateRoomTableSignalProcessor;
import Client.Processor.ProcessComponent;
import Client.TransferObject.Receiving;
import Client.TransferObject.Sending;
import Data.*;
import javafx.application.Platform;

import java.net.Socket;

public class ClientSignalProcess extends Thread {
    private Receiving receiver;
    private Sending sender;
    private Socket socket;
    private ProcessComponent updateAccountTableSignal_processor;
    private ProcessComponent updateRoomTableSignal_processor;
    private ProcessComponent playerTurn_processor;

    public ClientSignalProcess(Socket socket) {
        this.socket = socket;
        receiver = new Receiving(socket);
        sender = new Sending(socket);
        updateAccountTableSignal_processor = new UpdateAccountTableSignalProcessor();
        updateRoomTableSignal_processor = new UpdateRoomTableSignalProcessor();
        playerTurn_processor = new PlayerTurnProcessor();
    }

    @Override
    public void run() {
        try {
            while (!socket.isClosed()) {
                Data data = receiver.receive();
                if (data instanceof LoginSignal || data instanceof LogoutSignal) {
                    updateAccountTableSignal_processor.process(data);

                    if (data instanceof LogoutSignal && socket.getLocalPort() == data.getSenderPORT()) {
                        socket.close();
                    }
                } else if (data instanceof CreateRoomSignal || data instanceof QuitRoomSignal) {
                    updateRoomTableSignal_processor.process(data);
                } else if (data instanceof JointRoomRequest) {
                    // Accept and send accept signal
                    JointRoomRequest request = (JointRoomRequest) data;

                    Main.currentRoom.setAmount(Main.currentRoom.getAmount()+1);
                    Main.currentRoom.setUsernameOfOther(request.getUsername());
                    Main.currentRoom.setOtherPort(request.getSenderPORT());
                    Main.gameFramework.setPlayPermission(true);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Main.gameFramework.updateRoom(Main.currentRoom);
                        }
                    });
                    Main.serverManage.updateRoom(Main.currentRoom);

                    sender.send(new JointRoomAccept(request.getRoomId(), request.getSenderPORT(), socket.getLocalPort()));
                } else if (data instanceof JointRoomAccept) {
                    JointRoomAccept jointRoomAccept = (JointRoomAccept) data;

                    Main.currentRoom = Main.serverManage.getRoom(jointRoomAccept.getRoomId());
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Main.gameFramework.updateRoom(Main.currentRoom);
                        }
                    });
                } else if (data instanceof PlayerTurnMessage) {
                    playerTurn_processor.process(data);
                }
            }
            System.out.println("Close socket" + socket);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
