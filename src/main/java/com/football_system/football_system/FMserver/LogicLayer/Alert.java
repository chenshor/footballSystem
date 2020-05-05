package com.football_system.football_system.FMserver.LogicLayer;


import java.io.Serializable;
import java.util.Date;

public class Alert implements Serializable {

    private User user;
    private String description;
    String date;
    // hour is missing

    public Alert(User user, String description, String date) {
        this.user = user;
        this.description = description;
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
