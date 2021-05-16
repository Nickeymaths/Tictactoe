package Data;

public class DrawAccept extends Data {
    private boolean accept;

    public boolean isAccept() {
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }

    public String getUsernameOfSender() {
        return usernameOfSender;
    }

    public void setUsernameOfSender(String usernameOfSender) {
        this.usernameOfSender = usernameOfSender;
    }

    private String usernameOfSender;
    public DrawAccept(String usernameOfSender, boolean accept, int receiverPort, int senderPort) {
        super(receiverPort, senderPort);
        this.accept = accept;
        this.usernameOfSender = usernameOfSender;
    }
}
