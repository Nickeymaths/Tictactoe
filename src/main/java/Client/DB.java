package Client;

import IndividualInformation.Account;
import java.io.ObjectInputStream;
import java.sql.*;

public class DB {
    private Connection connection;
    private String url = "jdbc:mysql://localhost:3306/mydata";
    private PreparedStatement preparedStatement;

    public DB() {
        connection = getConnect();
        try {
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO Account(username, Data) Value(?,?)");
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
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setObject(2, account);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error to insert");
        }

    }

    public Account getAccount(String username) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet result =  statement.executeQuery("SELECT Data FROM Account WHERE username = '" + username + "';");
            if (result.next()) {
                ObjectInputStream objInput = new ObjectInputStream(result.getBinaryStream("Data"));
                return (Account) objInput.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error to get Account");
        }
        return null;
    }

    public boolean isAlreadyHasUsername(String username) {
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT username FROM Account WHERE username = '" + username + "';");
            if (result.next()) {
                return true;
            }
        } catch(SQLException e) {
            System.out.println("Error to select");
        }
        return false;
    }
}
