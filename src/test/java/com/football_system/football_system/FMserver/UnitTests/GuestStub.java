package com.football_system.football_system.FMserver.UnitTests;

import com.football_system.football_system.FMserver.LogicLayer.*;
import com.football_system.football_system.FMserver.ServiceLayer.*;
import com.football_system.football_system.FMserver.DataLayer.*;

import java.util.ArrayList;
import java.util.List;

public class GuestStub extends Guest {
    List<Game> listG;
    List<Player> listP;

    public GuestStub() {
        super();
        init();
    }

    private void init() {
        listG = new ArrayList<>();
        listG.add(new Game());
        listG.add(new Game());
        listP = new ArrayList<>();
        Team team = new Team("FCB",null,null);
        Team otherTeam = new Team("RMCF",null,null);
        listP.add(new Player(null, null, team, "David", null));
        listP.add(new Player(null, null, team, "Or", null));
        listP.add(new Player(null, null, otherTeam, "Raul", null));
    }

    @Override
    public List<Game> retrieveGames() {
        return listG;
    }

    @Override
    public List<Player> retrievePlayers() {
        return listP;
    }

    @Override
    public List<User> SearchUserByName(String name) {
        List<User> list = new ArrayList<>();
        list.add(new User(null, null, name, name));
        return list;
    }
}
