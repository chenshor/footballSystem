package com.football_system.football_system.FMserver.LogicLayer;

public class Result {
    private Integer home;
    private Integer away;

    public Result(Integer home, Integer away) {
        this.home = home;
        this.away = away;
    }

    public Integer getHome() {
        return home;
    }

    public Integer getAway() {
        return away;
    }

    public void homeScores(){
        home++;
    }

    public void awayScores(){
        away++;
    }
}
