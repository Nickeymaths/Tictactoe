package Server;

import DB.DB;
import IndividualInformation.Account;

import java.util.List;

public class ServerManage {
    private DB db = new DB();
    private int number_of_room = 0;

    public List<Account> getActiveAccountList() {
        return db.getActiveAccount();
    }

    public void insertIntoDB(Account account) {
        db.INSERT(account);
    }

    public void insertIntoDB(int socketPort) {
        db.INSERT(socketPort);
    }

    public void delete(int socketPort) {
        db.DELETE(socketPort);
    }

    public boolean isContains(String username) {
        if (db.getAccount(username) != null) return true;
        return false;
    }

    public boolean isContains(int socketPort) {
        boolean result = db.isContains(socketPort);
        System.out.println(result);
        return result;
    }

    public Account getAccount(String username) {
        return db.getAccount(username);
    }

    public Room getRoom(int roomId) {
        return db.getRoom(roomId);
    }

    public void updateActiveState(String username, boolean isActive) {
        db.UPDATE_ACTIVE_STATE(username, isActive);
    }

    public void updateInMatchState(String username, boolean isInMatch) {
        db.UPDATE_IN_GAME_STATE(username, isInMatch);
    }

    public void updateRoom(Room room) {
        db.UPDATE(room);
    }

    public Room createRoom(String usernameOfOwner, String avatarOfOwner, int ownerPort) {
        // Room id is socket port of owner
        Room newRoom = new Room(ownerPort, 1, usernameOfOwner, avatarOfOwner, ownerPort);
        db.INSERT(newRoom);
        number_of_room++;
        return newRoom;
    }

    public void delete(Room room) {
        db.DELETE(room);
        number_of_room--;
    }

    public List<Room> getRoomList() {
        return db.getRooms();
    }

    public int getNumberOfRoom() {
        return number_of_room;
    }
}
