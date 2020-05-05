package com.football_system.football_system.FMserver.LogicLayer;

import com.football_system.football_system.FMserver.DataLayer.*;
import com.football_system.football_system.FMserver.ServiceLayer.*;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;


public class Referee extends Role implements Serializable {

    private String qualification;
    private String name;
    private League league;
    private List<Game> main;
    private  List<Game> line;
    private List<JudgmentApproval> judgmentApproval ;
    private static final Logger testLogger = Logger.getLogger(RefereeService.class);

    public Referee(User user, String qualification, String name, League league) {
        super(user);
        this.qualification = qualification;
        this.name = name;
        this.league = league;
        this.main = new LinkedList<>();
        this.line = new LinkedList<>();
        judgmentApproval = new LinkedList<>();
    }

    private static IDataManager data(){
        return DataComp.getInstance();
    }

    public Referee(User user, String qualification, String name) {
        super(user);
        this.qualification = qualification;
        this.name = name;
        this.main = new LinkedList<>();
        this.line = new LinkedList<>();
        judgmentApproval = new LinkedList<>();
    }



    /**
     * id: Referee@1
     * make a user a referee
     * @param user of referee
     * @param qualification of referee
     * @param name of referee
     * @return true if added successfully, else if already exists
     */
    public static boolean MakeUserReferee(User user, String qualification, String name){
           Referee referee = new Referee( user,  qualification,  name);
           boolean res =  user.addRole(referee);
           if(res) res = data().addReferee(referee) ;
           return res;
    }

    /**
     * id: Referee@2
     * Remove a user a referee
     * @param
     * @return true if removed successfully, else if already removed
     */
    public static boolean RemoveUserReferee(Referee referee){
        if(referee.getUser().removeRole(referee) == true) {
            return data().removeReferee(referee);
        }
        return false;
    }

    /**
     * id: Referee@9
     * get all referees
     * @param
     * @return all Referees in system
     */
    public static List<Referee> getReferees(){
        return data().getRefereeList() ;
    }

    /**
     * id: Referee@13
     * get all legal referees with needed approval for judgment
     * @param league of approval
     * @param season of approval
     * @return Referee list
     */
    public static List<Referee> legalRefereesForLeague(League league , Season season){
        JudgmentApproval neededApproval  = new JudgmentApproval(league , season);
        List<Referee> referees = new LinkedList<>();
        for(Referee referee : data().getRefereeList()){
            if(referee.judgmentApproval.contains(neededApproval)){
                referees.add(referee);
            }
        }
        return referees;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getName() {
        return name;
    }

    public boolean setName(String name) {
        if(name.matches("[a-zA-Z]+")){
            this.name=name;
            return true;
        }
        else{
            System.out.println("the name must contain only letters");
            return false;
        }

    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public List<Game> getMain() {
        return main;
    }

    public void setMain(List<Game> main) {
        this.main = main;
    }

    public List<Game> getLine() {
        return line;
    }

    public void setLine(List<Game> line) {
        this.line = line;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Referee){
            Referee other = (Referee)obj;
            return (((User)(other.getUser())).equals(this.getUser()));
        }
        return false;
    }

    /**
     * id: Referee@10
     * add Approval to judge in specific Season of League
     * @param approval
     * @return false if already contains this approval , true if added.
     */
    public boolean addJudgmentApproval(JudgmentApproval approval){
        if(judgmentApproval.contains(approval)) return false ;
        judgmentApproval.add(approval);
        return true ;
    }
    /**
     * id: Referee@11
     * remove Approval to judge in specific Season of League
     * @param approval
     * @return if been removed successfully.
     */
    public boolean removeJudgmentApproval(JudgmentApproval approval){
        if( ! judgmentApproval.contains(approval)) return false;
        judgmentApproval.remove(approval);
        return true ;
    }
    /**
     * id: Referee@12
     * return all Judgment Approvals to judge in specific Season of League
     * @return all approvals for league-season judgment
     */
    public List<JudgmentApproval> getJudgmentApproval(){
        return judgmentApproval ;
    }

    public boolean equals(Referee ref){
        if(this.getName().equals(ref.getName())&& this.getQualification().equals(ref.getQualification())){
            return true;
        }
        else{
            return false;
        }
    }


    /**
     * ID: Referee@3
     * adds a game to the main games list
     * @param game
     */
    public void addAGameMain(Game game){
        getMain().add(game);
    }

    /**
     * ID: Referee@4
     * adds a game to the lain games list
     * @param game
     */
    public void addAGameLine(Game game){
        getLine().add(game);
    }


    /**
     *ID: Referee@5
     * UC 10.2
     * displays the referee's games
     * @return an array of strings that contains all the games of the referee
     */
    public String[] displayGames(){
        if(main.size()==0 && line.size()==0){
            System.out.println("No games");
            String [] display = new String[1];
            display[0] = "no games";
            return display;
        }
        else {
            String[] display = new String[2 + getMain().size() + getLine().size()];
            System.out.println("main games:");
            display[0] = "main Games";
            int i = 1;
            for (Game game : getMain()) {
                System.out.println("Game number " + i);
                game.displayDetails();
                display[i] = "Game number: " + i;
                i++;
            }
            display[i] = "side Games:";
            int index = i + 1;
            i = 1;
            System.out.println("line games: ");
            for (Game game : getLine()) {
                System.out.println("Game number " + i);
                game.displayDetails();
                display[index] = "Game number: " + i;
                i++;
            }
            return display;
        }
    }


    /**
     * ID: Referee@6
     * UC 10.3
     * @param game the game we want to add an event to
     * @param description the description of the event
     * @param eventType the event type
     */
    public void addGameEvent(Game game,String description, String eventType){
        LocalDate date=LocalDate.now();
        LocalTime now=LocalTime.now();
        if(game.getLine().equals(this) || game.getMain().equals(this)){
            if(date.compareTo(LocalDate.parse(game.getDate()))==0){
                if(now.isBefore(LocalTime.parse(game.getEndTime()))&& now.isAfter(LocalTime.parse(game.getStartTime()))){
                    GameEventCalender event = new GameEventCalender(game,now.toString(),date.toString(),eventType,description,now.getMinute());
                    if(event.getDescription()==null){
                        System.out.println("no such type of events");
                    }else {
                        game.addEventGame(event);
                        String propertiesPath = "log4j.properties";
                        PropertyConfigurator.configure(propertiesPath);
                        testLogger.info("Added new game event");
                    }
                }
                else{
                    System.out.println("The game is not taking place right now");
                }
            }
        }
        else{
            System.out.println("You are not the referee in this game");
        }
    }


    /**
     * ID: Referee@7
     * UC: 10.4
     * adds an event to the game after he ended (only by a main referee)
     * @param game the game we want to add an event to
     * @param description the description of the event
     * @param eventType the event type
     */
    public void addEventAfterGame(Game game, String description, String eventType){
        LocalDate date=LocalDate.now();
        LocalTime now=LocalTime.now();
        if(game.getMain().equals(this)){
            if(date.compareTo(LocalDate.parse(game.getDate()))==0) {
                if (now.minusHours(5).isBefore(LocalTime.parse(game.getEndTime())) && now.isAfter(LocalTime.parse(game.getStartTime()))) {
                    GameEventCalender event = new GameEventCalender(game, now.toString(), date.toString(), eventType, description, now.getMinute());
                    if (event.getDescription() == null) {
                        System.out.println("no such type of events");
                    } else {
                        game.addEventGame(event);
                        String propertiesPath = "log4j.properties";
                        PropertyConfigurator.configure(propertiesPath);
                        testLogger.info("Added new game event");
                    }
                } else {
                    System.out.println(" The game havent start yet or Its been more then 5 hours, you can't edit the gameEvent");
                }
            }
            }else{
                System.out.println("you are not the main referee in this game");
            }
        }


    /**
     * ID: Referee@8
     * UC: 10.4.2
     * creates a game report by a main referee
     * @param game the game we want to create a report for
     * @param description the description of the game
     */
    public void createGameReport(Game game, String description){
        if(getMain().contains(game)){
            LocalDate date=LocalDate.now();
            LocalTime now=LocalTime.now();
            if((date.isAfter(LocalDate.parse(game.getDate()))|| date.isEqual(LocalDate.parse(game.getDate())))&& now.isAfter(LocalTime.parse(game.getEndTime()))) {
                GameReport gameReport = new GameReport(game, description);
                game.setGameReport(gameReport);
                String propertiesPath = "log4j.properties";
                PropertyConfigurator.configure(propertiesPath);
                testLogger.info("Added new game report");
            }else{
                System.out.println("The game hasn't finished yet");
            }
        }
        else{
            System.out.println("you are not the main referee in this game");
        }
    }




    public String getLeagueString(){
        return league.getTypeString();
    }
}
