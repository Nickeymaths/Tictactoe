package Data;

import java.io.Serializable;

public class LogIOSignal extends Data {
    private static final long serialVersionUID = 1L;
    private String username;
    private boolean isActive;
    private boolean isInGame;

    public LogIOSignal(String username, boolean isActive, boolean isInGame, int PORT) {
        super(PORT);
        this.username = username;
        this.isActive = isActive;
        this.isInGame = isInGame;
    }

    public String getUsername() {
        return username;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isInGame() {
        return isInGame;
    }

    public void setInGame(boolean inGame) {
        isInGame = inGame;
    }

    @Override
    public String toString() {
        return "LogIOSignal{" +
                "username='" + username + '\'' +
                ", isActive=" + isActive +
                ", isInGame=" + isInGame +
                '}';
    }
}
