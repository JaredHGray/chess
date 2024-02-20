package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import model.UserData;
import model.AuthData;
import dataAccess.UserDAO;
public class UserService {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }
    public UserData addUser(UserData registerUser) throws DataAccessException{
        if(userDAO.getUser(registerUser) == null){
            userDAO.addUser(registerUser);
            authDAO.createAuth(registerUser.username());
        }
        return null;
    }

}
