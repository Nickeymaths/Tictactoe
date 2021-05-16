package Data;

public class RematchResponseSignal extends Data {
    private static final long serialVersionUID = 1L;
    private int room_id;
    private String usernameOfSender;

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public String getUsernameOfSender() {
        return usernameOfSender;
    }

    public void setUsernameOfSender(String usernameOfSender) {
        this.usernameOfSender = usernameOfSender;
    }

    public boolean isAccept() {
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }

    private boolean accept;

    public RematchResponseSignal(String usernameOfSender, int room_id, int receiverPORT, int senderPORT, boolean accept) {
        super(receiverPORT, senderPORT);
        this.room_id = room_id;
        this.usernameOfSender = usernameOfSender;
        this.accept = accept;
    }


}
