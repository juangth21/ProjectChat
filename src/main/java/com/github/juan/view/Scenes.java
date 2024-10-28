package com.github.juan.view;

public enum Scenes {
    LAYOUT("view/layout.fxml"),
    MAIN("view/Main.fxml"),
    REGISTER("view/Register.fxml"),
    LOGIN("view/Login.fxml");

    private String url;

    Scenes(String url) {
        this.url = url;
    }

    public String getURL() {
        return url;
    }
}
