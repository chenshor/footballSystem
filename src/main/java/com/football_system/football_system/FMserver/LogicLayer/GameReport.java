package com.football_system.football_system.FMserver.LogicLayer;

import java.io.Serializable;

public class GameReport implements Serializable {
    private Game game;
    private String description;
    private int homeScore;
    private int awayScore;

    public GameReport(Game game,String description) {
        this.game = game;
        this.description=description;
    }

    public GameReport(Game game) {
        this.game = game;
        description="no Game report";
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * function number: 1
     */
    public void displayReport(){
        game.displayDetails();
        System.out.println("description: " +getDescription() );
    }
}
