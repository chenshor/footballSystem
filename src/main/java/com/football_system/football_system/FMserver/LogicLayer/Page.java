package com.football_system.football_system.FMserver.LogicLayer;

import com.football_system.football_system.FMserver.DataLayer.IDataManager;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Entity
@EnableAutoConfiguration
@Table(name = "Pages")
public class Page implements Serializable {
    @Id
    private int Id;
    @OneToMany
    private List<User> followers;
    @Transient
    private RoleHolder roleHolder;
    @Transient
    private List<String> updates;
    @Transient
    private static int Id_Generator = 7;

    public Page(RoleHolder roleHolder) {
        followers = new ArrayList<>();
        updates = new ArrayList<>();
        this.roleHolder = roleHolder;
        Id = Id_Generator;
        Id_Generator = Id_Generator + 10;
    }

    public Page(){}

    private IDataManager data(){
        return DataComp.getInstance();
    }

    public void addUpdate(String update) {
        if (update != null && update.length() > 0){
            updates.add(update);
            data().addPage(this);
        }
    }

    public List<String> getUpdates() {
        return updates;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if(this.Id == ((Page)obj).Id){
            return true;
        }
        return false;
    }
}
