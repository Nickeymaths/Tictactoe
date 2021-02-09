package Client;

import Client.Processor.LogIOSignalProcessor;
import Data.CreateRoomSignal;
import Data.LogIOSignal;
import IndividualInformation.Account;
import Server.ServerManage;
import Server.ServerSocketHandler;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class Main extends Application {
    public static  final int HEIGHT = 720;
    public static final int WIDTH = 1080;
    private StartPage startPage = new StartPage();
    private InGame gameFramework = new InGame();
    public static WaitingRoom waitingRoom = new WaitingRoom();
    private ClientSocketHandler clientSocketHandler;
    public static ServerManage serverManage = new ServerManage();
    public static Account currentAccount;

    @Override
    public void start(Stage primaryStage) throws Exception{
        startPage.initial();
        primaryStage.setHeight(HEIGHT);
        primaryStage.setWidth(WIDTH);

        primaryStage.setScene(startPage.getLoginScene());

        EventHandler<MouseEvent> createAccountClick = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setScene(startPage.getRegisterScene());
            }
        };
        startPage.getCreateAccountButton().addEventHandler(MouseEvent.MOUSE_CLICKED, createAccountClick);

        EventHandler<MouseEvent> registerClick = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setScene(startPage.getLoginScene());
                Account newPlayer = startPage.getAccount();
                if (newPlayer != null) {
                    serverManage.insertIntoDB(newPlayer);
                    startPage.resetRegisterPage();
                    System.out.println(newPlayer);
                }
            }
        };
        startPage.getRegisterButton().addEventHandler(MouseEvent.MOUSE_CLICKED, registerClick);

        EventHandler<MouseEvent> loginClick = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Account handle after login.
                try {
                    if (serverManage.isContain(startPage.getUserNameLogin())) {
                        currentAccount = serverManage.getAccount(startPage.getUserNameLogin());
                        if (currentAccount != null && currentAccount.getPassword().equals(startPage.getPasswordLogin())) {
                            primaryStage.setScene(waitingRoom.getScene());

                            currentAccount.setActive(true);

                            serverManage.updateActiveState(currentAccount.getUsername(), true);

                            clientSocketHandler = new ClientSocketHandler();

                            clientSocketHandler.activeSignalProcess();

                            waitingRoom.updatePlayerTable();

                            // Clear content in username field and password field
                            startPage.getUsernameFieldLogin().clear();
                            startPage.getPasswordFieldLogin().clear();
                            // Send LogIOSignal
                            clientSocketHandler.send(new LogIOSignal(currentAccount.getUsername(),
                                    true, false, clientSocketHandler.getLocalPort()));
                        } else {
                            System.out.println("Incorrect username or password");
                        }
                    } else {
                        System.out.println("Incorrect username or password");
                    }

                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        };

        EventHandler<MouseEvent> logoutEvent = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setScene(startPage.getLoginScene());

                serverManage.updateActiveState(currentAccount.getUsername(), false);
                currentAccount.setActive(false);
                try {
                    clientSocketHandler.send(new LogIOSignal(currentAccount.getUsername(),
                            false, false, clientSocketHandler.getLocalPort()));
                    ServerSocketHandler.socketList.remove(clientSocketHandler.getLocalPort());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        waitingRoom.getLogoutButton().addEventHandler(MouseEvent.MOUSE_CLICKED, logoutEvent);

        EventHandler<MouseEvent> createRoomEvent = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setScene(gameFramework.getScene());

                serverManage.createRoom(currentAccount.getUsername(), clientSocketHandler.getLocalPort());
                waitingRoom.updateRoomTable();
                try {
                    //Sending create signal
                    clientSocketHandler.send(new CreateRoomSignal(serverManage.getNumberOfRoom(), serverManage.getNumberOfRoom()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        waitingRoom.getCreateRoomButton().addEventHandler(MouseEvent.MOUSE_CLICKED, createRoomEvent);

        startPage.getLoginButton().addEventHandler(MouseEvent.MOUSE_CLICKED, loginClick);

        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
