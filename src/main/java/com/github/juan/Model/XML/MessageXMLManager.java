package com.github.juan.Model.XML;

import com.github.juan.Model.entity.Message;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MessageXMLManager {
    private static File messagesFile = new File("messages.xml");

    public static void addMessage(Message message) throws Exception {
        List<Message> messages = recoverMessages();
        messages.add(message);
        saveMessages(messages);
    }

    public static List<Message> recoverMessages() throws JAXBException {
        if (messagesFile.exists() && messagesFile.length() > 0) {
            JAXBContext context = JAXBContext.newInstance(MessageWrapper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            MessageWrapper wrapper = (MessageWrapper) unmarshaller.unmarshal(messagesFile);
            return wrapper.getMessages();
        } else {
            return new ArrayList<>();
        }
    }

    public static void saveMessages(List<Message> messages) throws Exception {
        MessageWrapper wrapper = new MessageWrapper();
        wrapper.setMessages(messages);

        JAXBContext context = JAXBContext.newInstance(MessageWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(wrapper, messagesFile);
    }
}
