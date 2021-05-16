package Data;

public class DrawRequestSignal extends Data {
    private int roomId;
    private String usernameOfSender;

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getUsernameOfSender() {
        return usernameOfSender;
    }

    public void setUsernameOfSender(String usernameOfSender) {
        this.usernameOfSender = usernameOfSender;
    }

    public DrawRequestSignal(int roomId, String usernameOfSender, int receiverPort, int senderPort) {
        super(receiverPort, senderPort);
        this.roomId = roomId;
        this.usernameOfSender = usernameOfSender;
    }
}
