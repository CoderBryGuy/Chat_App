package com.example.chat_app;

public class ModelClass {

    String message;
    String from;

    public ModelClass() {}

    public ModelClass(String messages, String from) {
        this.message = messages;
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }


}
