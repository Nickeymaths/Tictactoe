package Client.Processor;

import Client.Main;
import Data.*;

public class UpdateAccountTableSignalProcessor extends ProcessComponent {
    @Override
    public void process(Data data) {
        Main.waitingRoom.updatePlayerTable();
    }
}
