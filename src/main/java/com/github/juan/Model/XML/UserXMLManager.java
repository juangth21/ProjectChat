package com.github.juan.Model.XML;

import com.github.juan.Model.entity.Contact;
import com.github.juan.Model.entity.User;
import com.github.juan.Utils.PasswordHasher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UserXMLManager {
    private static File userFile = new File("users.xml");

    public static void addUser(User user) throws Exception {
        String hashedPassword = PasswordHasher.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        List<User> users = getUsers();
        users.add(user);
        saveUsers(users);
    }

    public static List<User> getUsers() throws Exception {
        if (userFile.exists() && userFile.length() > 0) {
            JAXBContext context = JAXBContext.newInstance(UserListWrapper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            UserListWrapper wrapper = (UserListWrapper) unmarshaller.unmarshal(userFile);
            return wrapper.getUsers() != null ? wrapper.getUsers() : new ArrayList<>();
        } else {
            return new ArrayList<>();
        }
    }

    public static ObservableList<Contact> getContactsByUsername(String username) throws Exception {
        List<User> users = getUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user.getContacts() != null ? FXCollections.observableArrayList(user.getContacts()) : FXCollections.observableArrayList();
            }
        }
        return FXCollections.observableArrayList();
    }

    public static void addContactToUser(String username, Contact contact) throws Exception {
        List<User> users = getUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                if (user.getContacts() == null) {
                    user.setContacts(FXCollections.observableArrayList());
                }
                user.getContacts().add(contact);
                saveUsers(users);
                return;
            }
        }
    }

    public static void saveUsers(List<User> users) throws Exception {
        UserListWrapper wrapper = new UserListWrapper();
        wrapper.setUsers(users);

        JAXBContext context = JAXBContext.newInstance(UserListWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(wrapper, userFile);
    }

    public static User validateUserCredentials(String username, String password) throws Exception {
        List<User> users = getUsers();
        String hashedInputPassword = PasswordHasher.hashPassword(password);
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                if (hashedInputPassword.equals(user.getPassword())) {
                    return user;
                } else {
                    throw new Exception();
                }
            }
        }
        throw new Exception();
    }

    public static boolean exists(String username) throws Exception {
        List<User> users = getUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
}