package com.football_system.football_system.FMserver.LogicLayer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
@Entity
@EnableAutoConfiguration
@Table(name = "game_events")
public class GameEventCalender implements Serializable {
    @Id
    private int GameEvent_Id;
    @JsonIgnore
    @ManyToOne
    private Game game;
    private String hour; // format (13:50)
    private String date; // format "2019-04-09"
    private eventType type;
    private String description;
    private int minute;

    public enum eventType{
        goal, offside, redCard, yellowCard, injury, playerReplacement
    }

    public GameEventCalender(Game game, String hour, String date, String type, String description, int minute) {
        if(isAType(type)) {
            this.game = game;
            this.hour = hour;
            this.date = date;
            this.description = description;
            this.minute = minute;
            this.type=eventType.valueOf(type);
        }
    }

    public GameEventCalender(Game game) {
        this.game = game;
    }

    public GameEventCalender(){}
    /**
     * ID: GameEventCalender@1
     * displays the event's details
     */
    public void displayEvents(){
        System.out.print("Game: " );
        getGame().displayDetails();
        System.out.println("Minute: " + getMinute());
        System.out.println("Type: " + getType().name());
        System.out.println("Description: " + getDescription());
    }


    /**
     * ID: GameEventCalender@2
     * checks if the String is a kind of a event type
     * @param type String type
     * @return true if the input is a kind of event
     */
    public boolean isAType(String type){
        try{
            eventType.valueOf(type);
            return true;
        }catch (Exception e){
            return false;
        }

    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public eventType getType() {
        return type;
    }

    public void setType(eventType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
