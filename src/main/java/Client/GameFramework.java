package Client;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.io.*;

public class GameFramework {
    private final int N = 20;
    private Scene scene;
    Button[][] buttons = new Button[N][N];
    private boolean turn = Turn.PLAYER1_TURN;
    private boolean hasNewTurn = true;

    public GameFramework() {
        try {
            initialGameFrame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialGameFrame() throws Exception {
        SplitPane splitFrame = new SplitPane();
        GridPane boardGamePane = new GridPane();
        VBox playerArea = new VBox();

        for (int i = 0; i < N; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / N);
            boardGamePane.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < N; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / N);
            boardGamePane.getRowConstraints().add(rowConst);
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                buttons[i][j] = new Button();
                buttons[i][j].setPrefWidth(0.7*Main.WIDTH/N);
                buttons[i][j].setPrefHeight(Main.HEIGHT/N);
                boardGamePane.add(buttons[i][j], i, j);
            }
        }

        splitFrame.getItems().add(boardGamePane);
        splitFrame.getItems().add(playerArea);
        splitFrame.setDividerPositions(0.7, 0.3);

        boardGamePane.setPadding(new Insets(0,0,0,0));

        boardGamePane.setMaxWidth(Main.WIDTH*0.7);
        boardGamePane.setMinWidth(Main.WIDTH*0.7);

        boardGamePane.setMinHeight(Main.HEIGHT);
        boardGamePane.setMaxHeight(Main.HEIGHT);

        GridPane boardInfoArea = new GridPane();

        Label board = new Label("#Board");
        Label time = new Label("Time");

        Label player1 = new Label();
        FileInputStream player1_Img = new FileInputStream("src/main/Resource/player1.jpg");
        player1.setGraphic(new ImageView(new Image(player1_Img)));
        Label player1Time = new Label("#1 time");

        Label player2 = new Label();
        FileInputStream player2_Img = new FileInputStream("src/main/Resource/player2.jpg");
        player2.setGraphic(new ImageView(new Image(player2_Img)));
        Label player2Time = new Label("#2 time");

        boardInfoArea.add(board, 0, 0);
        boardInfoArea.add(time, 1, 0);
        boardInfoArea.add(player1, 0, 2);
        boardInfoArea.add(player1Time, 0, 3);
        boardInfoArea.add(player2, 1, 2);
        boardInfoArea.add(player2Time, 1, 3);
        board.setAlignment(Pos.CENTER);

        TextArea chatArea = new TextArea();

        TextField chatType = new TextField();

        playerArea.getChildren().addAll(boardInfoArea, chatArea, chatType);

        scene = new Scene(splitFrame, Main.WIDTH, Main.HEIGHT);
    }

    public Scene getGameFramework() {
        return scene;
    }

    public void setTurn(boolean turn) {
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

                            if (hasNewTurn) {
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
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                buttons[i][j].addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickEvent[i][j]);
            }
        }
    }

    public boolean checkWin(int i , int j, String mark) {
        int count = 0;
        // Check left
        for (int n = j; n >= 0; n--) {
            if (buttons[i][n].getText().equals(mark)) {
                count++;
            } else {
                break;
            }
        }
        // Check right
        for (int n = j + 1; n < N; n++) {
            if (buttons[i][n].getText().equals(mark)) {
                count++;
            } else {
                break;
            }
        }
        if (count >= 5) return true;
        count = 0;
        // Check top
        for (int m = i; m >= 0; m--) {
            if (buttons[m][j].getText().equals(mark)) {
                count++;
            } else {
                break;
            }
        }
        // Check right
        for (int m = i + 1; m < N; m++) {
            if (buttons[m][j].getText().equals(mark)) {
                count++;
            } else {
                break;
            }
        }
        if (count >= 5) return true;
        count = 0;
        // Check top-left
        for (int m = 0; m < N; m++) {
            if (i >= m && j >= m && buttons[i-m][j-m].getText().equals(mark)) {
                count++;
            } else {
                break;
            }
        }
        // Check bottom-right
        for (int m = 1; m < N; m++) {
            if (i+m < N && j+m < N && buttons[i+m][j+m].getText().equals(mark)) {
                count++;
            } else {
                break;
            }
        }
        if (count >= 5) return true;
        count = 0;
        // Check top-right.
        for (int m = 0; m < N; m++) {
            if (i >= m && j+m < N && buttons[i-m][j+m].getText().equals(mark)) {
                count++;
            } else {
                break;
            }
        }
        // Check bottom-left
        for (int m = 1; m < N; m++) {
            if (i+m < N && j >= m && buttons[i+m][j-m].getText().equals(mark)) {
                count++;
            } else {
                break;
            }
        }
        if (count >= 5) return true;
        return false;
    }
}
