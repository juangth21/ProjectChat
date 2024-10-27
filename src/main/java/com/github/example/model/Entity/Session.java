package com.github.example.model.Entity;

public class Session {

    private static Session _instance;
    private static User userLoged;

    private Session() {
    }

    public static Session getInstancia() {
        if (_instance == null) {
            _instance = new Session();
            _instance.logIn(userLoged);
        }
        return _instance;
    }

    public void logIn(User user) {
        userLoged = user;
    }

    public User getUsuarioIniciado() {
        return userLoged;
    }

    public void logOut() {
        userLoged = null;
    }
}


