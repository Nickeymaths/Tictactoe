package Data;

public class ChatMessage extends Data {
    public static final long serialVersionUID = 1L;
    private String message;
    private String usernameOfSender;
    private String usernameOfReceiver;

    public ChatMessage(String message, String usernameOfReceiver, int receiverPort, String usernameOfSender, int senderPort) {
        super(receiverPort, senderPort);
        this.message = message;
        this.usernameOfReceiver = usernameOfReceiver;
        this.usernameOfSender = usernameOfSender;
    }

    public String getMessage() {
        return message;
    }

    public String getUsernameOfSender() {
        return usernameOfSender;
    }

    public String getUsernameOfReceiver() {
        return usernameOfReceiver;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "message='" + message + '\'' +
                '}';
    }
}
