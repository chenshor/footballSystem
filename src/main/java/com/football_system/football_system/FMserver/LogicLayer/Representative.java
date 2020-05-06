package com.football_system.football_system.FMserver.LogicLayer;

import com.football_system.football_system.FMserver.DataLayer.IDataManager;

import java.io.Serializable;

public class Representative extends Role implements Serializable {

    private String name;
    private IDataManager data = DataComp.getInstance();

    public Representative(User user, String name) {
        super(user);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void addTeam(Team team){
        data.addTeam(team);
    }

    public void createTeam(String name, String stadium, Page page){
        Team newTeam = new Team(name, stadium, page);
        addTeam(newTeam);
    }
}
