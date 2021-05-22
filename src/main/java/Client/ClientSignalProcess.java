package Client;

import Client.Processor.PlayerTurnProcessor;
import Client.Processor.UpdateAccountTableSignalProcessor;
import Client.Processor.UpdateRoomTableSignalProcessor;
import Client.Processor.ProcessComponent;
import Client.TransferObject.Receiving;
import Client.TransferObject.Sending;
import Data.*;
import IndividualInformation.Account;
import Server.Room;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

import java.awt.image.renderable.RenderableImageProducer;
import java.io.IOException;
import java.net.Socket;

public class ClientSignalProcess extends Thread {
    private Receiving receiver;
    private Sending sender;
    private Socket socket;
    private ProcessComponent updateAccountTableSignal_processor;
    private ProcessComponent updateRoomTableSignal_processor;
    private ProcessComponent playerTurn_processor;

    private Alert endGameAlert;
    private Alert requestRematchAlert;


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
                } else if (data instanceof CreateRoomSignal || data instanceof UpdateRoomSignal) {
                    updateRoomTableSignal_processor.process(data);
                } else if (data instanceof QuitRoomSignal) {
                    if (Main.gameFramework.isStarted()) {
                        Main.currentAccount.setWinMatch(Main.currentAccount.getWinMatch() + 1);
                    }
                    Room tmp = Main.serverManage.getRoom(((QuitRoomSignal) data).getRoom_id());

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (requestRematchAlert != null) requestRematchAlert.hide();
                            if (endGameAlert != null) endGameAlert.hide();

                            Main.gameFramework.stopGame();
                            Main.gameFramework.resetGameBoard();
                            Main.gameFramework.setPlayer1Avatar(Main.currentAccount.getImageIconLink());
                        }
                    });
                    Main.currentRoom = new Room(
                            tmp.getId(),
                            tmp.getAmount(),
                            tmp.getUsernameOfOwner(),
                            tmp.getAvatarOfOwner(),
                            tmp.getOwnerPort()
                    );
                    Main.serverManage.updateRoom(Main.currentRoom);

                    sender.send(new UpdateRoomSignal(Main.currentRoom.getId()));
                } else if (data instanceof JointRoomRequest) {
                    // Accept and send accept signal
                    JointRoomRequest request = (JointRoomRequest) data;

                    Main.currentRoom.setAmount(Main.currentRoom.getAmount()+1);
                    Main.currentRoom.setUsernameOfOther(request.getUsername());
                    Main.currentRoom.setOtherPort(request.getSenderPORT());
                    Main.currentRoom.addSymbol(request.getUsername(), "O");
//                    Main.gameFramework.setPlayPermission(true);

                    Account player2 = Main.serverManage.getAccount(request.getUsername());
                    Main.currentRoom.setAvatarOfOther(player2.getImageIconLink());
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Main.gameFramework.updateRoom(Main.currentRoom);
                            Main.gameFramework.startGame();
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
                            Main.gameFramework.startGame();
                        }
                    });
                } else if (data instanceof PlayerTurnMessage) {
                    playerTurn_processor.process(data);
                } else if (data instanceof ChatMessage) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Label message = new Label("[" + ((ChatMessage) data).getUsernameOfSender() + "]" + ((ChatMessage) data).getMessage());
                            message.setPrefWidth(240);
                            message.setTextFill(Paint.valueOf("#2350ab"));
                            message.setAlignment(Pos.CENTER_RIGHT);
                            Main.gameFramework.getMessageView().getChildren().add(message);
                        }
                    });
                } else if (data instanceof RequestRematchSignal) {
                    RequestRematchSignal requestRematch = (RequestRematchSignal) data;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            requestRematchAlert = new Alert(Alert.AlertType.INFORMATION);
                            requestRematchAlert.setTitle("Rematch");
                            requestRematchAlert.setContentText(requestRematch.getUsernameOfSender() + " you want to rematch ?");
                            requestRematchAlert.getButtonTypes().clear();

                            ButtonType rematchButton = new ButtonType("Rematch");
                            ButtonType cancel = new ButtonType("Cancel");
                            requestRematchAlert.getButtonTypes().addAll(rematchButton, cancel);
                            requestRematchAlert.setHeaderText(null);

                            requestRematchAlert.showAndWait().ifPresent(response->{
                                try {
                                    if (response == rematchButton) {
                                        if (endGameAlert != null) endGameAlert.hide(); endGameAlert.close();

                                        Main.gameFramework.resetGameBoard();
                                        Main.gameFramework.getUsername1().setText(Main.currentRoom.getUsernameOfOwner());
                                        Main.gameFramework.getUsername2().setText(Main.currentRoom.getUsernameOfOther());
                                        Main.gameFramework.resetStartTime();
                                        Main.gameFramework.setPlayer1Avatar(Main.currentRoom.getAvatarOfOwner());
                                        Main.gameFramework.setPlayer2Avatar(Main.currentRoom.getAvatarOfOther());

                                        Main.currentRoom.setGoFirst(requestRematch.getGoFirst());
                                        Main.currentRoom.setCurrentTurn(Main.currentRoom.getGoFirst());
                                        Main.gameFramework.stopGame();
                                        Main.gameFramework.resetStartTime();
                                        Main.gameFramework.startGame();

                                            sender.send(new RematchResponseSignal(
                                                    Main.currentAccount.getUsername(),
                                                    Main.currentRoom.getId(),
                                                    requestRematch.getSenderPORT(),
                                                    socket.getLocalPort(),
                                                    true
                                            ));
                                    } else {

                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    });
                } else if (data instanceof RematchResponseSignal) {
                    RematchResponseSignal responseSignal = (RematchResponseSignal) data;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (responseSignal.isAccept()) {
                                Main.gameFramework.resetGameBoard();

                                Main.gameFramework.getUsername1().setText(Main.currentRoom.getUsernameOfOwner());
                                Main.gameFramework.getUsername2().setText(Main.currentRoom.getUsernameOfOther());

                                Main.gameFramework.setPlayer1Avatar(Main.currentRoom.getAvatarOfOwner());
                                Main.gameFramework.setPlayer2Avatar(Main.currentRoom.getAvatarOfOther());

                                Main.currentRoom.setGoFirst(Main.currentRoom.getOpponentUsername(Main.currentRoom.getGoFirst()));
                                Main.currentRoom.setCurrentTurn(Main.currentRoom.getGoFirst());
                                Main.gameFramework.stopGame();
                                Main.gameFramework.resetStartTime();
                                Main.gameFramework.startGame();
                            } else {

                            }
                        }
                    });
                } else if (data instanceof EndGameSignal) {
                    EndGameSignal endGameSignal = (EndGameSignal) data;
                    if (endGameSignal.getWinner().equals(Main.currentAccount.getUsername())) {
                        Main.currentAccount.setWinMatch(Main.currentAccount.getWinMatch()+1);
                    } else {
                        Main.currentAccount.setWinMatch(Main.currentAccount.getLossMatch()+1);
                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Main.gameFramework.stopGame();
                            Main.gameFramework.resetStartTime();
                            Main.currentRoom.setWinner(endGameSignal.getWinner());

                            endGameAlert = new Alert(Alert.AlertType.INFORMATION);
                            endGameAlert.setTitle("End game");
                            endGameAlert.setContentText(Main.currentRoom.getWinner()
                                    + " is the winner. Do you want to rematch ?");
                            endGameAlert.getButtonTypes().clear();

                            ButtonType rematchButton = new ButtonType("Rematch");
                            ButtonType outGame = new ButtonType("Leave room");
                            endGameAlert.getButtonTypes().addAll(rematchButton, outGame, ButtonType.CANCEL);
                            endGameAlert.setHeaderText(null);

                            endGameAlert.showAndWait().ifPresent(response-> {
                                if (response == rematchButton) {
                                    try {
                                        sender.send(
                                                new RequestRematchSignal(
                                                        Main.currentAccount.getUsername(),
                                                        Main.currentRoom.getId(),
                                                        Main.currentRoom.getOpponentPort(socket.getLocalPort()),
                                                        socket.getLocalPort(),
                                                        Main.currentRoom.getOpponentUsername(Main.currentRoom.getGoFirst())
                                                )
                                        );
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else if (response == outGame) {
                                    Main.gameFramework.getQuitButton().fire();
                                }
                            });
                        }
                    });
                } else if (data instanceof DrawRequestSignal) {
                    DrawRequestSignal drawRequestSignal = (DrawRequestSignal) data;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Draw");
                            alert.setHeaderText(null);
                            alert.setContentText(drawRequestSignal.getUsernameOfSender()
                                    + "request to make draw");

                            alert.showAndWait().ifPresent(res -> {
                                try {
                                    if (res == ButtonType.OK) {
                                        Main.gameFramework.resetGameBoard();

                                        Main.gameFramework.getUsername1().setText(Main.currentRoom.getUsernameOfOwner());
                                        Main.gameFramework.getUsername1().setText(Main.currentRoom.getUsernameOfOther());

                                        Main.gameFramework.setPlayer1Avatar(Main.currentRoom.getAvatarOfOwner());
                                        Main.gameFramework.setPlayer2Avatar(Main.currentRoom.getAvatarOfOther());

                                        Main.currentRoom.setGoFirst(Main.currentRoom.getOpponentUsername(Main.currentRoom.getGoFirst()));
                                        Main.currentRoom.setCurrentTurn(Main.currentRoom.getGoFirst());

                                        Main.gameFramework.stopGame();
                                        Main.gameFramework.resetStartTime();
                                        Main.gameFramework.startGame();
                                    }
                                    sender.send(new DrawAccept(
                                            Main.currentAccount.getUsername(),
                                            res == ButtonType.OK,
                                            drawRequestSignal.getSenderPORT(),
                                            socket.getLocalPort())
                                    );
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    });
                } else if (data instanceof DrawAccept) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            DrawAccept drawAccept = (DrawAccept) data;
                            Label message = new Label("[" + drawAccept.getUsernameOfSender() +  "dennie to make a draw]"
                                    + Main.gameFramework.getTypingArea().getText());
                            message.setPrefWidth(240);
                            message.setAlignment(Pos.CENTER_LEFT);
                            Main.gameFramework.getMessageView().getChildren().add(message);

                            if (drawAccept.isAccept()) {
                                Main.gameFramework.resetGameBoard();

                                Main.gameFramework.getUsername1().setText(Main.currentRoom.getUsernameOfOwner());
                                Main.gameFramework.getUsername1().setText(Main.currentRoom.getUsernameOfOther());

                                Main.gameFramework.setPlayer1Avatar(Main.currentRoom.getAvatarOfOwner());
                                Main.gameFramework.setPlayer2Avatar(Main.currentRoom.getAvatarOfOther());

                                Main.currentRoom.setGoFirst(Main.currentRoom.getOpponentUsername(Main.currentRoom.getGoFirst()));
                                Main.currentRoom.setCurrentTurn(Main.currentRoom.getGoFirst());

                                Main.gameFramework.stopGame();
                                Main.gameFramework.resetStartTime();
                                Main.gameFramework.startGame();
                            }
                        }
                    });
                }
            }
            System.out.println("Close socket" + socket);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
