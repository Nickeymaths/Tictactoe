package Client;

import IndividualInformation.Account;
import Server.Room;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class WaitingRoom {
    private Scene scene;
    private Button logoutButton;
    private Button profileButton;
    private Button findMatchButton;
    private Button createRoomButton;
    private TableView<Account> playerTable;
    private TableView<Room> roomTable;
    private TabPane waitingTapPane;

    public WaitingRoom() {
        waitingTapPane = new TabPane();
        initialLookAndFeel();
    }

    public void initialLookAndFeel() {
        Tab playerTab = new Tab("Player Online");
        waitingTapPane.getTabs().add(playerTab);
        // Player tab
        playerTable = new TableView<>();

        TableColumn<Account, String> userNameCol //
                = new TableColumn<>("User Name");

        TableColumn<Account, String> activeCol = new TableColumn<>("Active");
        TableColumn<Account, String> inMatchCol = new TableColumn<>("In game");

        userNameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        activeCol.setCellValueFactory(new PropertyValueFactory<>("active"));
        inMatchCol.setCellValueFactory(new PropertyValueFactory<>("inMatch"));

        playerTable.getColumns().addAll(userNameCol, activeCol, inMatchCol);

        playerTable.setRowFactory(new Callback<TableView<Account>, TableRow<Account>>() {
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

        playerTab.setContent(playerTable);
        // Client.Room tab
        Tab roomTab = new Tab("Room");
        waitingTapPane.getTabs().add(roomTab);

        roomTable = new TableView<>();

        TableColumn<Room, String> roomId //
                = new TableColumn<>("id");

        TableColumn<Room, String> amount = new TableColumn<>("amount");

        roomId.setCellValueFactory(new PropertyValueFactory<>("id"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        roomTable.getColumns().addAll(roomId, amount);

        roomTable.setRowFactory(new Callback<TableView<Room>, TableRow<Room>>() {
            @Override
            public TableRow<Room> call(TableView<Room> param) {
                TableRow<Room> row = new TableRow<>();
                ContextMenu contextMenu = new ContextMenu();

                MenuItem joinRoom = new Menu("Join room");
                joinRoom.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        // Code for join request

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

        roomTab.setContent(roomTable);

        waitingTapPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        // Waiting room pane
        AnchorPane anchorPane = new AnchorPane();
//        anchorPane.setPrefHeight(Main.HEIGHT);
        anchorPane.setPrefWidth(Main.WIDTH);

        logoutButton = new Button("Logout");
        profileButton = new Button("Edit Profile");
        findMatchButton = new Button("Find match");
        createRoomButton = new Button("Create Room");

        AnchorPane.setLeftAnchor(logoutButton, 14.0);
        AnchorPane.setTopAnchor(logoutButton, 14.0);
        AnchorPane.setBottomAnchor(logoutButton, 155.0);
        AnchorPane.setRightAnchor(logoutButton, 946.0);

        AnchorPane.setLeftAnchor(profileButton, 111.0);
        AnchorPane.setTopAnchor(profileButton, 14.0);
        AnchorPane.setBottomAnchor(profileButton, 155.0);
        AnchorPane.setRightAnchor(profileButton, 839.0);

        AnchorPane.setLeftAnchor(findMatchButton, 14.0);
        AnchorPane.setTopAnchor(findMatchButton, 85.0);
        AnchorPane.setBottomAnchor(findMatchButton, 84.0);
        AnchorPane.setRightAnchor(findMatchButton, 596.0);

        AnchorPane.setLeftAnchor(createRoomButton, 596.0);
        AnchorPane.setTopAnchor(createRoomButton, 85.0);
        AnchorPane.setBottomAnchor(createRoomButton, 84.0);
        AnchorPane.setRightAnchor(createRoomButton, 14.0);

        anchorPane.getChildren().addAll(logoutButton, profileButton
                , findMatchButton, createRoomButton);

        VBox root = new VBox();
        root.setPadding(new Insets(10, 10, 10, 10));
        root.getChildren().addAll(anchorPane, waitingTapPane);

        scene = new Scene(root, 1080, 720);
    }

    public Button getLogoutButton() {
        return logoutButton;
    }

    public Button getCreateRoomButton() {
        return createRoomButton;
    }

    public Scene getScene() {
        return scene;
    }

    public void updatePlayerTable() {
        System.out.println(playerTable.getItems());
        if (playerTable.getItems() != null) playerTable.getItems().clear();
        ObservableList<Account> activeAccounts = FXCollections.observableList(Main.serverManage.getActiveAccountList());
        playerTable.setItems(activeAccounts);
    }

    public void updateRoomTable() {
        if (roomTable.getItems() != null) roomTable.getItems().clear();
        ObservableList<Room> roomsList = FXCollections.observableList(Main.serverManage.getRoomList());
        System.out.println(roomsList);
        roomTable.setItems(roomsList);
    }
}
