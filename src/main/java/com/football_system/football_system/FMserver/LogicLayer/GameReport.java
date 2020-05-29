package com.football_system.football_system.FMserver.LogicLayer;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
@Entity
@EnableAutoConfiguration
@Table(name = "game_reports")
public class GameReport implements Serializable {
    @Id
    private int gamerReportId;
    @OneToOne
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

    public GameReport(){}

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
