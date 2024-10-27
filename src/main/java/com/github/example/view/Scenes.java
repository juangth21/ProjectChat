package com.github.example.view;

public enum Scenes {

    ROOT("view/Layout.fxml"),
    MAINPAGE("view/Main.fxml"),
    REGISTRO("view/Register.fxml"),
    INICIOSESION("view/Login.fxml");




    private final String url;

    Scenes(String url) {
        this.url = url;
    }

    public String getURL() {
        return url;
    }
}
