package IndividualInformation;

import javafx.scene.image.Image;

import java.io.Serializable;

public class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String fullName;
    private Date birthday;
    private boolean male;
    private int winMatch;
    private int lossMatch;
    private Image imageIcon;

    public Account(String username, String password, String fullName, boolean isMale, String birthday) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.birthday = new Date(birthday);
        this.male = isMale;
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

    @Override
    public String toString() {
        return "Account[Username = " + username + ", Password = " + password + ", FullName = " + fullName
                + ", " + birthday.toString() + ", Male = " + male + "]";
    }

    public String getUsername() {
        return username;
    }
}
