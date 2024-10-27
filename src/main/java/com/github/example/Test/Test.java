package com.github.example.Test;

import com.github.example.model.Entity.Contact;
import com.github.example.model.Entity.User;
import com.github.example.model.XML.XMLUser;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        try {
            XMLUser xmlUserHandler = new XMLUser();

            List<Contact> contactosJuan = new ArrayList<>();
            Contact contact1 = new Contact("Juan Serrano", "juanserrano@example.com", "juan serrano");
            Contact contact2 = new Contact("Alfonso", "alfonso@example.com", "alfonso");
            Contact contact3 = new Contact("JJ", "jj@example.com", "jj");
            contactosJuan.add(contact1);
            contactosJuan.add(contact2);
            contactosJuan.add(contact3);


            User user1 = new User("Juan", "juan@example.com", "juan", "serranocastro",contactosJuan);


            List<Contact> contactosMaria = new ArrayList<>();
            Contact contact4 = new Contact("Luis Fernandez", "luis.fernandez@example.com", "luisito");
            Contact contact5 = new Contact("Elena Gomez", "elena.gomez@example.com", "elenita");
            contactosMaria.add(contact4);
            contactosMaria.add(contact5);


            User user2 = new User("Mariaaa", "maria@", "mariquilla", "password123", contactosMaria);


            xmlUserHandler.agregarUsuario(user1);
            xmlUserHandler.agregarUsuario(user2);


            List<User> usuariosGuardados = xmlUserHandler.obtenerUsuarios();
            for (User user : usuariosGuardados) {
                System.out.println(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

