package dataAccess;

import model.UserData;

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

    public boolean getUser(UserData registerUser) throws DataAccessException {
        if(users.isEmpty()){
            return false;
        }else{
            for(UserData findUser : users){
                if(findUser.username().equals(registerUser.username())){
                    return false;
                }
            }
            return true;
        }
    }

}
