package com.github.example.view;

import com.github.example.App;

import java.io.IOException;


public abstract class Controller {
    App app;

    public void setApp(App app) {
        this.app = app;
    }

    public abstract void onOpen(Object input) throws Exception;

}


