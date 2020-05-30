package com.football_system.football_system.FMserver.LogicLayer;

import com.football_system.football_system.FMserver.DataLayer.IDataManager;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
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
    @Transient
    private static int Id_Generator = 5;

    public GameReport(Game game,String description) {
        this.game = game;
        this.description=description;
        gamerReportId = Id_Generator;
        Id_Generator = Id_Generator + 10;
    }

    public GameReport(Game game) {
        this.game = game;
        description="no Game report";
        gamerReportId = Id_Generator;
        Id_Generator = Id_Generator + 10;
    }

    public GameReport(){}

    private static IDataManager data(){
        return DataComp.getInstance();
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
        data().addGameReport(this);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        data().addGameReport(this);
    }


    /**
     * function number: 1
     */
    public void displayReport(){
        game.displayDetails();
        System.out.println("description: " +getDescription() );
    }

    @Override
    public boolean equals(Object obj) {
        if(this.gamerReportId == ((GameReport)obj).gamerReportId){
            return true;
        }
        return false;
    }
}
