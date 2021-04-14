package Client.Processor;

import Client.Main;
import Client.Turn;
import Data.*;
import javafx.application.Platform;

public class PlayerTurnProcessor extends ProcessComponent {
    @Override
    public void process(Data data) {
        PlayerTurnMessage message = (PlayerTurnMessage) data;
        int posX = message.getPosX(), posY = message.getPosY();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (message.getTurn() == Turn.PLAYER1_TURN) {
                    Main.gameFramework.getBoard()[posY][posX].setText("X");
                    Main.gameFramework.getBoard()[posY][posX].setStyle("-fx-text-fill: red; -fx-font: normal bold 18px 'serif';");
                } else {
                    Main.gameFramework.getBoard()[posY][posX].setText("O");
                    Main.gameFramework.getBoard()[posY][posX].setStyle("-fx-text-fill: blue; -fx-font: normal bold 18px 'serif';");
                }
                Main.gameFramework.setPlayPermission(true);
                Main.gameFramework.resetStartTime();
            }
        });
    }
}
