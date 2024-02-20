package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import model.UserData;
import dataAccess.UserDAO;

import java.util.UUID;

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
            String authToken = generateToken();
            authDAO.createAuth(registerUser.username(), authToken);
        }
        return null; //return combo of username and token
    } //create a result object that can turn anything into a json
    //can also handle errors

    private String generateToken(){
        return UUID.randomUUID().toString();
    }


}
