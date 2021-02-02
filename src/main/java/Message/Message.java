package Message;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private int posX;
    private int posY;
    private int PORT;

    public Message(int posX, int posY, int PORT) {
        this.posX = posX;
        this.posY = posY;
        this.PORT = PORT;
    }

    @Override
    public String toString() {
        return "Position=[PosX = " + posX + ", Posy = " + posY
                + ", PORT = " + PORT;
    }
}
