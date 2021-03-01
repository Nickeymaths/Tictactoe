package Client.Processor;

import Client.Main;
import Data.Data;

public class UpdateRoomTableSignalProcessor extends ProcessComponent {
    @Override
    public void process(Data data) {
        Main.waitingRoom.updateRoomTable();
    }
}
