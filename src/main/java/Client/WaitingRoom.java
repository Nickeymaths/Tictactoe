package Client;

import IndividualInformation.Account;
import Server.Room;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
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

        setPositionOnAnchorPane(logoutButton, 14,966,155,14);
        setPositionOnAnchorPane(profileButton, 14,839,155,131);
        setPositionOnAnchorPane(findMatchButton, 85,596,84,14);
        setPositionOnAnchorPane(createRoomButton, 85,14,84,596);

        anchorPane.getChildren().addAll(logoutButton, profileButton
                , findMatchButton, createRoomButton);

        VBox root = new VBox();
        root.setPadding(new Insets(10, 10, 10, 10));
        root.getChildren().addAll(anchorPane, waitingTapPane);

        scene = new Scene(root, 1080, 720);
    }

    public void setPositionOnAnchorPane(Node child, double top, double right, double bottom, double left) {
        AnchorPane.setTopAnchor(child, top);
        AnchorPane.setRightAnchor(child, right);
        AnchorPane.setBottomAnchor(child, bottom);
        AnchorPane.setLeftAnchor(child, left);
    }

    public Button getLogoutButton() {
        return logoutButton;
    }

    public Button getCreateRoomButton() {
        return createRoomButton;
    }

    public Button getFindMatchButton() {
        return findMatchButton;
    }

    public TableView<Account> getPlayerTable() {
        return playerTable;
    }

    public TableView<Room> getRoomTable() {
        return roomTable;
    }

    public Scene getScene() {
        return scene;
    }

    public void updatePlayerTable() {
        if (playerTable.getItems() != null) playerTable.getItems().clear();
        ObservableList<Account> activeAccounts = FXCollections.observableList(Main.serverManage.getActiveAccountList());
        System.out.println(activeAccounts.subList(0, activeAccounts.size()));
        playerTable.getItems().setAll(activeAccounts);
    }

    public void updateRoomTable() {
        if (roomTable.getItems() != null) roomTable.getItems().clear();
        ObservableList<Room> roomsList = FXCollections.observableList(Main.serverManage.getRoomList());
        System.out.println("Number of room:" + roomsList.size());
        roomTable.getItems().setAll(roomsList);
    }
}
