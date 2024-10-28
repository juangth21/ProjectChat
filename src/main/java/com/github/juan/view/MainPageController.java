package com.github.juan.view;

import com.github.juan.App;
import com.github.juan.Model.XML.MessageXMLManager;
import com.github.juan.Model.XML.UserXMLManager;
import com.github.juan.Model.entity.Contact;
import com.github.juan.Model.entity.Message;
import com.github.juan.Model.entity.User;
import com.github.juan.Utils.UserSession;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class MainPageController extends Controller implements Initializable {

    @FXML
    private Text username;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox contacts;
    @FXML
    private Button buttonNewContact;
    @FXML
    private TextField usernameField;
    @FXML
    private ListView<Contact> contactListView;
    @FXML
    private TextField messageTextField;
    @FXML
    private Button buttonSend;
    @FXML
    private ImageView back;
    @FXML
    private VBox messagesVBox;
    @FXML
    private Button AnalyzeButton;

    private String selectedContactUsername;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupContactListView();
        addContactSelectionListener();
    }

    @Override
    public void onOpen(Object input) throws Exception {
        User loggedInUser = UserSession.getCurrentUser();
        loadContacts(loggedInUser);
    }

    @Override
    public void onClose(Object output) {
    }

    public void addNewContact() throws Exception {
        User loggedInUser = UserSession.getCurrentUser();
        if (loggedInUser == null) {
            return;
        }
        Contact contact = validateContact();
        if (contact == null) {
            AppController.ShowAlertsContactNotFound();
            return;
        }

        if (loggedInUser.getContacts().contains(contact)) {
            AppController.ShowAlertsContactAlreadyExists();
            return;
        }

        loggedInUser.getContacts().add(contact);
        AppController.ShowAlertsSuccessfullyAddedContact();

        List<User> users = UserXMLManager.getUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(loggedInUser.getUsername())) {
                users.set(i, loggedInUser);
                break;
            }
        }
        UserXMLManager.saveUsers(users);
        loadContacts(loggedInUser);
    }

    private Contact validateContact() throws Exception {
        String username = usernameField.getText();
        List<User> users = UserXMLManager.getUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return new Contact(user.getEmail(), user.getName(), user.getUsername());
            }
        }
        return null;
    }

    private void loadContacts(User user) throws Exception {
        contactListView.getItems().clear();
        if (user.getContacts() != null) {
            contactListView.getItems().addAll(FXCollections.observableArrayList(user.getContacts()));
        }
    }

    private void setupContactListView() {
        contactListView.setCellFactory(param -> new ListCell<Contact>() {
            @Override
            protected void updateItem(Contact item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getUsername());
            }
        });
    }

    private void addContactSelectionListener() {
        contactListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedContactUsername = newValue.getUsername();
                username.setText(selectedContactUsername);
                try {
                    showMessages(newValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showMessages(Contact contact) throws Exception {
        messagesVBox.getChildren().clear();
        User currentUser = UserSession.getCurrentUser();
        if (currentUser == null) {
            return;
        }

        List<Message> messages = MessageXMLManager.recoverMessages();
        boolean messagesFound = false;

        for (Message message : messages) {
            Label messageLabel = new Label();

            if ((message.getRecipient().equals(contact.getUsername()) && message.getSender().equals(currentUser.getUsername())) ||
                    (message.getSender().equals(contact.getUsername()) && message.getRecipient().equals(currentUser.getUsername()))) {

                messageLabel.setText(message.getTimestamp() + " - " + message.getSender() + ": " + message.getContent());
                messageLabel.setWrapText(true);
                messageLabel.setStyle("-fx-padding: 10;");

                if (message.getSender().equals(currentUser.getUsername())) {
                    messageLabel.setStyle(messageLabel.getStyle() + "-fx-text-fill: grey;");
                } else {
                    messageLabel.setStyle(messageLabel.getStyle() + "-fx-text-fill: black;");
                }
                messagesVBox.getChildren().add(messageLabel);
                messagesFound = true;
            }
        }
        if (!messagesFound) {
            Label noMessagesLabel = new Label("Mensajes no disponibles");
            messagesVBox.getChildren().add(noMessagesLabel);
        }
    }

    public void sendMessage() throws Exception {
        String messageText = messageTextField.getText();

        if (messageText.isEmpty()) {
            AppController.ShowAlertsErrorMessageEmpty();
            return;
        }

        Contact recipientContact = findContactByUsername(selectedContactUsername);

        if (recipientContact == null) {
            AppController.ShowAlertsErrorContactEmpty();
            return;
        }
        User sender = UserSession.getCurrentUser();
        if (sender == null) {
            return;
        }
        Message newMessage = new Message(sender.getUsername(), recipientContact.getUsername(), messageText);


        if (recipientContact.getMessages() == null) {
            recipientContact.setMessages(new ArrayList<>());
        }
        recipientContact.getMessages().add(newMessage);
        MessageXMLManager.addMessage(newMessage);
        UserXMLManager.saveUsers(UserXMLManager.getUsers());
        showMessages(recipientContact);
        messageTextField.clear();
    }

    private Contact findContactByUsername(String username) throws Exception {
        List<User> users = UserXMLManager.getUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return new Contact(user.getEmail(), user.getName(), user.getUsername());
            }
        }
        return null;
    }

    public void exportConversation() {
        User currentUser = UserSession.getCurrentUser();
        if (currentUser == null) {
            return;
        }
        Contact selectedContact = getSelectedContact();
        if (selectedContact == null) {
            AppController.ShowAlertsErrorContact();
            return;
        }
        List<Message> messages;
        try {
            messages = MessageXMLManager.recoverMessages();
        } catch (Exception e) {
            AppController.ShowAlertsErrorMessage();
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar chat");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(null);
        if (file == null) {
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Message message : messages) {
                if ((message.getRecipient().equals(selectedContact.getUsername()) && message.getSender().equals(currentUser.getUsername())) ||
                        (message.getSender().equals(selectedContact.getUsername()) && message.getRecipient().equals(currentUser.getUsername()))) {

                    writer.write(message.getTimestamp() + " - " + message.getSender() + ": " + message.getContent());
                    writer.newLine();
                }
            }
            AppController.ShowAlertsSuccessfullyGeneratedtxt();
        } catch (IOException e) {
            AppController.ShowAlertsErrorGeneratedtxt();
        }
    }

    public void analyzeConversation() {
        User currentUser = UserSession.getCurrentUser();
        if (currentUser == null) {
            return;
        }
        Contact selectedContact = getSelectedContact();
        if (selectedContact == null) {
            AppController.ShowAlertsErrorContact();
            return;
        }
        List<Message> messages;
        try {
            messages = MessageXMLManager.recoverMessages();
        } catch (Exception e) {
            AppController.ShowAlertsErrorMessage();
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(null);
        if (file == null) {
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            long totalMessages = messages.stream()
                    .filter(message -> (message.getRecipient().equals(selectedContact.getUsername()) && message.getSender().equals(currentUser.getUsername())) ||
                            (message.getSender().equals(selectedContact.getUsername()) && message.getRecipient().equals(currentUser.getUsername())))
                    .count();
            writer.write("Total: " + totalMessages + "\n");

            Map<String, Long> messagesPerUser = messages.stream()
                    .filter(message -> (message.getRecipient().equals(selectedContact.getUsername()) && message.getSender().equals(currentUser.getUsername())) ||
                            (message.getSender().equals(selectedContact.getUsername()) && message.getRecipient().equals(currentUser.getUsername())))
                    .collect(Collectors.groupingBy(Message::getSender, Collectors.counting()));
            writer.write("Mensajes:\n");
            messagesPerUser.forEach((user, count) -> {
                try {
                    writer.write("Usuario: " + user + ", Mensajes: " + count + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            Map<String, Long> wordFrequency = messages.stream()
                    .filter(message -> (message.getRecipient().equals(selectedContact.getUsername()) && message.getSender().equals(currentUser.getUsername())) ||
                            (message.getSender().equals(selectedContact.getUsername()) && message.getRecipient().equals(currentUser.getUsername())))
                    .flatMap(message -> Arrays.stream(message.getContent().split("\\W+")))
                    .map(String::toLowerCase)
                    .collect(Collectors.groupingBy(word -> word, Collectors.counting()));

            writer.write("Palabras más repetidas:\n");
            wordFrequency.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(10)
                    .forEach(entry -> {
                        try {
                            writer.write("Palabra: " + entry.getKey() + ", Frecuencia: " + entry.getValue() + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

            AppController.ShowAlertsSuccessfullyGeneratedtxt();
        } catch (IOException e) {
            AppController.ShowAlertsErrorGeneratedtxt();
        }
    }

    private Contact getSelectedContact() {
        return contactListView.getSelectionModel().getSelectedItem();
    }
    public void changeSceneToLoginPage() throws Exception {
        App.currentController.changeScene(Scenes.LOGIN, null);
    }
}