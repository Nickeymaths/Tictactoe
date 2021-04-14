package Data;

public class PlayerTurnMessage extends Data {
    private static final long serialVersionUID = 1L;

    private boolean turn;
    private int posX;
    private int posY;

    public PlayerTurnMessage(boolean turn, int posX, int posY, int receiverPort, int senderPort) {
        super(receiverPort, senderPort);
        this.turn = turn;
        this.posX = posX;
        this.posY = posY;
    }

    public boolean getTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
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
