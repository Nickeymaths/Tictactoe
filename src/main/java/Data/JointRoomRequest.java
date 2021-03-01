package Data;

public class JointRoomRequest extends Data {
    private static final long serialVersionUID = 1L;
    private String username;
    private int roomId;

    public JointRoomRequest(String username, int roomId, int receiverPort, int senderPort) {
        super(receiverPort, senderPort);
        this.username = username;
        this.roomId = roomId;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}
