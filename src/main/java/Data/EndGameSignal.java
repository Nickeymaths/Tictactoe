package Data;

public class EndGameSignal extends Data {
    private String winner;

    public EndGameSignal(String winner, int receiverPort, int senderPort) {
        super(receiverPort, senderPort);
        this.winner = winner;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}
