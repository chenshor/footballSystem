package com.football_system.football_system.FMserver.LogicLayer;

import java.io.Serializable;

public class Representitive extends Role implements Serializable {

    private String name;

    public Representitive(User user, String name) {
        super(user);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
