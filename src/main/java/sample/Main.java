package sample;

import IndividualInformation.Account;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Main extends Application {
    public static  final int HEIGHT = 720;
    public static final int WIDTH = 1080;
    private StartPage startPage = new StartPage();
    private GameFramework gameFramework = new GameFramework();
    public static DB database = new DB();
    // Network
    private final String host = "localhost";
    private final int PORT = 455;
    public static Socket clientSocket;
//    public static  ObjectInputStream is;
//    public static ObjectOutputStream os;

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
                database.INSERT(newPlayer);
                startPage.resetRegisterPage();
                System.out.println(newPlayer);
            }
        };
        startPage.getRegisterButton().addEventHandler(MouseEvent.MOUSE_CLICKED, registerClick);

        EventHandler<MouseEvent> loginClick = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Account handle after login.
                try {
                    if (database.isAlreadyHasUsername(startPage.getUserNameLogin())) {
                        Account currentAccount = database.getAccount(startPage.getUserNameLogin());
                        if (currentAccount != null && currentAccount.getPassword().equals(startPage.getPasswordLogin())) {
                            primaryStage.setScene(gameFramework.getGameFramework());

                            clientSocket = new Socket(host, PORT);
                            clientSocket.getOutputStream().flush();

                            String message = "SS";
                            ByteArrayOutputStream byteOs = new ByteArrayOutputStream(4096);
                            ObjectOutputStream ObjOs = new ObjectOutputStream(byteOs);
                            ObjOs.writeObject(message);
                            clientSocket.getOutputStream().write(byteOs.toByteArray());
//                            os = new ObjectOutputStream(clientSocket.getOutputStream());
                            // Stream has initial data AC ED 00 05 at OutputStream -> flush()
//                            os.flush();
//                            is = new ObjectInputStream(clientSocket.getInputStream());

                            gameFramework.run();
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
        startPage.getLoginButton().addEventHandler(MouseEvent.MOUSE_CLICKED, loginClick);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
