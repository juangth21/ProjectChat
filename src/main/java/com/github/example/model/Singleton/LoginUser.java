package com.github.example.model.Singleton;

import com.github.example.model.Entity.User;

public class LoginUser {

    private static LoginUser _instance;
    private User currentUser;

    private LoginUser(User user){
        currentUser=user;
    }

    public static LoginUser getInstance(){
        return _instance;
    }

    public static void getInstance(User userToUse){
        _instance=new LoginUser(userToUse);
    }

    public static void closeSession(){
        _instance=null;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
