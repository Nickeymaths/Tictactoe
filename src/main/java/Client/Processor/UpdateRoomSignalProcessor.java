package Client.Processor;

import Client.Main;
import Data.Data;

public class UpdateRoomSignalProcessor extends ProcessComponent {
    @Override
    public void process(Data data) {
        Main.waitingRoom.updateRoomTable();
    }
}
