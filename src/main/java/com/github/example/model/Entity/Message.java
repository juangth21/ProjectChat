package com.github.example.model.Entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;

@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.FIELD)
public class Message {
     // Adaptador para LocalDateTime
    @XmlElement    public LocalDateTime fecha;
    @XmlElement
    public String text;
    @XmlElement
    public Contact contactEmisor;
    @XmlElement
    public Contact contactReceptor;

    public Message(LocalDateTime fecha, String text, Contact contactEmisor, Contact contactReceptor) {
        this.fecha = fecha;
        this.text = text;
        this.contactEmisor = contactEmisor;
        this.contactReceptor = contactReceptor;
    }

    public Message() {}

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Contact getContactoEmisor() {
        return contactEmisor;
    }

    public void setContactoEmisor(Contact contactEmisor) {
        this.contactEmisor = contactEmisor;
    }

    public Contact getContactoReceptor() {
        return contactReceptor;
    }

    public void setContactoReceptor(Contact contactReceptor) {
        this.contactReceptor = contactReceptor;
    }

    @Override
    public String toString() {
        return "Message{" +
                "fecha=" + fecha +
                ", text='" + text + '\'' +
                ", contactoEmisor=" + contactEmisor +
                ", contactoReceptor=" + contactReceptor +
                '}';
    }
}
