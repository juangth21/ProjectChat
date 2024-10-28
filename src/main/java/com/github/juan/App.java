package com.github.juan;

import com.github.juan.view.AppController;
import com.github.juan.view.Scenes;
import com.github.juan.view.View;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    public static Scene scene;
    public static Stage stage;
    public static AppController currentController;

    @Override
    public void start(Stage stage) throws Exception {
        View view = AppController.loadFXML(Scenes.LAYOUT);
        scene = new Scene(view.scene, 1920, 1080);
        currentController = (AppController) view.controller;
        currentController.onOpen(null);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
    }

    public static void main(String[] args) {
        launch();
    }
}

