package Client;

import Server.Room;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.FileInputStream;

public class InGame {
    private final int N = 20;
    private final int M = 19;

    private Label roomIdLabel;
    private Button newGameButton;
    private Button quitButton;
    private Label processingStateLabel;
    private Label waitingStateLabel;
    private ImageView player1Avatar;
    private ImageView player2Avatar;
    private Label username1;
    private Label username2;
    private Label timeLabel;
    private TextArea messageView;
    private TextField typingArea;
    private Button sendButton;
    private Button[][] squares;

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

        newGameButton = new Button("New game");
        setPositionOnAnchorPane(newGameButton,64,174,608,22);

        quitButton = new Button("Quit");
        setPositionOnAnchorPane(quitButton,64,20,608,176);

        processingStateLabel = new Label("Processing...");
        processingStateLabel.setFont(new Font("Bell MT bold", 17));
        processingStateLabel.setTextFill(Color.WHITESMOKE);
        setPositionOnAnchorPane(processingStateLabel, 150,182,528,32);

        waitingStateLabel = new Label("Waiting");
        waitingStateLabel.setFont(new Font("Bell MT bold", 17));
        waitingStateLabel.setTextFill(Color.WHITESMOKE);
        setPositionOnAnchorPane(waitingStateLabel, 150, 43, 522,198);

        player1Avatar = new ImageView();
        setPositionOnAnchorPane(player1Avatar, 189, 184, 421, 30);
        FileInputStream inputStream = new FileInputStream("src/main/Resource/icons8_magneto_96px.png");
        player1Avatar.setImage(new Image(inputStream));

        ImageView vsImage = new ImageView();
        setPositionOnAnchorPane(vsImage, 227,136,444,136);
        inputStream = new FileInputStream("src/main/Resource/icons8_sword_48px.png");
        vsImage.setImage(new Image(inputStream));

        player2Avatar = new ImageView();
        setPositionOnAnchorPane(player2Avatar, 189, 29, 421, 185);
        inputStream = new FileInputStream("src/main/Resource/icons8_spider-man_head_96px.png");
        player2Avatar.setImage(new Image(inputStream));

        username1 = new Label("Name 1");
        username1.setFont(new Font("Arial bold italic", 15));
        username1.setTextFill(Color.WHITESMOKE);
        setPositionOnAnchorPane(username1, 321,202,366,47);

        username2 = new Label("Name 2");
        username2.setFont(new Font("Arial bold italic", 15));
        username2.setTextFill(Color.WHITESMOKE);
        setPositionOnAnchorPane(username2, 321, 50, 366, 199);

        timeLabel = new Label("Timer");
        timeLabel.setFont(new Font("Arial", 15));
        timeLabel.setTextFill(Color.WHITESMOKE);
        setPositionOnAnchorPane(timeLabel,319,132,364,140);

        Label chatLabel = new Label("Chat");
        chatLabel.setFont(new Font("Arial", 15));
        chatLabel.setTextFill(Color.WHITESMOKE);
        setPositionOnAnchorPane(chatLabel, 395, 260, 292,15);

        messageView = new TextArea();
        setPositionOnAnchorPane(messageView, 418,13,90,14);

        typingArea = new TextField();
        setPositionOnAnchorPane(typingArea,646,86,32,13);

        sendButton = new Button("Send");
        setPositionOnAnchorPane(sendButton,646,13,32,236);

        featurePane.getChildren().addAll(
                roomIdLabel,
                newGameButton,
                quitButton,
                processingStateLabel,
                waitingStateLabel,
                player1Avatar,
                player2Avatar,
                vsImage,
                username1,
                username2,
                timeLabel,
                chatLabel,
                messageView,
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

/*    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public void run() {
        EventHandler<MouseEvent>[][] mouseClickEvent = new EventHandler[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int finalI = i;
                int finalJ = j;
                mouseClickEvent[i][j] = new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        try {
                            if (Main.clientSocket.getInputStream().available() != 0) {
                                byte[] reader = new byte[4096];
                                Main.clientSocket.getInputStream().read(reader);
                                ByteArrayInputStream byteIs = new ByteArrayInputStream(reader);
                                ObjectInputStream ObjIs = new ObjectInputStream(byteIs);
                                String comeData = (String) ObjIs.readObject();
                                System.out.println("Server say: " + comeData);
                                hasNewTurn = true;
                            } else {
                                hasNewTurn = false;
                            }

//                            if (hasNewTurn) {
                        if (turn == Turn.PLAYER1_TURN) {
                            // Mark X
                            buttons[finalI][finalJ].setText("X");
                            buttons[finalI][finalJ].setStyle("-fx-text-fill: red; -fx-font: normal bold 18px 'serif';");
                            System.out.println(checkWin(finalI, finalJ, "X"));
                        } else {
                            // Mark Y
                            buttons[finalI][finalJ].setText("O");
                            buttons[finalI][finalJ].setStyle("-fx-text-fill: blue; -fx-font: normal bold 18px 'serif';");
                            System.out.println(checkWin(finalI, finalJ, "O"));
                        }
                        turn = !turn;
                                String message = String.valueOf(finalI) + (finalJ);

                                ByteArrayOutputStream byteOs = new ByteArrayOutputStream(4096);
                                ObjectOutputStream ObjOs = new ObjectOutputStream(byteOs);
                                ObjOs.writeObject(message);
                                Main.clientSocket.getOutputStream().write(byteOs.toByteArray());
//                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                buttons[i][j].addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickEvent[i][j]);
            }
        }
    }*/



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












