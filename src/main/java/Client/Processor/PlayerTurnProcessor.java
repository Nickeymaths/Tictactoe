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
                Main.gameFramework.getBoard()[posY][posX].setText(Main.currentRoom.getSymbol(message.getUsername()));
                Main.gameFramework.getBoard()[posY][posX].setStyle(Main.currentRoom.getColorSymbol(Main.currentRoom.getSymbol(message.getUsername())));
//                Main.gameFramework.setPlayPermission(true);
                Main.currentRoom.setCurrentTurn(Main.currentAccount.getUsername());
                Main.gameFramework.resetStartTime();
            }
        });
    }
}
