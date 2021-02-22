package Data;

public class QuitRoomSignal extends Data {
    private static final long serialVersionUID = 1L;
    private int room_id;

    public QuitRoomSignal(int room_id) {
        this.room_id = room_id;
    }

    public int getRoom_id() {
        return room_id;
    }

    @Override
    public String toString() {
        return "QuitRoomSignal{" +
                ", room_id=" + room_id +
                '}';
    }
}
