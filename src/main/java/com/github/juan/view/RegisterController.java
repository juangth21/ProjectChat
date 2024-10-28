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

    /**
     * Registers a new user with the provided credentials and stores it in an XML file.
     * Displays error alerts if the registration attempt fails due to invalid input or other issues.
     *
     * @throws IOException If an I/O exception occurs while registering the user.
     */
    public void Register() throws IOException {
        try {
            User user = getValues();

            // Verificar si alguno de los campos está vacío
            if (user.getUsername().isEmpty() || user.getName().isEmpty() || user.getPassword().isEmpty() || user.getEmail().isEmpty()) {
                AppController.ShowAlertsErrorRegister();
                return; // Agregar un return aquí para evitar que el código continúe
            }

            // Validar el formato del correo electrónico
            if (!isValidEmail(user.getEmail())) {
                AppController.ShowAlertsInvalidEmail();
                return;
            }

            // Comprobar si el usuario ya existe
            if (UserXMLManager.exists(user.getUsername())) {
                AppController.ShowAlertsUserAlreadyExists();
                return; // Agregar un return aquí para evitar que el código continúe
            }


            String hashedPassword = PasswordHasher.hashPassword(user.getPassword());
            user.setPassword(hashedPassword);

            // Agregar el usuario a XML
            UserXMLManager.addUser(user);
            UserSession.login(user); // Iniciar sesión con el nuevo usuario
            changeSceneToLoginPage(); // Cambiar a la página de inicio de sesión
            AppController.ShowAlertsSuccessfullyRegister(); // Mostrar mensaje de éxito

        } catch (Exception e) {
            e.printStackTrace(); // Imprimir el stack trace para depurar errores
            AppController.ShowAlertsErrorRegister(); // Manejo de error en la interfaz
        }
    }

    /**
     * Checks if the provided email address is in a valid format.
     *
     * @param email The email address to validate.
     * @return True if the email address is valid, false otherwise.
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    public void changeSceneToLoginPage() throws Exception {
        App.currentController.changeScene(Scenes.LOGIN, null);
    }
}
