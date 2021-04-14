package Client;

import Data.CreateRoomSignal;
import Data.*;
import IndividualInformation.Account;
import Server.Room;
import Server.ServerManage;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

public class Main extends Application {
    public static  final int HEIGHT = 770;
    public static final int WIDTH = 1110;
    private StartPage startPage = new StartPage();
    public static InGame gameFramework = new InGame();
    public static WaitingRoom waitingRoom = new WaitingRoom();
    private ClientSocketHandler clientSocketHandler;
    public static ServerManage serverManage = new ServerManage();

    public static Account currentAccount;
    private String accountAvatarLink = "src/main/Resource/avatar/icons8_confusion_96px.png";
    public static Room currentRoom;

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
                    newPlayer.setImageIconLink(accountAvatarLink);
                    System.out.println(accountAvatarLink);

                    serverManage.insertIntoDB(newPlayer);
                    startPage.resetRegisterPage();
                    System.out.println(newPlayer);
                }
            }
        };
        startPage.getRegisterButton().addEventHandler(MouseEvent.MOUSE_CLICKED, registerClick);

        for (int i = 0; i < startPage.getAvatarMenu().getItems().size(); i++) {
            int finalI = i;
            startPage.getAvatarMenu().getItems().get(i).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    accountAvatarLink = startPage.getAvatarList().get(finalI);
                }
            });
        }

        EventHandler<MouseEvent> loginClick = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Account handle after login.
                String username = startPage.getUserNameLogin();
                try {
                    if (!username.isEmpty() && serverManage.isContains(username)) {
                        currentAccount = serverManage.getAccount(startPage.getUserNameLogin());
                        if (currentAccount != null && currentAccount.getPassword().equals(startPage.getPasswordLogin())) {
                            primaryStage.setScene(waitingRoom.getScene());
                            // Update data base
                            currentAccount.setActive(true);

                            serverManage.updateActiveState(currentAccount.getUsername(), true);
                            waitingRoom.updatePlayerTable();
                            waitingRoom.updateRoomTable();

                            // Socket handle
                            clientSocketHandler = new ClientSocketHandler();

                            serverManage.insertIntoDB(clientSocketHandler.getSocket().getLocalPort());

                            clientSocketHandler.activeSignalProcess();

                            // Send LoginSignal
                            clientSocketHandler.send(new LoginSignal(currentAccount.getUsername(),true,
                                    false, clientSocketHandler.getSocket().getLocalPort(),
                                    clientSocketHandler.getSocket().getLocalPort()));

                            // Clear content in username field and password field
                            startPage.getUsernameFieldLogin().clear();
                            startPage.getPasswordFieldLogin().clear();
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

                serverManage.delete(clientSocketHandler.getSocket().getLocalPort());
                try {
                    clientSocketHandler.send(new LogoutSignal(currentAccount.getUsername(),false,
                            false, clientSocketHandler.getSocket().getLocalPort(), clientSocketHandler.getSocket().getLocalPort()));

                    currentAccount = null;
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

                currentRoom = serverManage.createRoom(currentAccount.getUsername(),
                        currentAccount.getImageIconLink(), clientSocketHandler.getSocket().getLocalPort());
                waitingRoom.updateRoomTable();
                gameFramework.setTurn(Turn.PLAYER1_TURN);
                gameFramework.setPlayer1Avatar(currentAccount.getImageIconLink());

                try {
                    //Sending create signal
                    clientSocketHandler.send(new CreateRoomSignal(clientSocketHandler.getSocket().getLocalPort()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        waitingRoom.getCreateRoomButton().addEventHandler(MouseEvent.MOUSE_CLICKED, createRoomEvent);

        EventHandler<MouseEvent> quitMatchEvent = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setScene(waitingRoom.getScene());
                serverManage.delete(currentRoom);
                waitingRoom.updateRoomTable();
                try {
                    //Sending quit signal
                    clientSocketHandler.send(new CreateRoomSignal(clientSocketHandler.getSocket().getLocalPort()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                currentRoom = null;
            }
        };
        gameFramework.getQuitButton().addEventHandler(MouseEvent.MOUSE_CLICKED, quitMatchEvent);

        // Event handle for invite action
        waitingRoom.getPlayerTable().setRowFactory(new Callback<TableView<Account>, TableRow<Account>>() {
            @Override
            public TableRow<Account> call(TableView<Account> param) {
                TableRow<Account> row = new TableRow<>();
                ContextMenu contextMenu = new ContextMenu();

                MenuItem invite = new Menu("Invite");

                invite.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        // Code for invite request friend.
                    }
                });
                contextMenu.getItems().add(invite);
                row.contextMenuProperty().bind(
                        Bindings.when(row.emptyProperty())
                                .then((ContextMenu)null)
                                .otherwise(contextMenu)
                );
                return row;
            }
        });

        // Event handle for joint room action
        waitingRoom.getRoomTable().setRowFactory(new Callback<TableView<Room>, TableRow<Room>>() {
            @Override
            public TableRow<Room> call(TableView<Room> param) {
                TableRow<Room> row = new TableRow<>();
                ContextMenu contextMenu = new ContextMenu();

                MenuItem joinRoom = new Menu("Join room");
                joinRoom.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        // Code for join request
                        TablePosition tablePosition = waitingRoom
                                .getRoomTable()
                                .getSelectionModel()
                                .getSelectedCells()
                                .get(0);
                        int rowPosition = tablePosition.getRow();
                        Room selectedRoom = waitingRoom
                                .getRoomTable()
                                .getItems()
                                .get(rowPosition);
                        if (selectedRoom.getAmount() >= 2) {
                            // Room has enough player
                        } else {
                            try {
                                clientSocketHandler.send(
                                        new JointRoomRequest(
                                                currentAccount.getUsername(),
                                                selectedRoom.getId(),
                                                selectedRoom.getOwnerPort(),
                                                clientSocketHandler.getSocket().getLocalPort()
                                        )
                                );
                                gameFramework.setTurn(Turn.PLAYER2_TURN);
                                primaryStage.setScene(gameFramework.getScene());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

                contextMenu.getItems().add(joinRoom);
                row.contextMenuProperty().bind(
                        Bindings.when(row.emptyProperty())
                                .then((ContextMenu)null)
                                .otherwise(contextMenu)
                );
                return row;
            }
        });

        startPage.getLoginButton().addEventHandler(MouseEvent.MOUSE_CLICKED, loginClick);

        int M = gameFramework.getNumberOfRow();
        int N = gameFramework.getNumberOfColumn();
        EventHandler<MouseEvent>[][] mouseClickEvent = new EventHandler[M][N];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                int finalI = i;
                int finalJ = j;
                mouseClickEvent[i][j] = new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        try {
                            if (gameFramework.isYourTurn()) {
                                if (gameFramework.getTurn() == Turn.PLAYER1_TURN) {
                                    // Mark X
                                    gameFramework.getBoard()[finalI][finalJ].setText("X");
                                    gameFramework.getBoard()[finalI][finalJ].setStyle("-fx-text-fill: red; -fx-font: normal bold 18px 'serif';");
                                    System.out.println(gameFramework.checkWin(finalI, finalJ, "X"));
                                } else {
                                    // Mark Y
                                    gameFramework.getBoard()[finalI][finalJ].setText("O");
                                    gameFramework.getBoard()[finalI][finalJ].setStyle("-fx-text-fill: blue; -fx-font: normal bold 18px 'serif';");
                                    System.out.println(gameFramework.checkWin(finalI, finalJ, "O"));
                                }

                                gameFramework.setPlayPermission(false);
                                gameFramework.resetStartTime();
                                clientSocketHandler.send(
                                        new PlayerTurnMessage(
                                                gameFramework.getTurn(),
                                                finalJ, finalI,
                                                currentRoom.getOpponentPort(clientSocketHandler.getSocket().getLocalPort()),
                                                clientSocketHandler.getSocket().getLocalPort()
                                        )
                                );
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                gameFramework.getBoard()[i][j].addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickEvent[i][j]);
            }
        }

        EventHandler<MouseEvent> sendMessageEvent = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    if (currentRoom != null && currentRoom.getAmount() == 2) {
                        clientSocketHandler.send(
                                new ChatMessage(
                                        gameFramework.getTypingArea().getText(),
                                        currentRoom.getOpponentUsername(currentAccount.getUsername()),
                                        currentRoom.getOpponentPort(clientSocketHandler.getSocket().getLocalPort()),
                                        currentAccount.getUsername(),
                                        clientSocketHandler.getSocket().getLocalPort())
                        );

                        Label message = new Label("[" + currentAccount.getUsername() +  "]"
                                + gameFramework.getTypingArea().getText());
                        message.setPrefWidth(240);
                        message.setAlignment(Pos.CENTER_LEFT);
                        gameFramework.getMessageView().getChildren().add(message);

                        gameFramework.getTypingArea().clear();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        gameFramework.getSendButton().addEventHandler(MouseEvent.MOUSE_CLICKED, sendMessageEvent);

        primaryStage.setWidth(WIDTH);
        primaryStage.setMaxHeight(HEIGHT);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
