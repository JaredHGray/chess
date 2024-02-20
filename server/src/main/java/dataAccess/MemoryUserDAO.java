package dataAccess;

import chess.ChessMove;
import model.UserData;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MemoryUserDAO implements UserDAO {
    //do i need some sort of hash map?\
    //yes for storage and retrieval
    Set<UserData> users = new HashSet<>();
    public UserData addUser(UserData registerUser) throws DataAccessException {

        registerUser = new UserData(registerUser.username(), registerUser.password(), registerUser.email());
        users.add(registerUser);
        return registerUser; //do i return success or make this void?
    }

    public UserData getUser(UserData registerUser) throws DataAccessException {
        return null;
    }

}
