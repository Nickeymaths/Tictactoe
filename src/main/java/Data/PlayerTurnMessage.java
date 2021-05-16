package Data;

public class PlayerTurnMessage extends Data {
    private static final long serialVersionUID = 1L;

    private String username;
    private int posX;
    private int posY;

    public PlayerTurnMessage(String username, int posX, int posY, int receiverPort, int senderPort) {
        super(receiverPort, senderPort);
        this.username = username;
        this.posX = posX;
        this.posY = posY;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }
}
