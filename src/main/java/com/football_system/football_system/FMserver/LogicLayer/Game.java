package com.football_system.football_system.FMserver.LogicLayer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.football_system.football_system.FMserver.DataLayer.IDataManager;

import javax.annotation.Generated;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

public class Game extends Observable implements Serializable {


    private Integer id;
    private Season season;

    private Team home;

    private Team away;
    @JsonIgnore
    private Referee line;
    @JsonIgnore
    private Referee main;
    @JsonIgnore
    private List<GameEventCalender> gameEventCalenderHome;
    @JsonIgnore
    private List<GameEventCalender> gameEventCalenderAway;
    private String date; // format "2019-04-09"
    private String startTime; // format "13:50"
    private String endTime;
    @JsonIgnore
    private Result result;
    @JsonIgnore
    private GameReport gameReport;

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
        this.id = data().getGameList().size()+1; // remove it later
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
        this.id = data().getGameList().size()+1; // remove it later
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
        setChanged();
        notifyObservers(event);
    }

    public void addEventGame(boolean home , GameEventCalender event){
        if(home) {
            gameEventCalenderHome.add(event);
        }else{
            gameEventCalenderAway.add(event);
        }

        setChanged();
        notifyObservers(event);

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
}
