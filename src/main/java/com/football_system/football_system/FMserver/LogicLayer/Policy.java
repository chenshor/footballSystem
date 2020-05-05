package com.football_system.football_system.FMserver.LogicLayer;

import java.io.Serializable;

public class Policy implements Serializable {

    private League league;
    private Season season;

    public Policy(League league, Season season) {
        this.league = league;
        this.season = season;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }
}
