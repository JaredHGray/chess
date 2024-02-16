package service;

import dataAccess.DataAccessException;
import model.UserData;
import model.AuthData;
import dataAccess.UserDAO;
public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserData addUser(UserData registerUser) throws DataAccessException{
            return userDAO.addUser(registerUser);
    }

}
