package Client;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Profile {
    private Scene profileScene;
    private TextField usernameField;
    private DatePicker dobField;
    private TextField fullNameField;
    private Label winRateLabel;
    private Label totalMatchLabel;
    private ImageView image;
    private Button saveProfileButton;
    private Button cancelProfileButton;
    private Label maleLabel;

    private String defaultAvatarLink = "src/main/Resource/avatar/icons8_confusion_96px.png";


    public Profile() {
        initialUI();
    }

    public void initialUI() {
        AnchorPane profilePane = new AnchorPane();
        profilePane.setPrefWidth(800);
        profilePane.setPrefHeight(600);
        profilePane.setStyle("-fx-background-color: #41608a");

        GridPane generalInfo = new GridPane();
        setPositionOnAnchorPane(generalInfo, 26,240,374,240);

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(40);
        generalInfo.getColumnConstraints().add(column1);

        Label usernameLabel = new Label("Username");
        usernameLabel.setPrefWidth(227);
        usernameLabel.setPrefHeight(40);
        usernameLabel.setTextFill(Color.WHITESMOKE);
        generalInfo.add(usernameLabel, 0, 0);

        usernameField = new TextField();
        usernameField.setEditable(false);
        generalInfo.add(usernameField, 1, 0);

        Label fullNameLabel = new Label("Full name");
        fullNameLabel.setPrefWidth(227);
        fullNameLabel.setPrefHeight(40);
        fullNameLabel.setTextFill(Color.WHITESMOKE);
        generalInfo.add(fullNameLabel, 0, 1);

        fullNameField = new TextField();
        generalInfo.add(fullNameField, 1, 1);

        Label dobLabel = new Label("Date of birth");
        dobLabel.setPrefWidth(227);
        dobLabel.setPrefHeight(40);
        dobLabel.setTextFill(Color.WHITESMOKE);
        generalInfo.add(dobLabel, 0, 2);

        dobField = new DatePicker();
        generalInfo.add(dobField, 1, 2);

        maleLabel = new Label("Male");
        maleLabel.setPrefWidth(227);
        maleLabel.setPrefHeight(40);
        maleLabel.setTextFill(Color.WHITESMOKE);

        Label statisticLabel = new Label("Statistic");
        statisticLabel.setPrefWidth(227);
        statisticLabel.setPrefHeight(40);
        statisticLabel.setFont(new Font("Arial bold", 20));
        statisticLabel.setTextFill(Color.WHITESMOKE);
        statisticLabel.setAlignment(Pos.CENTER);
        setPositionOnAnchorPane(statisticLabel, 282,316,282,316);

        VBox statisticPane = new VBox();
        statisticPane.setPadding(new Insets(10,10,10,10));
        setPositionOnAnchorPane(statisticPane, 357,510,100,40);

        winRateLabel = new Label("Win rate: 50%");
        winRateLabel.setPrefWidth(227);
        winRateLabel.setPrefHeight(40);
        winRateLabel.setTextFill(Color.WHITESMOKE);

        totalMatchLabel = new Label("Total match: 300");
        totalMatchLabel.setPrefWidth(227);
        totalMatchLabel.setPrefHeight(40);
        totalMatchLabel.setTextFill(Color.WHITESMOKE);

        statisticPane.getChildren().addAll(winRateLabel, totalMatchLabel);


        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream("src/main/Resource/icons8_sword_48px.png");
            image = new ImageView(new Image(inputStream));
            setPositionOnAnchorPane(image, 26,423,224,27);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        saveProfileButton = new Button("Save");
        setPositionOnAnchorPane(saveProfileButton, 470,167,55,521);

        cancelProfileButton = new Button("Cancel");
        setPositionOnAnchorPane(cancelProfileButton, 470,32,55,656);

        profilePane.getChildren().addAll(
                image,
                generalInfo,
                statisticLabel,
                statisticPane,
                saveProfileButton,
                cancelProfileButton
        );

        profileScene = new Scene(profilePane);

    }

    public void setPositionOnAnchorPane(Node child, double top, double right, double bottom, double left) {
        AnchorPane.setTopAnchor(child, top);
        AnchorPane.setRightAnchor(child, right);
        AnchorPane.setBottomAnchor(child, bottom);
        AnchorPane.setLeftAnchor(child, left);
    }

    public void setAccountAvatar(String avatarUrl) {
        try {
            FileInputStream inputStream = new FileInputStream(avatarUrl);
            image.setImage(new Image(inputStream));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Scene getProfileScene() {
        return profileScene;
    }

    public Button getCancelProfileButton() {
        return cancelProfileButton;
    }

    public Button getSaveProfileButton() {
        return saveProfileButton;
    }

    public TextField getUsernameField() {
        return usernameField;
    }

    public TextField getFullNameField() {
        return fullNameField;
    }

    public DatePicker getDobField() {
        return dobField;
    }

    public Label getWinRateLabel() {
        return winRateLabel;
    }

    public Label getTotalMatchLabel() {
        return totalMatchLabel;
    }

    public Label getMaleLabel() {
        return maleLabel;
    }
}
