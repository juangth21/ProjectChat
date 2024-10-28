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
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController extends Controller implements Initializable {

    @FXML
    TextField username;
    @FXML
    TextField name;
    @FXML
    PasswordField password;
    @FXML
    TextField email;
    @FXML
    Button registrarse;

    @FXML
    ImageView back;

    @Override
    public void onOpen(Object input) throws IOException {}

    @Override
    public void onClose(Object output) {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    public User getValues() {
        String usernames = username.getText();
        String names = name.getText().trim();
        String passwords = password.getText().trim();
        String emails = email.getText();
        return new User(usernames, names, passwords, emails);
    }

    public void Register() throws IOException {
        try {
            User user = getValues();

            if (user.getUsername().isEmpty() || user.getName().isEmpty() || user.getPassword().isEmpty() || user.getEmail().isEmpty()) {
                AppController.ShowAlertsErrorRegister();
                return;
            }

            if (!isValidEmail(user.getEmail())) {
                AppController.ShowAlertsInvalidEmail();
                return;
            }

            if (UserXMLManager.exists(user.getUsername())) {
                AppController.ShowAlertsUserAlreadyExists();
                return;
            }


            String hashedPassword = PasswordHasher.hashPassword(user.getPassword());
            user.setPassword(hashedPassword);


            UserXMLManager.addUser(user);
            UserSession.login(user);
            changeSceneToLoginPage();
            AppController.ShowAlertsSuccessfullyRegister();

        } catch (Exception e) {
            e.printStackTrace();
            AppController.ShowAlertsErrorRegister();
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    public void changeSceneToLoginPage() throws Exception {
        App.currentController.changeScene(Scenes.LOGIN, null);
    }
}
