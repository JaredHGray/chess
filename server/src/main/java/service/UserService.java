package service;

import model.UserData;
import model.AuthData;
import dataAccess.DataAccessException;
public class UserService {

    private final DataAccessException dataAccess;

    public UserService(DataAccessException dataAccess) {
        this.dataAccess = dataAccess;
    }

//    public UserService() {
//        public AuthData register(UserData user) {}
//        public AuthData login(UserData user) {}
//        public void logout(UserData user) {}
//    }

}
