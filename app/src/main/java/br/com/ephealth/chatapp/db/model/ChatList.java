package br.com.ephealth.chatapp.db.model;

public class ChatList {
    public String id;

    public ChatList() {
    }

    public ChatList(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
