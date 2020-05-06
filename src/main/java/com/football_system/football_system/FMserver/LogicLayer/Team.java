package com.football_system.football_system.FMserver.LogicLayer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.football_system.football_system.FMserver.DataLayer.*;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class Team implements Serializable {

    public enum TeamStatus {
        activityClosed, activityOpened
    }
    private String name;
    private String stadium;
    @JsonIgnore private Page page;
    @JsonIgnore private List<Player> playerList;
    @JsonIgnore private List<Manager> managerList;
    @JsonIgnore private List<Owner> ownerList;
    @JsonIgnore private List<Game> away;
    @JsonIgnore private List<Game> home;
    private League league;
    @JsonIgnore private List<Coach> coachList;
    @JsonIgnore private List<RoleHolder> roleHolders;
    private TeamStatus status;
    @JsonIgnore private boolean finalClose; // true if the admin closed (cant be changed after true)

    private static IDataManager data(){
        return DataComp.getInstance();
    }

    public TeamStatus getStatus() {
        return status;
    }
    public void addHomeGame(Game game){
        home.add(game);
    }
    public void addAwayGame(Game game){
        away.add(game);
    }

    public void setStatus(TeamStatus status) {
        this.status = status;
    }

    public Team(String name, String stadium, Page page) {
        this.name=name;
        this.stadium = stadium;
        this.page = page;
        managerList = new LinkedList<>();
        playerList = new LinkedList<>();
        ownerList = new LinkedList<>();
        away = new LinkedList<>();
        home = new LinkedList<>();
        coachList = new LinkedList<>();
        roleHolders = new LinkedList<>();
        finalClose = false;
    }

    /***
     * id: Team@4
     * get all teams in some league
     * @return league teams
     */
    public static List<Team> getAllTeamsInLeague(League league){
        List<Team> teams = new LinkedList<>();
        for(Team team : data().getTeamList()){
            if(team.league.equals(league)){
                teams.add(team);
            }
        }
        return teams;
    }
    public void addOwner(Owner owner) {
        if (!this.ownerList.contains(owner))
            ownerList.add(owner);
    }

    public void addManager(Manager manager){
        if(!this.managerList.contains(manager)){
            managerList.add(manager);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public Manager getManager(User user) {
        for (Manager manager : managerList) {
            if (manager.getUser().equals(user))
                return manager;
        }
        return null;
    }

    public Player getPlayer(User user) {
        for (Player player : playerList) {
            if (player.getUser().equals(user))
                return player;
        }
        return null;
    }

    public Coach getCoach(User user) {
        for (Coach coach : coachList) {
            if (coach.getUser().equals(user))
                return coach;
        }
        return null;
    }

    public Owner getOwner(User user) {
        for (Owner owner : ownerList) {
            if (owner.getUser().equals(user))
                return owner;
        }
        return null;
    }

    public List<Manager> getManagerList() {
        return managerList;
    }

    public void setManager(Manager manager) {
        if (!managerList.contains(manager))
            managerList.add(manager);
    }

    /**
     * id: Team@1
     * returns a RoleHolder that belongs to the requiered team
     * search is made by user name and email
     * @param userName
     * @param email
     * @return
     */
    public RoleHolder getRoleHolder(String userName,String email) {

        User user = data().getUserByMail(userName,email);
        if (user!=null) {
            for (RoleHolder roleHolder : this.roleHolders) {
                if (roleHolder.getUser().equals(user))
                    return roleHolder;
            }
        }
        return null;
    }
    /**
     * ID: 1
     * returns a RoleHolder that belongs to the requiered team of the given owner
     * search is made by user name and email
     * @param owner
     * @param userName
     * @param email
     * @return
     */
    public RoleHolder getRoleHolder(Owner owner, String userName,String email) {

        User user = data().getUserByMail(userName,email);
        if (this.ownerList.contains(owner)) {
            for (RoleHolder roleHolder : this.roleHolders) {
                if (roleHolder.getUser().equals(user))
                    return roleHolder;
            }
        }
        return null;
    }
    public List<RoleHolder> getRoleHolders() {
        return roleHolders;
    }

    public List<Owner> getOwnerList() {
        return ownerList;
    }

    public void setOwnerList(List<Owner> ownerList) {
        this.ownerList = ownerList;
    }

    public List<Game> getAway() {
        return away;
    }

    public void setAway(List<Game> away) {
        this.away = away;
    }

    public List<Game> getHome() {
        return home;
    }

    public void setHome(List<Game> home) {
        this.home = home;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }


    /**
     * ID: Team@2
     * compare two teams
     * @param team the team we want to compare to
     * @return true if the teams are equal
     */
    public boolean equals(Team team){
        if(this.name.equals(team.getName())&& this.stadium.equals(team.getStadium())){
            return true;
        }else{
            return false;
        }
    }

    public void setPlayer(Player player) {
        if(!playerList.contains(player))
            playerList.add(player);
    }

    public void setCoach(Coach coach) {
        if (!coachList.contains(coach))
            coachList.add(coach);
    }

    public List<Coach> getCoachList() {
        return coachList;
    }

    public void setCoachList(List<Coach> coachList) {
        this.coachList = coachList;
    }
    public boolean isFinalClose() {
        return finalClose;
    }

    public boolean setFinalClose(boolean finalClose) {
        if(this.isFinalClose()){
            System.out.println("this close is permanently close");
            return false;
        }
        else{
            this.finalClose=finalClose;
            return true;

        }
    }

    public void finalCloseTeam(){
        finalClose=true;
    }

    /**
     * id: Team@2
     * changes the status of the team to close if the owner is the real owner of the team
     * @param owner
     */
    public void changeTeamActivity(Owner owner, TeamStatus newStatus) throws IOException {
        if (ownerList.contains(owner)) {
            String date = LocalDate.now().toString();
            Alert alert;
            for(RoleHolder roleHolder: getRoleHolders()){
                if (roleHolder instanceof Manager || roleHolder instanceof Owner) {
                    if (newStatus == TeamStatus.activityClosed)
                        alert = new Alert(roleHolder.getUser(), "The team: " + this.getName() + " is closed temporarily",date);
                    else // Opened
                        alert = new Alert(roleHolder.getUser(), "The team: " + this.getName() + " is open", date);
                    data().addAlert(roleHolder.getUser(),alert);
                }
            }
            setStatus(newStatus);
        }
        else {
            throw new IOException("This team can not be closed without official owner premission");
        }
    }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                ", stadium='" + stadium + '\'' +
                '}';
    }
}
