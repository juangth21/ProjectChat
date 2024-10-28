package com.github.juan.Test;

import com.github.juan.Model.entity.Contact;
import com.github.juan.Model.entity.User;
import com.github.juan.Model.XML.UserXMLManager;


import java.io.File;
import java.util.List;

public class TestUser {
    public static void main(String[] args) {
        try {
            User user1 = new User();
            user1.setUsername("juan");
            user1.setName("juan");
            user1.setPassword("serrano");
            user1.setEmail("juan@gmail.com");

            User user2 = new User();
            user2.setUsername("alfonso");
            user2.setName("alfonso");
            user2.setPassword("serrano");
            user2.setEmail("alfonso@gmail.com");

            UserXMLManager.addUser(user1);
            UserXMLManager.addUser(user2);


            Contact contactForUser1 = new Contact();
            contactForUser1.setUsername("alfonso");
            UserXMLManager.addContactToUser("juan", contactForUser1);

            System.out.println("Registered Users:");
            List<User> users = UserXMLManager.getUsers();
            for (User user : users) {
                System.out.println("Username: " + user.getUsername() +
                        ", Name: " + user.getName() +
                        ", Email: " + user.getEmail() +
                        ", Contacts: " + user.getContacts());
            }

            List<Contact> juanContacts = UserXMLManager.getContactsByUsername("juan");
            System.out.println("Contacts for juan:");
            for (Contact contact : juanContacts) {
                System.out.println("Contact Username: " + contact.getUsername());
            }

            File file = new File("users.xml");
            if (file.exists()) {
                System.out.println("\nThe users.xml file has been created successfully.");
            } else {
                System.out.println("\nThe users.xml file does not exist.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}