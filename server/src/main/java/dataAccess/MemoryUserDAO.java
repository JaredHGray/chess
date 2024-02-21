package dataAccess;

import model.UserData;

import java.util.HashSet;
import java.util.Set;

public class MemoryUserDAO implements UserDAO {
    //do i need some sort of hash map?\
    //yes for storage and retrieval
    Set<UserData> users = new HashSet<>();
    public void addUser(UserData registerUser) throws DataAccessException {

        registerUser = new UserData(registerUser.username(), registerUser.password(), registerUser.email());
        users.add(registerUser);
    }

    public UserData getUser(UserData registerUser) throws DataAccessException {
        if (!users.isEmpty()) {
            for (UserData findUser : users) {
                if (findUser.username().equals(registerUser.username())) {
                    return findUser;
                }
            }
        }
        return null;
    }

    public UserData verifyUser(UserData verifyUser) throws DataAccessException {
        if(!users.isEmpty()){
            for (UserData findUser : users) {
                if (findUser.username().equals(verifyUser.username()) && findUser.password().equals(verifyUser.password())) {
                    return findUser;
                }
            }
        }
        return null;
    }
}
