package dataAccess;

import model.UserData;
import java.util.Collection;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    //do i need some sort of hash map?
    public UserData addUser(UserData registerUser) throws DataAccessException {

        registerUser = new UserData(registerUser.username(), registerUser.password(), registerUser.email());
        return registerUser;
    }

}
