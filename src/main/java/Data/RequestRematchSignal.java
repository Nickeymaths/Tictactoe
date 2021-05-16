package Data;

public class RequestRematchSignal extends Data {
    private static final long serialVersionUID = 1L;
    private int room_id;
    private String usernameOfSender;
    private String goFirst;

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public String getUsernameOfSender() {
        return usernameOfSender;
    }

    public void setUsernameOfSender(String usernameOfSender) {
        this.usernameOfSender = usernameOfSender;
    }

    public String getGoFirst() {
        return goFirst;
    }

    public void setGoFirst(String goFirst) {
        this.goFirst = goFirst;
    }

    public RequestRematchSignal(String usernameOfSender, int room_id, int receiverPORT, int senderPORT, String goFirst) {
        super(receiverPORT, senderPORT);
        this.room_id = room_id;
        this.usernameOfSender = usernameOfSender;
        this.goFirst = goFirst;
    }

    public int getRoom_id() {
        return room_id;
    }
}
