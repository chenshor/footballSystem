package com.football_system.football_system.FMserver.LogicLayer;

import java.io.Serializable;

public class JudgmentApproval implements Serializable {
    private League league;
    private Season season;

    public JudgmentApproval(League league, Season season) {
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

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof JudgmentApproval){
            JudgmentApproval other = (JudgmentApproval) obj ;
            return other.league.equals(this.league) && other.season.equals(this.season);
        }
        return false;
    }
}
