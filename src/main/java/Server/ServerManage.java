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

    public boolean isContain(String username) {
        if (db.getAccount(username) != null) return true;
        return false;
    }

    public Account getAccount(String username) {
        return db.getAccount(username);
    }

    public void updateActiveState(String username, boolean isActive) {
        db.UPDATE_ACTIVE_STATE(username, isActive);
    }

    public void updateInMatchState(String username, boolean isInMatch) {
        db.UPDATE_IN_GAME_STATE(username, isInMatch);
    }

    public Room createRoom(String usernameOfOwner, int ownerPort) {
        Room newRoom = new Room(++number_of_room, 1, usernameOfOwner, ownerPort);
        db.INSERT_ROOM(newRoom);
        number_of_room++;
        return newRoom;
    }

    public List<Room> getRoomList() {
        return db.getRooms();
    }

    public int getNumberOfRoom() {
        return number_of_room;
    }
}
