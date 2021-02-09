package DB;

import Client.Main;
import IndividualInformation.Account;
import IndividualInformation.Date;
import Server.Room;
import javafx.scene.image.Image;
import javafx.util.Pair;

import java.io.ObjectInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DB {
    private Connection connection;
    private String url = "jdbc:mysql://localhost:3306/mydata";
    private PreparedStatement preparedStatement_accountTable;
    private PreparedStatement preparedStatement_roomTable;

    public DB() {
        connection = getConnect();
        try {
            preparedStatement_accountTable = connection.prepareStatement(
                    "INSERT INTO Account(Username, Password, FullName, " +
                            "DOB, Male, WinMatch, LossMatch, isActive, isInMatch) Value(?,?,?,?,?,?,?,?,?)");
            preparedStatement_roomTable = connection.prepareStatement(
                    "INSERT INTO Room(Id, Amount, OwnerUsername, " +
                            "OtherUsername, OwnerPort, OtherPort) Value(?,?,?,?,?,?)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
            preparedStatement_accountTable.setString(1, account.getUsername());
            preparedStatement_accountTable.setString(2, account.getPassword());
            preparedStatement_accountTable.setString(3, account.getFullName());
            preparedStatement_accountTable.setString(4, account.getDOB().toString());
            preparedStatement_accountTable.setBoolean(5, account.isMale());
            preparedStatement_accountTable.setInt(6, account.getWinMatch());
            preparedStatement_accountTable.setInt(7, account.getLossMatch());
            preparedStatement_accountTable.setBoolean(8, account.isActive());
            preparedStatement_accountTable.setBoolean(9, account.isInMatch());
            preparedStatement_accountTable.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error to insert new account");
        }

    }

    public Account getAccount(String username) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet result =  statement.executeQuery("SELECT* FROM Account WHERE username = '" + username + "';");
            if (result.next()) {
                String password = result.getString("Password");
                String fullName = result.getString("FullName");
                Date birthday = new Date(result.getString("DOB"));
                boolean isMale = result.getBoolean("Male");
                int winMatch = result.getInt("WinMatch");
                int lossMatch = result.getInt("LossMatch");
                boolean active = result.getBoolean("isActive");
                boolean inMatch = result.getBoolean("isInMatch");
                return new Account(username, password, fullName, isMale, birthday, winMatch, lossMatch, active, inMatch);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error to get Account");
        }
        return null;
    }

//    public Pair<Boolean, Boolean> getAccountState(String username) {
//        Statement statement = null;
//        try {
//            statement = connection.createStatement();
//            ResultSet result =  statement.executeQuery("SELECT isActive, isInGame FROM Account WHERE username = '" + username + "';");
//            if (result.next()) {
//                Boolean isActive = result.getBoolean("isActive");
//                Boolean isInGame = result.getBoolean("isInGame");
//                return new Pair<>(isActive, isInGame);
//            }
//        } catch(Exception e) {
//            System.out.println("Error to determine state");
//        }
//        return null;
//    }

    public void UPDATE_ACTIVE_STATE(String username, boolean state) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate("update account set isActive = " + state
                    + " where username = '" + username + "';");
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Error to update isActive state");
        }
    }

    public void UPDATE_IN_GAME_STATE(String username, boolean state) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate("update account set isInGame = " + state
                    + " where username = '" + username + "';");
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Error to update active state");
        }
    }

    public List<Account> getActiveAccount() {
        Statement statement = null;
        List<String> usernameList = null;
        List<Account> activeAccountList = null;
        try {
            usernameList = new ArrayList<>();
            activeAccountList = new ArrayList<>();

            statement = connection.createStatement();
            ResultSet result =  statement.executeQuery("SELECT Username FROM Account WHERE isActive = true;");
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

    public void INSERT_ROOM(Room room) {
        try {
            preparedStatement_roomTable.setInt(1, room.getId());
            preparedStatement_roomTable.setInt(2, room.getAmount());
            preparedStatement_roomTable.setString(3, room.getUsernameOfOwner());
            preparedStatement_roomTable.setString(4, room.getUsernameOfOther());
            preparedStatement_roomTable.setInt(5, room.getOwnerPort());
            preparedStatement_roomTable.setInt(6, room.getOtherPort());
            preparedStatement_roomTable.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error to insert new room");
        }
    }

    public List<Room> getRooms() {
        Statement statement = null;
        List<Room> roomList = null;
        try {
            roomList = new ArrayList<>();

            statement = connection.createStatement();
            ResultSet result =  statement.executeQuery("SELECT* FROM Room;");
            while (result.next()) {
                int id = result.getInt("Id");
                int amount = result.getInt("Amount");
                String usernameOfOwner = result.getString("OwnerUsername");
                String usernameOfOther = result.getString("OtherUsername");
                int ownerPort = result.getInt("OwnerPort");
                int otherPort = result.getInt("OtherPort");
                Room room = new Room(id, amount, usernameOfOwner, usernameOfOther, ownerPort, otherPort);
                roomList.add(room);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error to get active account");
        }
        return roomList;
    }
}
/*INSERT INTO Room(Id, Amount, OwnerUsername, " +
                            "OtherUsername, OwnerPort, OtherPort) Value(?,?,?,?,?,?)");*/