package com.github.juan.Model.XML;

import com.github.juan.Model.entity.Message;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "messages")
public class MessageWrapper {
    private List<Message> messages = new ArrayList<>();

    @XmlElement(name = "message")
    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
