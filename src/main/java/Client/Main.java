package Client;

import Data.CreateRoomSignal;
import Data.*;
import IndividualInformation.Account;
import Server.Room;
import Server.ServerManage;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;

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
    private String defaultAvatarLink = "src/main/Resource/avatar/icons8_confusion_96px.png";
    public static Room currentRoom;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        startPage.initial();
        primaryStage.setHeight(HEIGHT);
        primaryStage.setWidth(WIDTH);

        primaryStage.setScene(startPage.getLoginScene());

       events(primaryStage);

        for (int i = 0; i < startPage.getAvatarMenu().getItems().size(); i++) {
            int finalI = i;
            startPage.getAvatarMenu().getItems().get(i).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    accountAvatarLink = startPage.getAvatarList().get(finalI);
                }
            });
        }

        primaryStage.setWidth(WIDTH);
        primaryStage.setMaxHeight(HEIGHT);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void events(Stage primaryStage) {
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
                loginAction();
            }
        };
        startPage.getLoginButton().addEventHandler(MouseEvent.MOUSE_CLICKED, loginClick);

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
                currentAccount.setInMatch(true);
//                gameFramework.setTurn(Turn.PLAYER1_TURN);
                gameFramework.setPlayer1Avatar(currentAccount.getImageIconLink());
                gameFramework.getRoomIdLabel().setText(String.valueOf(currentRoom.getId()));
                gameFramework.getUsername1().setText(currentAccount.getUsername());

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
                quitMatchAction(false);
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
//                                gameFramework.setTurn(Turn.PLAYER2_TURN);
                                currentAccount.setInMatch(true);
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
                            System.out.println("Started: " + gameFramework.isStarted());
                            System.out.println("Current turn: " + currentRoom.getCurrentTurn());
                            if (gameFramework.isStarted() && currentRoom.getCurrentTurn().equals(currentAccount.getUsername())) {
                                // Mark symbol
                                gameFramework.getBoard()[finalI][finalJ].setText(currentRoom.getSymbol(currentAccount.getUsername()));
                                gameFramework.getBoard()[finalI][finalJ].setStyle(currentRoom.getColorSymbol(currentRoom.getSymbol(currentAccount.getUsername())));
                                if (gameFramework.checkWin(finalI, finalJ, currentRoom.getSymbol(currentAccount.getUsername()))) {
                                    currentAccount.setWinMatch(currentAccount.getWinMatch()+1);
                                    currentRoom.setWinner(currentAccount.getUsername());
                                    WinAction();
                                } else {

//                                gameFramework.setPlayPermission(false);
                                    currentRoom.setCurrentTurn(currentRoom.getOpponentUsername(currentAccount.getUsername()));
                                    gameFramework.resetStartTime();
                                    clientSocketHandler.send(
                                            new PlayerTurnMessage(
                                                    currentAccount.getUsername(),
                                                    finalJ, finalI,
                                                    currentRoom.getOpponentPort(clientSocketHandler.getSocket().getLocalPort()),
                                                    clientSocketHandler.getSocket().getLocalPort()
                                            )
                                    );
                                }
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
                sendMessage();
            }
        };
        gameFramework.getSendButton().addEventHandler(MouseEvent.MOUSE_CLICKED, sendMessageEvent);

        EventHandler<MouseEvent> findMatchEvent = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                List<Room> roomList = serverManage.getRoomList();

                for (Room room : roomList) {
                    if (room.getAmount() < 2) {
                        JointRoomRequest jointRoomRequest = new JointRoomRequest(
                                currentAccount.getUsername(),
                                room.getId(),
                                room.getOwnerPort(),
                                clientSocketHandler.getSocket().getLocalPort()
                        );

                        try {
                            clientSocketHandler.send(jointRoomRequest);

//                            gameFramework.setTurn(Turn.PLAYER2_TURN);
                            primaryStage.setScene(gameFramework.getScene());
                            break;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        waitingRoom.getFindMatchButton().addEventHandler(MouseEvent.MOUSE_CLICKED, findMatchEvent);

        startPage.getPasswordFieldLogin().setOnAction(event -> {
            loginAction();
        });

        startPage.getUsernameFieldLogin().setOnAction(event -> {
            loginAction();
        });

        gameFramework.getTypingArea().setOnAction(event -> {
            if (gameFramework.isStarted()) sendMessage();
        });

        gameFramework.getGiveUpButton().setOnAction(event -> {
            if (gameFramework.isStarted()) {
                currentAccount.setLossMatch(currentAccount.getLossMatch()+1);
                currentRoom.setWinner(currentRoom.getOpponentUsername(currentAccount.getUsername()));
                WinAction();
            }
        });

        gameFramework.getMakeDrawButton().setOnAction(event -> {
            if (gameFramework.isStarted()) {
                currentRoom.setWinner("");
                MakeDrawAction();
            }
        });
    }

    public void loginAction() {
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
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Login status");
                    alert.setContentText("Incorrect password. Please consider your information!");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setTitle("Login status");
                alert.setContentText("Username doesn't exists. Please consider your information!");
                alert.showAndWait();
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage() {
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

    public void WinAction() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("End game");
        alert.setContentText(currentRoom.getWinner()
                + " is the winner. Do you want to rematch ?");
        alert.getButtonTypes().clear();

        ButtonType rematchButton = new ButtonType("Rematch");
        ButtonType outGame = new ButtonType("Leave room");
        alert.getButtonTypes().addAll(rematchButton, outGame);
        alert.setHeaderText(null);

        try {
            clientSocketHandler.send(new EndGameSignal(
                    currentRoom.getWinner(),
                    currentRoom.getOpponentPort(clientSocketHandler.getSocket().getLocalPort()),
                            clientSocketHandler.getSocket().getLocalPort())
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        alert.showAndWait().ifPresent(response->{
            Main.gameFramework.stopGame();
            if (response == rematchButton) {
                try {
                    clientSocketHandler.send(
                            new RequestRematchSignal(
                                    currentAccount.getUsername(),
                                    currentRoom.getId(),
                                    currentRoom.getOpponentPort(clientSocketHandler.getSocket().getLocalPort()),
                                    clientSocketHandler.getSocket().getLocalPort(),
                                    currentRoom.getOpponentUsername(currentRoom.getGoFirst())
                                    )
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                quitMatchAction(true);
            }
        });
    }

    public void quitMatchAction(boolean endedGame) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quit room");
        alert.setHeaderText(null);

        if (gameFramework.isStarted()) {
            alert.setContentText("Are you sure to quit room ?. You will lose this game");
        } else {
            alert.setContentText("Are you sure to quit room ?");
        }

        alert.showAndWait().ifPresent(res -> {
            System.out.println(gameFramework.isStarted());

            if (res == ButtonType.OK) {
                try {
                    if (!endedGame) currentAccount.setLossMatch(currentAccount.getLossMatch()+1);

                    primaryStage.setScene(waitingRoom.getScene());
                    Room tmp = serverManage.getRoom(currentRoom.getId());
                    tmp.setAmount(tmp.getAmount() - 1);
                    if (tmp.getAmount() == 0) {
                        serverManage.delete(tmp);
                        clientSocketHandler.send(new UpdateRoomSignal(currentRoom.getId()));
                    } else {
                        String usernameOfOwner = tmp.getOpponentUsername(currentAccount.getUsername());
                        int portOfOwner = tmp.getOpponentPort(clientSocketHandler.getSocket().getLocalPort());
                        String avatarOfOwner = tmp.getOpponentAvatar(currentAccount.getImageIconLink());

                        tmp.setUsernameOfOwner(usernameOfOwner);
                        tmp.setOwnerPort(portOfOwner);
                        tmp.setAvatarOfOwner(avatarOfOwner);

                        serverManage.updateRoom(tmp);
                        clientSocketHandler.send(
                                new QuitRoomSignal(
                                        currentRoom.getId(),
                                        currentRoom.getOpponentPort(clientSocketHandler.getSocket().getLocalPort()),
                                        clientSocketHandler.getSocket().getLocalPort()
                                )
                        );
                    }
                    gameFramework.resetGameBoard();
                    waitingRoom.updateRoomTable();
                    currentAccount.setInMatch(false);
                    serverManage.updateInMatchState(currentAccount.getUsername(), false);
                    currentRoom = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void MakeDrawAction() {
        try {
            clientSocketHandler.send(new DrawRequestSignal(
                    currentRoom.getId(),
                    currentAccount.getUsername(),
                    currentRoom.getOpponentPort(clientSocketHandler.getSocket().getLocalPort()),
                    clientSocketHandler.getSocket().getLocalPort()
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
