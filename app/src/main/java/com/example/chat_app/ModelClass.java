package com.example.chat_app;

public class ModelClass {

    String messages;
    String from;

    public ModelClass() {}

    public ModelClass(String messages, String from) {
        this.messages = messages;
        this.from = from;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }


}
