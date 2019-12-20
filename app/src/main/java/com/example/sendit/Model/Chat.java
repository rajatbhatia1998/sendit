package com.example.sendit.Model;

public class Chat {
    private String sender;
    private String reciever;
    private String message;
    private boolean isseen;
    public Chat() {
    }



    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public String getMessage() {
        return message;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Chat(String sender, String reciever, String message,boolean isseen) {
        this.sender = sender;
        this.reciever = reciever;
        this.isseen = isseen;
        this.message = message;
    }
}
