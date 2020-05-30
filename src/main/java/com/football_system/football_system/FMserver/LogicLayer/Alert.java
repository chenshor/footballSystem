package com.football_system.football_system.FMserver.LogicLayer;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.football_system.football_system.FMserver.DataLayer.IDataManager;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
@Entity
@EnableAutoConfiguration
public class Alert implements Serializable {
    @Id
    private int A_id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "U_id")
    private User user;
    private String description;
    String date;
    private boolean readed;
    @Transient
    @JsonIgnore
    private static int Id_Generator = 2;

    // hour is missing

//    @Override
//    public String toString() {
//        return "MessageModel{" +
//                "message='" + description + '\'' +
//                ", date='" + date + '\'' +
//                ", readed='" + readed + '\'' +
//                '}';
//    }

    private static IDataManager data(){
        return DataComp.getInstance();
    }


    public Alert(User user, String description, String date) {
        this.user = user;
        this.description = description;
        this.date = date;
        A_id = Id_Generator;
        Id_Generator = Id_Generator + 10;
    }

    public Alert(User user, String description, String date , boolean readed) {
        this.user = user;
        this.description = description;
        this.date = date;
        this.readed = readed;
        A_id = Id_Generator;
        Id_Generator = Id_Generator + 10;
    }

    public Alert(){}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        data().addAlert(this);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        data().addAlert(this);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        data().addAlert(this);
    }

    public boolean isReaded() {
        return readed;
    }

    public void setReaded(boolean readed) {
        this.readed = readed;
        data().addAlert(this);
    }

    public int getA_id() {
        return A_id;
    }

    public void setA_id(int a_id) {
        A_id = a_id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.A_id == ((Alert)obj).A_id)
            return true;
        return false;
    }

}
