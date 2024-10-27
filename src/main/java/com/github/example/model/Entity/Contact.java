package com.github.example.model.Entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Contact {

    private String email;
    private String name;
    private String nickname;

    // Constructor vacío
    public Contact() {
    }

    // Constructor con parámetros
    public Contact(String email, String name, String nickname) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;

    }

    @XmlElement
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


}
