package com.example.sendit.Model;

public class Room {
    private String sender;
    private String message;
    private String sender_id;
    public Room(String sender, String message,String sender_id) {
        this.sender = sender;
        this.message = message;
        this.sender_id = sender_id;

    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public Room() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
