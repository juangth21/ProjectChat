package com.github.example.view;

import com.github.example.App;
import com.github.example.model.Entity.Session;
import com.github.example.model.Entity.User;
import com.github.example.model.XML.XMLUser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController extends Controller implements Initializable {
    @FXML
    Button buttonIniciarSesion;
    @FXML
    Button buttonRegister;
    @FXML
    TextField nombreUsuario;
    @FXML
    PasswordField contraseña;


    @FXML
    private User takeValuesLogin() throws Exception {
        if (nombreUsuario == null || contraseña == null) {
            throw new NullPointerException("Campos no inicializados");
        }
        String nickname = nombreUsuario.getText();
        String password = contraseña.getText();
        User auxUser = new User();
        auxUser.setNickname(nickname);
        auxUser.setPassword(User.hashPassword(password));
        User fullUser = verifyCredentials(auxUser);
        if (fullUser != null) {
            System.out.println("Credenciales correctas");
            Session.getInstancia().logIn(fullUser);
            goToMainPage();
        } else {
            AppController.showAlertForLogin();
        }
        return fullUser;
    }


    private static User verifyCredentials(User tempUser) throws Exception {
        List<User> usuarios = XMLUser.obtenerUsuarios();
        for (User storedUser : usuarios) {
            System.out.println(storedUser);
            if (tempUser.getNickname().equals(storedUser.getNickname()) &&
                    tempUser.getPassword().equals(storedUser.getPassword())) {
                return storedUser;
            }
        }
        return null;
    }


    @Override
    public void onOpen(Object input) throws Exception {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private void goToMainPage() throws Exception {
        App.currentController.changeScene(Scenes.MAINPAGE, null);
    }

    @FXML
    private void goToRegisterPage() throws Exception {
        App.currentController.changeScene(Scenes.REGISTRO, null);
    }
}
