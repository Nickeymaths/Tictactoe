package Data;

public class JointRoomAccept extends Data {
    private int roomId;
    public JointRoomAccept(int roomId, int receiverPort, int senderPort) {
        super(receiverPort, senderPort);
        this.roomId = roomId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}
