package com.football_system.football_system.FMserver.LogicLayer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.football_system.football_system.FMserver.DataLayer.IDataManager;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.annotation.Generated;
import javax.persistence.*;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
@Entity
@EnableAutoConfiguration
@Table(name = "Games")
public class Game  implements Serializable{

    @Id
    private int id;
    @OneToOne
    @JoinColumn(name = "Season_id")
    private Season season;
    @OneToOne
    @JoinColumn(name = "home_team_id")
    private Team home;
    @OneToOne
    @JoinColumn(name = "away_team_id")
    private Team away;
    @OneToOne
    @JsonIgnore
    private Referee line;
    @JsonIgnore
    @OneToOne
    private Referee main;
    @OneToMany(cascade = CascadeType.REMOVE)
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<GameEventCalender> gameEventCalenderHome;
    @OneToMany(cascade = CascadeType.REMOVE)
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<GameEventCalender> gameEventCalenderAway;
    private String date; // format "2019-04-09"
    private String startTime; // format "13:50"
    private String endTime;
    @OneToOne(cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Result result;
    @OneToOne(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnore
    private GameReport gameReport;
    @OneToMany(cascade = CascadeType.REMOVE)
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Fan> subscribers;

    public static boolean checkIfHomeTeam(String teamName , Integer game_id){
        return Game.getGameById(game_id).getHome().getName().equals(teamName) ;
    }
    public static IDataManager data(){
        return DataComp.getInstance();
    }
    public Game() {

    }

    public Game(Season season, Team home, Team away, Referee line, Referee main, List<GameEventCalender> gameEventCalender, String date, String start, String end) {
        this.season = season;
        this.home = home;
        this.away = away;
        this.line = line;
        this.main = main;
        this.gameEventCalenderHome = gameEventCalender;
        this.gameEventCalenderAway = gameEventCalender;
        if(this.gameEventCalenderHome == null){
            this.gameEventCalenderHome = new LinkedList<>();
        }
        if(this.gameEventCalenderAway == null){
            this.gameEventCalenderAway = new LinkedList<>();
        }
        this.date=date;
        this.startTime=start;
        this.endTime=end;
        this.gameReport=new GameReport(this);
        this.subscribers = new LinkedList<>();
        data().addGame(this);
    }

    public static List<Game> getGamesByReferee(String refereeID){
        List<Game> games = data().getGameList();
        List<Game> refereeGames = new LinkedList<>();
        for( Game game : games){
            if(game.getMain().getUser().getEmail().equals(refereeID)){
                refereeGames.add(game);
            }
        }
        return refereeGames ;
    }

    public static Game getGameById(Integer id){
        return data().getGameList().stream().filter(game -> game.getId().equals(id)).findFirst().get() ;
    }
    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Game(Season season, Team home, Team away, Referee line, Referee main, String date, String start, String end) {
        this.season = season;
        this.home = home;
        this.away = away;
        this.line = line;
        this.main = main;
        if(this.gameEventCalenderHome == null){
            this.gameEventCalenderHome = new LinkedList<>();
        }
        if(this.gameEventCalenderAway == null){
            this.gameEventCalenderAway = new LinkedList<>();
        }
        this.date=date;
        this.startTime=start;
        this.endTime=end;
        this.gameReport=new GameReport(this);
        this.subscribers = new LinkedList<>();
        data().addGame(this);
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public Team getHome() {
        return home;
    }

    public void setHome(Team home) {
        this.home = home;
    }

    public Team getAway() {
        return away;
    }

    public void setAway(Team away) {
        this.away = away;
    }

    public Referee getLine() {
        return line;
    }

    public void setLine(Referee line) {
        this.line = line;
    }

    public Referee getMain() {
        return main;
    }

    public void setMain(Referee main) {
        this.main = main;
    }

    public List<GameEventCalender> getGameEventCalender() {
        return gameEventCalenderHome;
    }

    public void setGameEventCalender(List<GameEventCalender> gameEventCalender) {
        this.gameEventCalenderHome = gameEventCalender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * id: Game@1
     * prints the game's details
     */
    public void displayDetails(){
        System.out.println("Date: " + getDate());
        System.out.println("Start Time: " + getStartTime());
        System.out.println("End Time: " + getEndTime());
    }

    public GameReport getGameReport() {
        return gameReport;
    }

    public void setGameReport(GameReport gameReport) {
        this.gameReport = gameReport;
    }


    /**
     * id: Game@2
     * compare two games
     * @param game2 game we wanyt to compare
     * @return return true if the two games are equal
     */
    public boolean equals(Game game2){
        if(this.getDate().equals(game2.getDate())&& this.getHome().equals(game2.getHome())&&
        this.getAway().equals(game2.getAway())){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * id: Game@3
     * @param event - event to add
     */
    public void addEventGame(GameEventCalender event){
        gameEventCalenderHome.add(event);
    }

    public HashMap<User , Alert> addEventGame(boolean home , GameEventCalender event){
        HashMap<User , Alert> userListHashMap = new HashMap<>();
        if(home) {
            gameEventCalenderHome.add(event);
        }else{
            gameEventCalenderAway.add(event);
        }

        String description = null ;
        if(home) {
            description = this.home.getName() + " against "+ this.away.getName() +" : "+ this.home.getName() + " makes "+event.getType() +", "+event.getDescription();
        }else{
            description = this.home.getName() + " against "+ this.away.getName() +" : "+ this.away.getName() + " makes "+event.getType() +", "+event.getDescription();

        }
        for (Fan fan: subscribers) {
            User user  = fan.getUser();
            Alert alert = new Alert(user , description , this.getDate() , false) ;
            user.addAlerts(alert);
            userListHashMap.put(user , alert) ;
        }

        return userListHashMap;
    }

    public Integer getId() {
        return id;
    }

    public List<GameEventCalender> getGameEventCalenderHome() {
        return gameEventCalenderHome;
    }

    public void getGameEventCalenderHome(List<GameEventCalender> gameEventCalenderHome) {
        this.gameEventCalenderHome = gameEventCalenderHome;
    }
    public List<GameEventCalender> getGameEventCalenderAway() {
        return gameEventCalenderAway;
    }

    public void setGameEventCalenderAway(List<GameEventCalender> gameEventCalenderAway) {
        this.gameEventCalenderAway = gameEventCalenderAway;
    }

    public List<Fan> getSubscribers() {
        return subscribers;
    }

    public void addSubscriber(Fan fan){

        if( ! subscribers.contains(fan)) {
            subscribers.add(fan);
        }
        if(! fan.getGames().contains(this)){
            fan.addGameToSubscribe(this);
        }
    }

    public void removeSubscriber(Fan fan){
        if(subscribers.contains(fan)) {
            subscribers.remove(fan);
        }
        if(fan.getGames().contains(this)){
            fan.removeGameFromSubscribe(this);
        }
    }
}
