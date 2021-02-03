package Client;

import IndividualInformation.Account;
import IndividualInformation.Sex;
import javafx.collections.ObservableList;;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;

public class StartPage {
    private boolean isLogging = true;

    private Label usernameLabelLogin;
    private Label passwordLabelLogin;

    private TextField usernameFieldLogin;
    private PasswordField passwordFieldLogin;

    private Button loginButton;
    private Button createAccountButton;

    private Scene loginScene;

    private TextField fullNameReg;
    private TextField usernameFieldReg;
    private PasswordField passwordFieldReg;
    private PasswordField confirmPasswordReg;
    private DatePicker dateReg;
    private Label sexLabel;
    private Button registerButton;
    private Scene registerScene;
    private RadioButton maleButton;
    private RadioButton femaleButton;

    public void initial() throws IOException {
        usernameLabelLogin = new Label("Username");
        usernameLabelLogin.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));

        passwordLabelLogin = new Label("Password");
        passwordLabelLogin.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));

        usernameFieldLogin = new TextField();
        usernameFieldLogin.setPromptText("Username");

        passwordFieldLogin = new PasswordField();
        passwordFieldLogin.setPromptText("Password");

        loginButton = new Button("Login");
        createAccountButton = new Button("Create Account");

        GridPane pane = new GridPane();
        pane.setPadding(new Insets(30,10,10,10));
        pane.setVgap(5);
        pane.setHgap(5);

        pane.setAlignment(Pos.TOP_CENTER);

        pane.add(usernameLabelLogin, 0, 0);
        pane.add(passwordLabelLogin, 0, 1);
        pane.add(usernameFieldLogin, 1, 0);
        pane.add(passwordFieldLogin, 1, 1);
        pane.add(loginButton, 0, 2);
        pane.add(createAccountButton, 1, 2);

        FileInputStream loginInp = new FileInputStream("D:/Java/OOP/GameTicTocToe/src/main/Resource/loginPicture.jpg");
        Image imageLoader = new Image(loginInp);

        Background background = new Background(new BackgroundImage(imageLoader, null, null, null, null));

        pane.setBackground(background);

        loginScene = new Scene(pane);

        // Register scene
        Label registerLabel = new Label("Register");
        registerLabel.setFont(Font.font("verdana", FontWeight.SEMI_BOLD, 20));

        fullNameReg = new TextField();
        fullNameReg.setPromptText("Full name");

        usernameFieldReg = new TextField();
        usernameFieldReg.setPromptText("Username");
        usernameFieldReg.setMinWidth(400);

        Button checkButton = new Button("Check");

        HBox usernameRow = new HBox();
        usernameRow.setSpacing(60);
        usernameRow.getChildren().add(usernameFieldReg);
        usernameRow.getChildren().add(checkButton);

        passwordFieldReg = new PasswordField();
        passwordFieldReg.setPromptText("Password");

        confirmPasswordReg = new PasswordField();
        confirmPasswordReg.setPromptText("Confirm password");

        dateReg = new DatePicker();

        sexLabel = new Label("Sex:");
        sexLabel.setFont(Font.font("verdana", FontWeight.NORMAL, 18));

        registerButton = new Button("Register");

        VBox registerPane = new VBox();
        registerPane.setPadding(new Insets(10,270,10,270));
        registerPane.setSpacing(20);
        registerPane.setAlignment(Pos.CENTER);

        ObservableList regListObs = registerPane.getChildren();
        regListObs.add(0, registerLabel);
        regListObs.add(fullNameReg);
        regListObs.add(usernameRow);
        regListObs.add(passwordFieldReg);
        regListObs.add(confirmPasswordReg);
        regListObs.add(dateReg);

        HBox sexRow = new HBox();
        sexRow.setSpacing(20);

        ToggleGroup sexGroup = new ToggleGroup();
        maleButton = new RadioButton("Male");
        maleButton.setToggleGroup(sexGroup);
        femaleButton = new RadioButton("Female");
        femaleButton.setToggleGroup(sexGroup);

        sexRow.getChildren().addAll(sexLabel, maleButton, femaleButton);

        regListObs.add(sexRow);
        regListObs.add(registerButton);

        registerScene = new Scene(registerPane);
    }

    public void run(Stage stage) {
        stage.setScene(loginScene);
        EventHandler<MouseEvent> createAccountClick = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setScene(registerScene);
            }
        };
        createAccountButton.addEventHandler(MouseEvent.MOUSE_CLICKED, createAccountClick);

        EventHandler<MouseEvent> registerClick = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setScene(loginScene);
            }
        };
        registerButton.addEventHandler(MouseEvent.MOUSE_CLICKED, registerClick);
    }

    public Scene getLoginScene() {
        return loginScene;
    }

    public Scene getRegisterScene() {
        return registerScene;
    }

    public Button getRegisterButton() {
        return registerButton;
    }

    public Button getCreateAccountButton() {
        return createAccountButton;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public String getUserNameLogin() {
        return usernameFieldLogin.getText();
    }

    public String getPasswordLogin() {
        return passwordFieldLogin.getText();
    }

    public Account getAccount() {
        boolean sex = maleButton.isSelected()? Sex.MALE : Sex.FEMALE;
        if (!Main.database.isAlreadyHasUsername(usernameFieldReg.getText())) {
            if (passwordFieldReg.getText().equals(confirmPasswordReg.getText())) {
                return new Account(usernameFieldReg.getText(), passwordFieldReg.getText()
                        , fullNameReg.getText(), sex, dateReg.getEditor().getText());
            } else {
                System.out.println("Confirm password is not same");
            }
        }
        else
            System.out.println("Username is already exist");
        return null;
    }

    public void resetRegisterPage() {
        usernameFieldReg.clear();
        fullNameReg.clear();
        passwordFieldReg.clear();
        confirmPasswordReg.clear();
        dateReg.getEditor().clear();
        maleButton.setSelected(false);
        femaleButton.setSelected(false);
    }
}
