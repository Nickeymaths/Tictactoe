package Server;

public class Room {
    private int id;
    private int amount;
    private String usernameOfOwner;
    private String avatarOfOwner;
    private String usernameOfOther;
    private String avatarOfOther;
    private int ownerPort;
    private int otherPort;

    public Room(int id, int amount, String usernameOfOwner, String avatarOfOwner, int ownerPort) {
        this.id = id;
        this.amount = amount;
        this.usernameOfOwner = usernameOfOwner;
        this.avatarOfOwner = avatarOfOwner;
        this.ownerPort = ownerPort;
    }

    public Room(int id, int amount, String usernameOfOwner, String avatarOfOwner,
                String usernameOfOther, String avatarOfOther, int ownerPort, int otherPort) {
        this(id, amount, usernameOfOwner, avatarOfOwner, ownerPort);
        this.usernameOfOther = usernameOfOther;
        this.avatarOfOther = avatarOfOther;
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
}
