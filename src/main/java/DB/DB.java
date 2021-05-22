package DB;

import Client.Main;
import IndividualInformation.Account;
import IndividualInformation.Date;
import Server.Room;
import com.mysql.cj.protocol.Resultset;
import javafx.scene.image.Image;
import javafx.util.Pair;

import java.io.ObjectInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DB {
    private Connection connection;
    private String url = "jdbc:mysql://localhost:3306/mydata";

    public DB() {
        connection = getConnect();
    }

    public Connection getConnect() {
        try {
            Connection con = DriverManager.getConnection(url, "root", "0904235269");
            return con;
        } catch (SQLException e) {
            System.out.println("Can't connect to mysql");
        }
        return null;
    }

    public void INSERT(Account account) {
        try {
            PreparedStatement preparedStatement_accountTable = connection.prepareStatement(
                    "INSERT INTO Account(Username, Password, FullName, " +
                            "DOB, Male, num_win, num_loss, isOnline, isPlaying, avatar) Value(?,?,?,?,?,?,?,?,?,?)");
            preparedStatement_accountTable.setString(1, account.getUsername());
            preparedStatement_accountTable.setString(2, account.getPassword());
            preparedStatement_accountTable.setString(3, account.getFullName());
            preparedStatement_accountTable.setString(4, account.getDOB().toString());
            preparedStatement_accountTable.setBoolean(5, account.isMale());
            preparedStatement_accountTable.setInt(6, account.getWinMatch());
            preparedStatement_accountTable.setInt(7, account.getLossMatch());
            preparedStatement_accountTable.setBoolean(8, account.isActive());
            preparedStatement_accountTable.setBoolean(9, account.isInMatch());
            preparedStatement_accountTable.setString(10, account.getImageIconLink());
            preparedStatement_accountTable.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error to insert new account");
        }

    }

    public Account getAccount(String username) {
        try {
            PreparedStatement statement
                    = connection.prepareStatement("SELECT* FROM Account WHERE username = '" + username + "';");
            ResultSet result =  statement.executeQuery();
            if (result.next()) {
                String password = result.getString("Password");
                String fullName = result.getString("FullName");
                Date birthday = new Date(result.getString("DOB"));
                boolean isMale = result.getBoolean("Male");
                int winMatch = result.getInt("num_win");
                int lossMatch = result.getInt("num_loss");
                boolean active = result.getBoolean("isOnline");
                boolean inMatch = result.getBoolean("isPlaying");
                String avatar = result.getString("avatar");
                Account account = new Account(username, password, fullName, isMale, birthday, winMatch, lossMatch, active, inMatch);
                account.setImageIconLink(avatar);
                return account;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error to get Account");
        }
        return null;
    }

    public void UPDATE_ACTIVE_STATE(String username, boolean state) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate("update account set isOnline = " + state
                    + " where username = '" + username + "';");
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Error to update isOnline state");
        }
    }

    public void UPDATE_IN_GAME_STATE(String username, boolean state) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate("update account set isPlaying = " + state
                    + " where username = '" + username + "';");
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Error to update active state");
        }
    }

    public List<Account> getActiveAccount() {
        List<String> usernameList = null;
        List<Account> activeAccountList = null;
        try {
            usernameList = new ArrayList<>();
            activeAccountList = new ArrayList<>();

            PreparedStatement statement = connection.prepareStatement("SELECT Username FROM Account WHERE isOnline = true;");
            ResultSet result =  statement.executeQuery();
            while (result.next()) {
                Account account = getAccount(result.getString("Username"));
                if (!account.equals(Main.currentAccount)) activeAccountList.add(account);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error to get active account");
        }
        return activeAccountList;
    }

    public void INSERT(Room room) {
        try {
            PreparedStatement preparedStatement_roomTable = connection.prepareStatement(
                    "INSERT INTO Room(Id, Amount, OwnerUsername, AvatarOfOwner, " +
                            "OtherUsername, AvatarOfOther, OwnerPort, OtherPort) Value(?,?,?,?,?,?,?,?)");
            preparedStatement_roomTable.setInt(1, room.getId());
            preparedStatement_roomTable.setInt(2, room.getAmount());
            preparedStatement_roomTable.setString(3, room.getUsernameOfOwner());
            preparedStatement_roomTable.setString(4,room.getAvatarOfOwner());
            preparedStatement_roomTable.setString(5, room.getUsernameOfOther());
            preparedStatement_roomTable.setString(6, room.getAvatarOfOther());
            preparedStatement_roomTable.setInt(7, room.getOwnerPort());
            preparedStatement_roomTable.setInt(8, room.getOtherPort());
            preparedStatement_roomTable.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error to insert new room");
        }
    }

    public void DELETE(Room room) {
        try {
            PreparedStatement statement = connection.prepareStatement(
              "DELETE FROM Room WHERE Id = " + room.getId()
            );
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Error to delete room");
        }
    }

    public void UPDATE(Account account) {
        try {
            System.out.println(account.getUsername());
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE account SET "
                            + "FullName = '" + account.getFullName()
                            + "', DOB = '" + account.getDOB()
                            + "', Male = " + account.isMale()
                            + ", num_win = " + account.getWinMatch()
                            + ", num_loss = " + account.getLossMatch()
                            + ", isOnline = " + account.isActive()
                            + ", isPlaying = " + account.isInMatch()
                            + " Where Username = '" + account.getUsername() + "'"
            );
            statement.executeUpdate();
        } catch(SQLException e) {
            System.out.println("Error to update room");
            e.printStackTrace();
        }
    }

    public void UPDATE(Room room) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE Room SET "
                            + "Id = " + room.getId()
                            + ", Amount = " + room.getAmount()
                            + ", OwnerUsername = '" + room.getUsernameOfOwner()
                            + "', AvatarOfOwner = '" + room.getAvatarOfOwner()
                            + "', OtherUsername = '" + room.getUsernameOfOther()
                            + "', AvatarOfOther = '" + room.getAvatarOfOther()
                            + "', OwnerPort = " + room.getOwnerPort()
                            + ", OtherPort = " + room.getOtherPort()
                    + " WHERE Id = " + room.getId()
            );
            statement.executeUpdate();
        } catch(SQLException e) {
            System.out.println("Error to update room");
            e.printStackTrace();
        }
    }

    public Room getRoom(int roomId) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT* FROM Room WHERE Id = " + roomId
            );
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return new Room(
                        result.getInt("Id"),
                        result.getInt("Amount"),
                        result.getString("OwnerUsername"),
                        result.getString("AvatarOfOwner"),
                        result.getString("OtherUsername"),
                        result.getString("AvatarOfOther"),
                        result.getInt("OwnerPort"),
                        result.getInt("OtherPort")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error to select room");
            e.printStackTrace();
        }
        return null;
    }

    public List<Room> getRooms() {
        List<Room> roomList = null;
        try {
            roomList = new ArrayList<>();

            PreparedStatement statement = connection.prepareStatement("SELECT* FROM Room;");
            ResultSet result =  statement.executeQuery();
            while (result.next()) {
                int id = result.getInt("Id");
                int amount = result.getInt("Amount");
                String usernameOfOwner = result.getString("OwnerUsername");
                String avatarOfOwner = result.getString("AvatarOfOwner");
                String usernameOfOther = result.getString("OtherUsername");
                String avatarOfOther = result.getString("AvatarOfOther");
                int ownerPort = result.getInt("OwnerPort");
                int otherPort = result.getInt("OtherPort");
                Room room = new Room(id, amount, usernameOfOwner, avatarOfOwner, usernameOfOther,
                        avatarOfOther, ownerPort, otherPort);
                roomList.add(room);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error to get active account");
        }
        return roomList;
    }

    public void INSERT(int socketPort) {
        try {
            PreparedStatement preparedStatement_socketPort
                    = connection.prepareStatement("INSERT INTO client_socket(Port) VALUE(?)");
            preparedStatement_socketPort.setInt(1, socketPort);
            preparedStatement_socketPort.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void DELETE(int socketPort) {
        try{
            PreparedStatement statement = connection.prepareStatement("DELETE FROM client_socket WHERE Port = "
                    + socketPort);
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isContains(int socketPort) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT Port FROM client_socket WHERE Port = " + socketPort);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}