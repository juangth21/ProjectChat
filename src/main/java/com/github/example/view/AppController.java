package com.github.example.view;

import com.github.example.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AppController extends Controller implements Initializable {


    @FXML
    private BorderPane bp;
    private Controller centerController;
    @FXML
    static Alert alertError = new Alert(Alert.AlertType.ERROR);

    @FXML
    public static void showAlertForLogin() {
        alertError.setTitle("Error");
        alertError.setHeaderText(null);
        alertError.setContentText("Inténtelo de nuevo.");
        alertError.showAndWait();
    }

    @FXML
    public static void showAlertForContactSelected() {
        alertError.setTitle("Error");
        alertError.setHeaderText(null);
        alertError.setContentText("Contacto no seleccionado.");
        alertError.showAndWait();
    }
    @FXML
    public static void showAlertForPassword() {
        alertError.setTitle("Error");
        alertError.getDialogPane().setPrefWidth(700);
        alertError.getDialogPane().setPrefHeight(160);
        alertError.setHeaderText(null);
        alertError.setContentText("- La contraseña no cumple con los requisitos.");
        alertError.showAndWait();
    }
    @FXML
    public static void showAlertForEmail() {
        alertError.setTitle("Error");
        alertError.getDialogPane().setPrefWidth(700);
        alertError.getDialogPane().setPrefHeight(160);
        alertError.setHeaderText(null);
        alertError.setContentText("- El correo electrónico no cumple con los requisitos.");
        alertError.showAndWait();
    }
    @FXML
    public static void showAlertForAddContact() {
        alertError.setTitle("Error");
        alertError.getDialogPane().setPrefWidth(700);
        alertError.getDialogPane().setPrefHeight(160);
        alertError.setHeaderText(null);
        alertError.setContentText("Contacto inexistente.");
        alertError.showAndWait();
    }
    @FXML
    public static void showAlertForContactRepetido() {
        alertError.setTitle("Error");
        alertError.getDialogPane().setPrefWidth(700);
        alertError.getDialogPane().setPrefHeight(160);
        alertError.setHeaderText(null);
        alertError.setContentText("Contacto existente.");
        alertError.showAndWait();
    }
    @FXML
    public static void showAlertForNickname() {
        alertError.setTitle("Error");
        alertError.setHeaderText(null);
        alertError.getDialogPane().setPrefWidth(700);
        alertError.getDialogPane().setPrefHeight(160);
        alertError.setContentText("Por favor, revise la información proporcionada, y vuelva a intentarlo:\n\n" +
                "- El nombre de usuario puede estar en uso, por favor elija otro.");
        alertError.showAndWait();
    }


    @Override
    public void onOpen(Object input) throws Exception {
        App.currentController.changeScene(Scenes.INICIOSESION, null);
    }


    @FXML
    public void initialize(URL url, ResourceBundle rb) {
    }

    public static View loadFXML(Scenes scenes) throws Exception {
        String url = scenes.getURL();
        System.out.println(url);
        FXMLLoader loader = new FXMLLoader(App.class.getResource(url));
        Parent p = loader.load();
        Controller c = loader.getController();
        View view = new View();
        view.scene = p;
        view.controller = c;
        return view;
    }


    public void changeScene(Scenes scene, Object data) throws Exception {
        View view = loadFXML(scene);
        bp.setCenter(view.scene);
        this.centerController = view.controller;
        this.centerController.onOpen(data);
    }


    public void openModal(Scenes scene, String title, Controller parent, Object data) throws Exception {
        View view = loadFXML(scene);
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(App.stage);
        Scene _scene = new Scene(view.scene);
        stage.setScene(_scene);
        view.controller.onOpen(parent);
        stage.showAndWait();
    }
}



