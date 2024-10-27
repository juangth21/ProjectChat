package com.github.example.view;

import com.github.example.App;
import com.github.example.model.Entity.Contact;
import com.github.example.model.Entity.Message;
import com.github.example.model.Entity.Session;
import com.github.example.model.Entity.User;
import com.github.example.model.XML.XMLMessage;
import com.github.example.model.XML.XMLUser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.xml.bind.JAXBException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.github.example.model.XML.XMLMessage.archivoTxt;

public class MainController extends Controller implements Initializable {
    @FXML
    ImageView imagenResumen;
    @FXML
    private SplitPane splitpane;
    @FXML
    Text textNombreContacto;
    @FXML
    private ScrollPane scrollPaneMensajes;
    @FXML
    private VBox vBoxMensajes;
    @FXML
    private ImageView imagen;
    @FXML
    private VBox contactos;
    @FXML
    private VBox mensajes;
    @FXML
    private Button buttonAnadirContacto;
    @FXML
    private TextField textField;

    @FXML
    private ListView<Contact> contactListView;

    @FXML
    TextField mensajeTextField;

    @FXML
    private Button buttonAnadirMensaje;

    private String selectedNickname;


    @FXML
    public void obtenerContactos(User usuario) throws Exception {
        if (usuario.getContactos() == null) {
            usuario.setContactos(new ArrayList<>());
        }
        contactListView.getItems().clear();
        List<Contact> contacts = XMLUser.obtenerContactosPorUsuario(usuario);
        if (contacts != null && !contacts.isEmpty()) {
            contactListView.getItems().addAll(contacts);
        } else {
            System.out.println("No hay contactos disponibles.");
        }
    }

    public Contact validarContacto() throws Exception {
        Contact aux = null;
        String nickname = textField.getText();
        List<User> usuarios = XMLUser.obtenerUsuarios();
        for (User user : usuarios) {
            if (user.getNickname().equals(nickname)) {
                System.out.println("Contacto encontrado: " + user.getNickname());
                aux = new Contact();
                aux.setEmail(user.getEmail());
                aux.setName(user.getName());
                aux.setNickname(user.getNickname());
                break;
            }
        }
        return aux;
    }

    public void añadirContactos() throws Exception {
        User usuarioIniciado = Session.getInstancia().getUsuarioIniciado();
        Contact contact = validarContacto();
        if (usuarioIniciado.getContactos() == null) {
            usuarioIniciado.setContactos(new ArrayList<>());
        }
        if (contact == null) {
            AppController.showAlertForAddContact();
            return;
        }
        List<User> usuariosXML = XMLUser.obtenerUsuarios();
        for (User user : usuariosXML) {
            if (user.equals(usuarioIniciado)) {
                usuariosXML.remove(user);
                List<Contact> contactosUser = usuarioIniciado.getContactos();
                for (Contact c : contactosUser) {
                    if (c.getNickname().equals(contact.getNickname())) {
                        AppController.showAlertForContactRepetido();
                        return;
                    }
                }
                contactosUser.add(contact);
                usuarioIniciado.setContactos(contactosUser);
                usuariosXML.add(usuarioIniciado);
                XMLUser.guardarUsuarios(usuariosXML);
                obtenerContactos(usuarioIniciado);
                break;
            }
        }
    }

    @Override
    public void onOpen(Object input) throws Exception {
        User usuario = Session.getInstancia().getUsuarioIniciado();
        obtenerContactos(usuario);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarListView();
        agregarListenerSeleccion();
    }

    private void configurarListView() {
        contactListView.setCellFactory(param -> new ListCell<Contact>() {
            @Override
            protected void updateItem(Contact item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNickname());
            }
        });
    }

    private void agregarListenerSeleccion() {
        contactListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedNickname = newValue.getNickname();
                textNombreContacto.setText(selectedNickname);
                System.out.println("Contacto seleccionado: " + newValue.getNickname());
                User user = null;
                try {
                    user = buscarUsuarioPorNickname(newValue.getNickname());
                    if (user != null) {
                        System.out.println("Usuario encontrado: " + user.toString());
                        mostrarMensajes(user);
                    } else {
                        System.out.println("Usuario no encontrado.");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public String getselectedNickname() {
        return selectedNickname;
    }

    private void mostrarMensajes(User user) {
        vBoxMensajes.getChildren().clear();
        Contact contact = new Contact(user.getEmail(), user.getName(), user.getNickname());
        try {
            List<Message> mensajes = recogerMensajesdeUsuario(contact);

            for (Message message : mensajes) {
                Label mensajeLabel = new Label(message.getText());
                mensajeLabel.setStyle("-fx-padding: 10; -fx-background-radius: 10; -fx-border-radius: 10;");
                mensajeLabel.setWrapText(true);
                mensajeLabel.setMaxWidth(300);
                HBox hbox = new HBox();
                hbox.setPadding(new Insets(5));
                if (message.getContactoEmisor().getNickname().equals(Session.getInstancia().getUsuarioIniciado().getNickname())) {
                    mensajeLabel.setStyle("-fx-background-color: #B39DDB; -fx-padding: 10; -fx-background-radius: 10; -fx-border-radius: 10;");
                    hbox.setAlignment(Pos.CENTER_RIGHT);
                    hbox.getChildren().add(mensajeLabel);
                } else {
                    mensajeLabel.setStyle("-fx-background-color: #E0E0E0; -fx-padding: 10; -fx-background-radius: 10; -fx-border-radius: 10;");
                    hbox.setAlignment(Pos.CENTER_LEFT);
                    hbox.getChildren().add(mensajeLabel);
                }
                vBoxMensajes.getChildren().add(hbox);
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }


    private User buscarUsuarioPorNickname(String nickname) throws Exception {
        List<User> listaUsuarios = XMLUser.obtenerUsuarios();
        User user = new User();
        for (User users : listaUsuarios) {
            if (users.getNickname().equals(nickname)) {
                user = users;
            }
        }
        return user;
    }

    private List<Message> recogerMensajesdeUsuario(Contact contact) throws JAXBException {
        List<Message> mensajes = XMLMessage.recoverMessages();
        List<Message> mensajesFiltrados = new ArrayList<>();
        User usuarioIniciado = Session.getInstancia().getUsuarioIniciado();

        for (Message message : mensajes) {
            if ((message.getContactoEmisor().getNickname().equals(usuarioIniciado.getNickname()) &&
                    message.getContactoReceptor().getNickname().equals(contact.getNickname())) ||

                    (message.getContactoEmisor().getNickname().equals(contact.getNickname()) &&
                            message.getContactoReceptor().getNickname().equals(usuarioIniciado.getNickname()))) {
                mensajesFiltrados.add(message);
            }
        }
        return mensajesFiltrados;
    }


    public void guardarMensaje() throws Exception {
        String mensajeRecogido = mensajeTextField.getText();
        if (mensajeRecogido == null || mensajeRecogido.trim().isEmpty()) {
            return;
        }

        List<Message> mensajesRecogidos = XMLMessage.recoverMessages();
        LocalDateTime ahora = LocalDateTime.now();
        User user = Session.getInstancia().getUsuarioIniciado();
        String nicknameReceptor = getselectedNickname();
        if (nicknameReceptor == null || nicknameReceptor.isEmpty()) {
            AppController.showAlertForContactSelected();
            return;

        }
        User usuarioReceptor = new User();
        List<User> todosUsuarios = XMLUser.obtenerUsuarios();

        for (User user2 : todosUsuarios) {
            if (user2.getNickname().equals(nicknameReceptor)) {
                usuarioReceptor = user2;
                break;
            }
        }
        Contact contactEmisor = new Contact(user.getEmail(), user.getName(), user.getNickname());
        Contact contactorReceptor = new Contact(usuarioReceptor.getEmail(), usuarioReceptor.getName(), usuarioReceptor.getNickname());
        Message message = new Message(ahora, mensajeRecogido, contactEmisor, contactorReceptor);
        mensajesRecogidos.add(message);
        XMLMessage.saveMessages(mensajesRecogidos);
        mensajeTextField.clear();
        mostrarMensajes(usuarioReceptor);
    }

    @FXML
    public void changeSceneToInicioSesion() throws Exception {
        Session.getInstancia().logOut();
        App.currentController.changeScene(Scenes.INICIOSESION, null);
    }

    @FXML
    public void txtMensajes() throws Exception {
        String nickname = getselectedNickname();
        User user = buscarUsuarioPorNickname(nickname);
        List<Message> mensajesTxt = XMLMessage.recoverMessages();
        List<Message> mensajesFiltrados = new ArrayList<>();

        User usuarioIniciado = Session.getInstancia().getUsuarioIniciado();
        String usuarioIniciadoNickname = usuarioIniciado.getNickname();


        for (Message message : mensajesTxt) {
            String emisorNickname = message.getContactoEmisor().getNickname();
            String receptorNickname = message.getContactoReceptor().getNickname();

            if ((emisorNickname.equals(user.getNickname()) && receptorNickname.equals(usuarioIniciadoNickname)) ||
                    (receptorNickname.equals(user.getNickname()) && emisorNickname.equals(usuarioIniciadoNickname))) {
                mensajesFiltrados.add(message);
            }
        }
        if (!mensajesFiltrados.isEmpty()) {
            XMLMessage.convertirXMLaTxt(archivoTxt, mensajesFiltrados);
        } else {
            System.out.println("No hay mensajes entre estos usuarios.");
        }
    }


}