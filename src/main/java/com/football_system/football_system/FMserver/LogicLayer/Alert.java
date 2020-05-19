package com.football_system.football_system.FMserver.LogicLayer;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class Alert implements Serializable {

    @JsonIgnore
    private User user;
    private String description;
    String date;
    private boolean readed;

    // hour is missing

//    @Override
//    public String toString() {
//        return "MessageModel{" +
//                "message='" + description + '\'' +
//                ", date='" + date + '\'' +
//                ", readed='" + readed + '\'' +
//                '}';
//    }

    public Alert(User user, String description, String date) {
        this.user = user;
        this.description = description;
        this.date = date;
    }

    public Alert(User user, String description, String date , boolean readed) {
        this.user = user;
        this.description = description;
        this.date = date;
        this.readed = readed;
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

    public boolean isReaded() {
        return readed;
    }

    public void setReaded(boolean readed) {
        this.readed = readed;
    }

}
