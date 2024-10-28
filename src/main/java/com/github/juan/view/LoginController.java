package com.github.juan.view;

import com.github.juan.App;

import com.github.juan.Model.XML.UserXMLManager;
import com.github.juan.Model.entity.User;
import com.github.juan.Utils.UserSession;
import com.github.juan.Utils.PasswordHasher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends Controller implements Initializable {
    @FXML
    TextField username;

    @FXML
    PasswordField password;

    @FXML
    Button button;

    @FXML
    Button buttonRegister;

    @Override
    public void onOpen(Object input) throws IOException {

    }

    @Override
    public void onClose(Object output) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public User getValues() {
        User user = new User();
        String usernames = username.getText().trim();
        String passwords = password.getText().trim();
        if (!usernames.isEmpty() && !passwords.isEmpty()) {
            user.setUsername(usernames);
            user.setPassword(passwords);
            return user;
        } else {
            return null;
        }
    }

    public void Login() throws IOException {
        User user = getValues();
        if (user == null) {
            AppController.ShowAlertsErrorLogin2();
            return;
        }

        try {
            String hashedPasswordInput = PasswordHasher.hashPassword(user.getPassword());
            User loggedInUser = UserXMLManager.validateUserCredentials(user.getUsername(), hashedPasswordInput);

            UserSession.login(loggedInUser);
            changeSceneToMainPage();

        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());

            if (e.getMessage().equals("Contraseña incorrecta.")) {
                AppController.ShowAlertsErrorLoginPassword();
            } else if (e.getMessage().equals("Usuario no encontrado.")) {
                AppController.ShowAlertsErrorLogin();
            }
        }
    }

    public void changeSceneToMainPage() throws Exception {
        App.currentController.changeScene(Scenes.MAIN,null);
    }

    public void changeSceneToRegister() throws Exception {
        App.currentController.changeScene(Scenes.REGISTER,null);
    }
}