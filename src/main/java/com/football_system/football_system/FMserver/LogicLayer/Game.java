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
    @ManyToMany(cascade = CascadeType.REMOVE)
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Fan> subscribers;
    @Transient
    private static int Id_Generator = 4;

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
        id = Id_Generator;
        Id_Generator = Id_Generator + 10;
        data().addGame(this);
    }

    public static List<Game> getGamesByReferee(String refereeID){
        List<Game> games = data().getGameList();
        List<Game> refereeGames = new LinkedList<>();
        for( Game game : games){
            try{
                if(game.getMain().getUser().getEmail().equals(refereeID)){
                    refereeGames.add(game);
                }
            }catch (Exception e){ }
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
        data().addGame(this);
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
        id = Id_Generator;
        Id_Generator = Id_Generator + 10;
        data().addGame(this);
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
        data().addGame(this);
    }

    public Team getHome() {
        return home;
    }

    public void setHome(Team home) {
        this.home = home;
        data().addGame(this);
    }

    public Team getAway() {
        return away;
    }

    public void setAway(Team away) {
        this.away = away;
        data().addGame(this);
    }

    public Referee getLine() {
        return line;
    }

    public void setLine(Referee line) {
        this.line = line;
        data().addGame(this);
    }

    public Referee getMain() {
        return main;
    }

    public void setMain(Referee main) {
        this.main = main;
        data().addGame(this);
    }

    public List<GameEventCalender> getGameEventCalender() {
        return gameEventCalenderHome;
    }

    public void setGameEventCalender(List<GameEventCalender> gameEventCalender) {
        this.gameEventCalenderHome = gameEventCalender;
        data().addGame(this);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        data().addGame(this);
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
        data().addGame(this);
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
        data().addGame(this);
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
        data().addGame(this);
    }


    /**
     * id: Game@2
     * compare two games
     * @param obj game we wanyt to compare
     * @return return true if the two games are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this.id == ((Game)obj).id)
            return true;
        return false;
    }

    /**
     * id: Game@3
     * @param event - event to add
     */
    public void addEventGame(GameEventCalender event){
        gameEventCalenderHome.add(event);
        data().addGame(this);
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
        data().addGame(this);
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
        data().addGame(this);
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
            data().addFan(fan);
        }
        data().addGame(this);
    }

    public void removeSubscriber(Fan fan){
        if(subscribers.contains(fan)) {
            subscribers.remove(fan);
        }
        if(fan.getGames().contains(this)){
            fan.removeGameFromSubscribe(this);
            data().addFan(fan);
        }
        data().addGame(this);
    }

    public void setId(int id) {
        this.id = id;
    }
}
