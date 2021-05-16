package Server;

import java.util.HashMap;
import java.util.Map;

public class Room {
    private int id;
    private int amount;
    private String usernameOfOwner;
    private String avatarOfOwner;
    private String usernameOfOther;
    private String avatarOfOther;
    private int ownerPort;
    private int otherPort;

    private String goFirst;
    private String currentTurn;
    private Map<String, String> symbol = new HashMap<>();
    private Map<String, String> symbol_color = new HashMap<>();
    private String winner;

    public Room(int id, int amount, String usernameOfOwner, String avatarOfOwner, int ownerPort) {
        this.id = id;
        this.amount = amount;
        this.usernameOfOwner = usernameOfOwner;
        this.avatarOfOwner = avatarOfOwner;
        this.ownerPort = ownerPort;

        goFirst = usernameOfOwner;
        currentTurn = goFirst;
        symbol.put(usernameOfOwner, "X");
        symbol_color.put("X", "-fx-text-fill: red; -fx-font: normal bold 18px 'serif';");
        symbol_color.put("O", "-fx-text-fill: blue; -fx-font: normal bold 18px 'serif';");
    }

    public Room(int id, int amount, String usernameOfOwner, String avatarOfOwner,
                String usernameOfOther, String avatarOfOther, int ownerPort, int otherPort) {
        this(id, amount, usernameOfOwner, avatarOfOwner, ownerPort);
        this.usernameOfOther = usernameOfOther;
        this.avatarOfOther = avatarOfOther;
        this.otherPort = otherPort;

        goFirst = usernameOfOwner;
        currentTurn = goFirst;

        symbol.put(usernameOfOwner, "X");
        symbol.put(usernameOfOther, "O");

        symbol_color.put("X", "-fx-text-fill: red; -fx-font: normal bold 18px 'serif';");
        symbol_color.put("O", "-fx-text-fill: blue; -fx-font: normal bold 18px 'serif';");
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setUsernameOfOwner(String usernameOfOwner) {
        this.usernameOfOwner = usernameOfOwner;
    }

    public String getUsernameOfOwner() {
        return usernameOfOwner;
    }

    public void setUsernameOfOther(String usernameOfOther) {
        this.usernameOfOther = usernameOfOther;
    }

    public String getUsernameOfOther() {
        return usernameOfOther;
    }

    public int getOwnerPort() {
        return ownerPort;
    }

    public void setOwnerPort(int ownerPort) {
        this.ownerPort = ownerPort;
    }

    public void setOtherPort(int otherPort) {
        this.otherPort = otherPort;
    }

    public int getOtherPort() {
        return otherPort;
    }

    public void setAvatarOfOwner(String avatarOfOwner) {
        this.avatarOfOwner = avatarOfOwner;
    }

    public String getAvatarOfOwner() {
        return avatarOfOwner;
    }

    public void setAvatarOfOther(String avatarOfOther) {
        this.avatarOfOther = avatarOfOther;
    }

    public String getAvatarOfOther() {
        return avatarOfOther;
    }

    public String getGoFirst() {
        return goFirst;
    }

    public String getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(String currentTurn) {
        this.currentTurn = currentTurn;
    }

    public void setGoFirst(String goFirst) {
        this.goFirst = goFirst;
    }

    public String getSymbol(String username) {
        return symbol.get(username);
    }

    public void addSymbol(String username, String user_symbol) {
        symbol.put(username, user_symbol);
    }

    public String getColorSymbol(String symbol) {
        return symbol_color.get(symbol);
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    /**
     * Return player who has port number=port 's opponent port number
     * @param port
     * @return
     */
    public int getOpponentPort(int port) {
        if (port == ownerPort) return otherPort;
        else if (port == otherPort) return ownerPort;
        return -1;
    }

    public String getOpponentUsername(String username) {
        if (username.equals(usernameOfOwner)) {
            return usernameOfOther;
        }
        if (username.equals(usernameOfOther)) {
            return usernameOfOwner;
        }
        return null;
    }

    public String getOpponentAvatar(String avatar) {
        if (avatarOfOwner.equals(avatar)) {
            return avatarOfOther;
        }
        if (avatarOfOther.equals(avatar)) {
            return avatarOfOwner;
        }
        return null;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", amount=" + amount +
                ", usernameOfOwner='" + usernameOfOwner + '\'' +
                ", avatarOfOwner='" + avatarOfOwner + '\'' +
                ", usernameOfOther='" + usernameOfOther + '\'' +
                ", avatarOfOther='" + avatarOfOther + '\'' +
                ", ownerPort=" + ownerPort +
                ", otherPort=" + otherPort +
                '}';
    }
}
