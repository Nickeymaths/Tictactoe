package IndividualInformation;

import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.Objects;

public class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String fullName;
    private Date birthday;
    private boolean male;
    private int winMatch;
    private int lossMatch;
    private String imageIconLink;
    private boolean active;
    private boolean inMatch;

    public Account(String username, String password, String fullName, boolean isMale, Date birthday) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.birthday = birthday;
        this.male = isMale;
    }

    public Account(String username, String password, String fullName, boolean isMale, Date birthday,
                   int winMatch, int lossMatch, boolean active, boolean inMatch) {
        this(username, password, fullName, isMale, birthday);
        this.active = active;
        this.inMatch = inMatch;
        this.winMatch = winMatch;
        this.lossMatch = lossMatch;
    }

    public int getWinMatch() {
        return winMatch;
    }

    public int getLossMatch() {
        return lossMatch;
    }

     public void setWinMatch(int number) {
        winMatch = number;
     }

     public void setLossMatch(int number) {
        lossMatch = number;
     }

     public String getPassword() {
        return password;
     }

     public void setPassword(String newPassword) {
        this.password = newPassword;
     }

     public void setActive(boolean active) {
        this.active = active;
     }

    public void setInMatch(boolean inMatch) {
        this.inMatch = inMatch;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isInMatch() {
        return inMatch;
    }

    public void setImageIconLink(String imageIconLink) {
        this.imageIconLink = imageIconLink;
    }

    @Override
    public String toString() {
        return "Account[Username = " + username + ", Password = " + password + ", FullName = " + fullName
                + ", " + birthday.toString() + ", Male = " + male + ", isActive = " + active + ", isInMatch = " + inMatch + "]";
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public boolean isMale() {
        return male == Sex.MALE;
    }

    public Date getDOB() {
        return birthday;
    }


    public String getImageIconLink() {
        return imageIconLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return username.equals(account.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, fullName, birthday, male, winMatch, lossMatch, imageIconLink, active, inMatch);
    }
}
