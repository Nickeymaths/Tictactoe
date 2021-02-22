package Client.Processor;

import Client.Main;
import Data.*;

public class UpdateAccountSignalProcessor extends ProcessComponent {
    @Override
    public void process(Data data) {
        Main.waitingRoom.updatePlayerTable();
    }
}
