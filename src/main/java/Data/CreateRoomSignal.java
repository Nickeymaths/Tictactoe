package Data;

public class CreateRoomSignal extends Data {
    private static final long serialVersionUID = 1L;
    private int room_id;

    public CreateRoomSignal(int room_id) {
        this.room_id = room_id;
    }

    public int getRoom_id() {
        return room_id;
    }

    @Override
    public String toString() {
        return "CreateRoomSignal{" +
                ", room_id=" + room_id +
                '}';
    }
}
