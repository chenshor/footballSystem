package com.football_system.football_system.FMserver.LogicLayer;

import com.football_system.football_system.FMserver.DataLayer.IDataManager;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
@Entity
@EnableAutoConfiguration
@Table(name = "Representative")
public class Representative extends Role implements Serializable {

    private String name;
    @Transient
    private IDataManager data = DataComp.getInstance();

    public Representative(User user, String name) {
        super(user);
        this.name = name;
    }

    public  Representative(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        data.addRepresentetive(this);
    }

    private void addTeam(Team team){
        data.addTeam(team);
    }

    public void createTeam(String name, String stadium, Page page){
        Team newTeam = new Team(name, stadium, page);
        addTeam(newTeam);
    }
}
