package com.github.example.Test;

import com.github.example.model.Entity.Contact;
import com.github.example.model.Entity.Message;
import com.github.example.model.Entity.User;
import com.github.example.model.XML.XMLMessage;

import java.time.LocalDateTime;
import java.util.List;

public class Test2 {
    public static void main(String[] args) throws Exception {
        XMLMessage xmlMessage = new XMLMessage();

        LocalDateTime now = LocalDateTime.now();

        User user1 = new User("juan");
        User user2 = new User( "alfonso");

        Contact contact = new Contact();
        Contact contact2 = new Contact();

        Message mensaje1 = new Message(now,"hello", contact, contact2);

        xmlMessage.addMessage(mensaje1);

        List<Message> mensagge = xmlMessage.recoverMessages();
        for (Message m : mensagge){
            System.out.println(m);
        }
    }
}
