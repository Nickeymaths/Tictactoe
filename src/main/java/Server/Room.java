package Server;

public class Room {
    private int id;
    private int amount;
    private String usernameOfOwner;
    private String usernameOfOther;
    private int ownerPort;
    private int otherPort;

    public Room(int id, int amount, String usernameOfOwner, int ownerPort) {
        this.id = id;
        this.amount = amount;
        this.usernameOfOwner = usernameOfOwner;
        this.ownerPort = ownerPort;
    }

    public Room(int id, int amount, String usernameOfOwner, String usernameOfOther, int ownerPort, int otherPort) {
        this(id, amount, usernameOfOwner, ownerPort);
        this.usernameOfOther = usernameOfOther;
        this.otherPort = otherPort;
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
}
