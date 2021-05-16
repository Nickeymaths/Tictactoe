package Data;

public class UpdateRoomSignal extends Data {
    private static final long serialVersionUID = 1L;
    private int room_id;

    public UpdateRoomSignal(int room_id) {
        this.room_id = room_id;
    }

    public int getRoom_id() {
        return room_id;
    }

    @Override
    public String toString() {
        return "UpdateRoomSignal{" +
                "room_id=" + room_id +
                '}';
    }
}
