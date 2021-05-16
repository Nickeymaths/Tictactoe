package Client;

import Server.Room;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class InGame {
    private final int N = 20;
    private final int M = 19;

    private long startTime;

    private Label roomIdLabel;
    private Button makeDrawButton;
    private Button quitButton;
    private Label timeLabel1;
    private Label timeLabel2;
    private ImageView player1Avatar;
    private ImageView player2Avatar;
    private Label username1;
    private Label username2;
    private VBox messageView;
    private TextField typingArea;
    private Button sendButton;
    private Button[][] squares;
    private Button giveUpButton;
    private boolean turn;
    private boolean isYourTurn = false;

    private String defaultAvatarLink = "src/main/Resource/avatar/icons8_confusion_96px.png";
    private String defaultNameOfPlayer1 = "Player 1";
    private String defaultNameOfPlayer2 = "Player 2";

    private AnimationTimer timer;

    private boolean started = false;

    private Scene scene;

    public InGame() {
        try {
            initialLookAndFeel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialLookAndFeel() throws Exception {
        AnchorPane mainPane = new AnchorPane();
        AnchorPane featurePane = new AnchorPane();
        AnchorPane boardPane = new AnchorPane();

        // set structure
        mainPane.getChildren().addAll(featurePane, boardPane);

        mainPane.setPrefWidth(Main.WIDTH);
        mainPane.setPrefHeight(Main.HEIGHT);

        // Set position of concrete pane
        setPositionOnAnchorPane(featurePane, 7,8,7,770);
        setPositionOnAnchorPane(boardPane, 7, 324, 7, 7);

        // Create feature Pane
        featurePane.setStyle("-fx-background-color: #5d6d7e");

        roomIdLabel = new Label("#room id");
        roomIdLabel.setFont(new Font("Arial", 15));
        roomIdLabel.setTextFill(Color.WHITESMOKE);
        setPositionOnAnchorPane(roomIdLabel, 14, 235, 671, 21);

        makeDrawButton = new Button("Draw");
        setPositionOnAnchorPane(makeDrawButton,64,174,608,22);

        quitButton = new Button("Quit");
        setPositionOnAnchorPane(quitButton,64,20,608,176);

        giveUpButton = new Button("Give up");
        setPositionOnAnchorPane(giveUpButton, 109, 97, 563, 99);

        timeLabel1 = new Label("");
        timeLabel1.setFont(new Font("Bell MT bold", 17));
        timeLabel1.setTextFill(Color.WHITESMOKE);
        setPositionOnAnchorPane(timeLabel1, 150,182,528,32);

        timeLabel2 = new Label("");
        timeLabel2.setFont(new Font("Bell MT bold", 17));
        timeLabel2.setTextFill(Color.WHITESMOKE);
        setPositionOnAnchorPane(timeLabel2, 150, 43, 522,198);

        player1Avatar = new ImageView();
        setPositionOnAnchorPane(player1Avatar, 189, 184, 421, 30);
        FileInputStream inputStream = new FileInputStream(defaultAvatarLink);
        player1Avatar.setImage(new Image(inputStream));

        ImageView vsImage = new ImageView();
        setPositionOnAnchorPane(vsImage, 227,136,444,136);
        inputStream = new FileInputStream("src/main/Resource/icons8_sword_48px.png");
        vsImage.setImage(new Image(inputStream));

        player2Avatar = new ImageView();
        setPositionOnAnchorPane(player2Avatar, 189, 29, 421, 185);
        inputStream = new FileInputStream(defaultAvatarLink);
        player2Avatar.setImage(new Image(inputStream));

        username1 = new Label(defaultNameOfPlayer1);
        username1.setFont(new Font("Arial bold italic", 15));
        username1.setTextFill(Color.WHITESMOKE);
        setPositionOnAnchorPane(username1, 321,202,366,47);

        username2 = new Label(defaultNameOfPlayer2);
        username2.setFont(new Font("Arial bold italic", 15));
        username2.setTextFill(Color.WHITESMOKE);
        setPositionOnAnchorPane(username2, 321, 50, 366, 199);

        Label chatLabel = new Label("Chat");
        chatLabel.setFont(new Font("Arial", 15));
        chatLabel.setTextFill(Color.WHITESMOKE);
        setPositionOnAnchorPane(chatLabel, 395, 260, 292,15);

        ScrollPane scrollPane = new ScrollPane();
        messageView = new VBox();
        messageView.setStyle("-fx-background-color: #ffffff");

        scrollPane.setContent(messageView);
        setPositionOnAnchorPane(scrollPane, 418,13,90,14);

        typingArea = new TextField();
        setPositionOnAnchorPane(typingArea,646,86,32,13);

        sendButton = new Button("Send");
        setPositionOnAnchorPane(sendButton,646,13,32,236);

        featurePane.getChildren().addAll(
                roomIdLabel,
                makeDrawButton,
                quitButton,
                giveUpButton,
                timeLabel1,
                timeLabel2,
                player1Avatar,
                player2Avatar,
                vsImage,
                username1,
                username2,
                chatLabel,
                scrollPane,
                typingArea,
                sendButton
        );

        // Create board pane
        GridPane grid = new GridPane();

        boardPane.getChildren().add(grid);

        setPositionOnAnchorPane(grid, 5,0,5,0);
        squares = new Button[M][N];

        for (int i = 0; i < N; i++) {
            grid.getColumnConstraints().add(new ColumnConstraints(37));
        }

        for (int i = 0; i < M; i++) {
            grid.getRowConstraints().add(new RowConstraints(37));
        }

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                squares[i][j] = new Button();
                squares[i][j].setPrefWidth(37);
                squares[i][j].setPrefHeight(37);
                grid.add(squares[i][j], j,i);
            }
        }

        scene = new Scene(mainPane);
    }


    public void startGame() {
        startTime = System.nanoTime();
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long timeDuring = (now -  startTime)/1000000000;
                long minus = timeDuring/60;
                long second = timeDuring % 60;

                if (isYourTurn) {
                    if (turn == Turn.PLAYER1_TURN){
                        timeLabel1.setText(String.format("%02d:%02d", minus, second));
                        timeLabel2.setText("");
                    } else {
                        timeLabel2.setText(String.format("%02d:%02d", minus, second));
                        timeLabel1.setText("");
                    }
                } else {
                    if (turn == Turn.PLAYER1_TURN){
                        timeLabel2.setText(String.format("%02d:%02d", minus, second));
                        timeLabel1.setText("");
                    } else {
                        timeLabel1.setText(String.format("%02d:%02d", minus, second));
                        timeLabel2.setText("");
                    }
                }
            }
        };
        timer.start();
        started = true;
    }

    public void stopGame() {
        timer.stop();
        started = false;
    }

    public void setPositionOnAnchorPane(Node child, double top, double right, double bottom, double left) {
        AnchorPane.setTopAnchor(child, top);
        AnchorPane.setRightAnchor(child, right);
        AnchorPane.setBottomAnchor(child, bottom);
        AnchorPane.setLeftAnchor(child, left);
    }

    public Scene getScene() {
        return scene;
    }

    public Button getQuitButton() {
        return quitButton;
    }

    public Button getGiveUpButton() {
        return giveUpButton;
    }


    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public boolean getTurn() {
        return turn;
    }
    
    public boolean isYourTurn() {
        return isYourTurn;
    }
    
    public void setPlayPermission(boolean isYourTurn) {
        this.isYourTurn = isYourTurn;
    }

    public int getNumberOfRow() {
        return M;
    }

    public int getNumberOfColumn() {
        return N;
    }

    public Button[][] getBoard() {
        return squares;
    }

    public TextField getTypingArea() {
        return typingArea;
    }

    public Button getSendButton() {
        return sendButton;
    }

    public VBox getMessageView() {
        return messageView;
    }

    public Label getRoomIdLabel() {
        return roomIdLabel;
    }

    public Label getUsername1() {
        return username1;
    }

    public Label getUsername2() {
        return username2;
    }

    public boolean isStarted() {
        return started;
    }

    public Button getMakeDrawButton() {
        return makeDrawButton;
    }

    public void resetStartTime() {
        startTime = System.nanoTime();
    }

    public void setPlayer1Avatar(String avatarUrl) {
        try {
            FileInputStream inputStream = new FileInputStream(avatarUrl);
            player1Avatar.setImage(new Image(inputStream));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setPlayer2Avatar(String avatarUrl) {
        try {
            FileInputStream inputStream = new FileInputStream(avatarUrl);
            player2Avatar.setImage(new Image(inputStream));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void resetGameBoard() {
        username1.setText(defaultNameOfPlayer1);
        username2.setText(defaultNameOfPlayer2);
        setPlayer1Avatar(defaultAvatarLink);
        setPlayer2Avatar(defaultAvatarLink);
        timeLabel1.setText("");
        timeLabel2.setText("");
        messageView.getChildren().clear();
        typingArea.clear();

        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares[i].length; j++) {
                squares[i][j].setText("");
            }
        }
    }

    public void updateRoom(Room room) {
        roomIdLabel.setText("#" + room.getId());
        roomIdLabel.setFont(new Font("Arial", 15));
        roomIdLabel.setTextFill(Color.WHITESMOKE);
        username1.setText(room.getUsernameOfOwner());
        username1.setFont(new Font("Arial bold italic", 15));
        username1.setTextFill(Color.WHITESMOKE);
        username2.setText(room.getUsernameOfOther());
        username2.setFont(new Font("Arial bold italic", 15));
        username2.setTextFill(Color.WHITESMOKE);
        setPlayer1Avatar(room.getAvatarOfOwner());
        setPlayer2Avatar(room.getAvatarOfOther());
    }

    public boolean checkWin(int i , int j, String mark) {
        int count = 0;
        // Check left
        for (int n = j; n >= 0; n--) {
            if (squares[i][n].getText().equals(mark)) {
                count++;
            } else {
                break;
            }
        }
        // Check right
        for (int n = j + 1; n < N; n++) {
            if (squares[i][n].getText().equals(mark)) {
                count++;
            } else {
                break;
            }
        }
        if (count >= 5) return true;
        count = 0;
        // Check top
        for (int m = i; m >= 0; m--) {
            if (squares[m][j].getText().equals(mark)) {
                count++;
            } else {
                break;
            }
        }
        // Check right
        for (int m = i + 1; m < N; m++) {
            if (squares[m][j].getText().equals(mark)) {
                count++;
            } else {
                break;
            }
        }
        if (count >= 5) return true;
        count = 0;
        // Check top-left
        for (int m = 0; m < N; m++) {
            if (i >= m && j >= m && squares[i-m][j-m].getText().equals(mark)) {
                count++;
            } else {
                break;
            }
        }
        // Check bottom-right
        for (int m = 1; m < N; m++) {
            if (i+m < N && j+m < N && squares[i+m][j+m].getText().equals(mark)) {
                count++;
            } else {
                break;
            }
        }
        if (count >= 5) return true;
        count = 0;
        // Check top-right.
        for (int m = 0; m < N; m++) {
            if (i >= m && j+m < N && squares[i-m][j+m].getText().equals(mark)) {
                count++;
            } else {
                break;
            }
        }
        // Check bottom-left
        for (int m = 1; m < N; m++) {
            if (i+m < N && j >= m && squares[i+m][j-m].getText().equals(mark)) {
                count++;
            } else {
                break;
            }
        }
        if (count >= 5) return true;
        return false;
    }
}












