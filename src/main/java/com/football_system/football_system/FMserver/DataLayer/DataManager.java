package com.football_system.football_system.FMserver.DataLayer;


import java.util.ArrayList;
import java.util.HashMap;

import com.football_system.football_system.FMserver.LogicLayer.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.Map;

public class DataManager implements IDataManager, Serializable {

    private HashMap<Fan,List<String>>fanSearchNameHistory;
    private HashMap<Fan,List<String>>fanSearchCategoryHistory;
    private HashMap<Fan,List<String>>fanSearchKeyWordHistory;
    private List<Guest> guestsList;
    private List<User> userList;
    private Map<User, List<Alert>> alerts;
    private Map<User, List<Complaint>> complaints;
    private List<League> leagueList;
    private List<Season> seasonList;
    private List<Team> teamList;
    private List<Page> pageList;
    private List<Game> gameList;
    private LinkedList<Referee> RefereeList;
    private List<Administrator> administrators;
    private static final Logger systemLogger = Logger.getLogger(DataManager.class);

    public DataManager() {
        guestsList = new ArrayList<>();
        userList = new ArrayList<>();
        alerts = new HashMap<>();
        complaints = new HashMap<>();
        leagueList = new ArrayList<>();
        seasonList = new ArrayList<>();
        teamList = new ArrayList<>();
        pageList = new ArrayList<>();
        gameList = new ArrayList<>();
        RefereeList = new LinkedList<>();
        String propertiesPath = "log4j.properties";
        fanSearchNameHistory = new HashMap<>();
        fanSearchKeyWordHistory = new HashMap<>();
        fanSearchCategoryHistory = new HashMap<>();
        PropertyConfigurator.configure(propertiesPath);
    }

    public boolean checkIfEmailExists(String email) {
        for (User user : userList) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    public void addNewUser(User newUser) {
        userList.add(newUser);
    }

    public User getUser(String email, String password) {
        for (User user : userList) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public User getUserByPassword(String userName, String password) {
        for (User user : userList) {
            if (user.getUserName().equals(userName) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public User getUserByMail(String userName, String email) {
        for (User user : userList) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }


    public void addUser(User user) {
        if (!userList.contains(user))
            userList.add(user);
    }

    public List<Guest> getGuestsList() {
        return guestsList;
    }

    public void setGuestsList(List<Guest> guestsList) {
        this.guestsList = guestsList;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public Map<User, List<Alert>> getAlerts() {
        return alerts;
    }

    public void setAlerts(Map<User, List<Alert>> alerts) {
        this.alerts = alerts;
    }

    public Map<User, List<Complaint>> getComplaint() {
        return complaints;
    }

    public void setComplaint(Map<User, List<Complaint>> complaints) {
        this.complaints = complaints;
    }

    public List<League> getLeagueList() {
        return leagueList;
    }

    public HashMap<Fan, List<String>> getFanSearchNameHistory() {
        return fanSearchNameHistory;
    }

    public HashMap<Fan, List<String>> getFanSearchCategoryHistory() {
        return fanSearchCategoryHistory;
    }

    public HashMap<Fan, List<String>> getFanSearchKeyWordHistory() {
        return fanSearchKeyWordHistory;
    }

    /**
     * id: dataManager@1
     * Search League by league type
     *
     * @param leagueType
     * @return League if existing
     */
    public League SearchLeagueByType(League.LeagueType leagueType) {
        for (League league :
                leagueList) {
            if (league.getType() == leagueType) {
                return league;
            }
        }
        return null;
    }

    /**
     * id: dataManager@2
     * add New League To Data
     *
     * @param league to add
     */
    public void addLeague(League league) {
        if (SearchLeagueByType(league.getType()) == null) {
            leagueList.add(league);
            systemLogger.info("league been added , type:" + league.getType());
        }
    }

    /**
     * id: dataManager@3
     * Search Season by start and end dates
     *
     * @param start date of season
     * @param End   date of season
     * @return Season if found, else null
     */
    public Season SearchSeason(String start, String End) {
        for (Season season : seasonList) {
            if (season.getEnd().equals(End) && season.getStart().equals(start)) {
                return season;
            }
        }
        return null;
    }

    /**
     * id: dataManager@4
     * add new season to data
     *
     * @param season season to add
     */
    public void addSeason(Season season) {
        if (SearchSeason(season.getStart(), season.getEnd()) == null) {
            seasonList.add(season);
            systemLogger.info("Season been added , linked to League:" + " , Start date:" + season.getStart() +
                    " , End date:" + season.getEnd());
        } else if (SearchSeason(season.getStart(), season.getEnd()).getLeagueList().contains(season.getLeagueList())) {
            systemLogger.info("Season Linked to existing League:" + " , Start date:" + season.getStart() +
                    " , End date:" + season.getEnd());
        }
    }

    /**
     * id: dataManager@6
     * add New Referee To Data
     *
     * @param referee
     * @return if added successfully, if not -> already contains the element
     */
    public boolean addReferee(Referee referee) {
        if (!RefereeList.contains(referee)) {
            RefereeList.add(referee);
            systemLogger.info("new Referee been added , belong to user : " + referee.getUser().getUserName());
            return true;
        }
        return false;
    }

    /**
     * id: dataManager@7
     * remove referee from data
     *
     * @param referee
     * @return
     */
    public boolean removeReferee(Referee referee) {
        if (RefereeList.contains(referee)) {
            RefereeList.remove(referee);
            systemLogger.info("Referee been removed , belong to user : " + referee.getUser().getUserName());
            return true;
        }
        return false;
    }


    public void setLeagueList(List<League> leagueList) {
        this.leagueList = leagueList;
    }

    public List<Season> getSeasonList() {
        return seasonList;
    }

    public void setSeasonList(List<Season> seasonList) {
        this.seasonList = seasonList;
    }

    public List<Team> getTeamList() {
        return teamList;
    }

    public void setTeamList(List<Team> teamList) {
        this.teamList = teamList;
    }

    public List<Referee> getRefereeList() {
        return RefereeList;
    }

//    /**
//     * id: dataManager@10
//     *
//     * @param user
//     * @param alert
//     */
//    public void addAlert(User user, Alert alert) {
//        if (!lerts.containsKey(user)) {
//            List<Alert> alerts = new LinkedList<>();
//            alerts.add(alert);
//            Alerts.put(user, alerts);
//        } else
//            Alerts.get(user).add(alert);
//    }


    public static void writeData(IDataManager data, File file) {
        try {
            FileOutputStream f = new FileOutputStream(file);
            ObjectOutputStream o = new ObjectOutputStream(f);

            // Write objects to file
            o.writeObject(data);

            o.close();
            f.close();
        } catch (Exception e) {
        }

    }

    public static IDataManager readData(File file) {
        IDataManager Mdata = null;
        try {
            FileInputStream fi = new FileInputStream(file);
            ObjectInputStream oi = new ObjectInputStream(fi);

            // Read objects
            Mdata = (IDataManager) oi.readObject();

            oi.close();
            fi.close();
        } catch (Exception e) {
        }
        return Mdata;
    }

    public List<Page> getPageList() {
        return pageList;
    }

    /**
     * ID: dataManager@8
     * adds a new Team to the teams list
     * @param team the new team we want to add
     */
    public void addTeam(Team team){
        if(!getTeamList().contains(team)){
            teamList.add(team);
        }
    }

    /**
     * ID: dataManager@9
     * adds a new complaint to the complaints map
     * @param complaint the new complaint we want to add
     * @param user the user we add the complaint to
     */
    public void addComplaint(Complaint complaint,User user){
        if(!complaints.containsValue(complaint)){
            List<Complaint> list = complaints.get(user);
            if(list==null){
                list = new LinkedList<>();
            }
            list.add(complaint);
            complaints.put(user,list);
        }
    }

    public List<Owner> getOwners() {
        List<Owner> owners = new ArrayList<Owner>();
        for (User user : userList) {
            List<Role> userRoles = user.getRoles();
            for (Role role : userRoles) {
                if (role instanceof Coach) {
                    owners.add((Owner) role);
                }
            }
        }
        return owners;
    }

    public List<Manager> getManagers() {
        List<Manager> managers = new ArrayList<Manager>();
        for (User user : userList) {
            List<Role> userRoles = user.getRoles();
            for (Role role : userRoles) {
                if (role instanceof Coach) {
                    managers.add((Manager) role);
                }
            }
        }
        return managers;
    }

    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<Player>();
        for (User user : userList) {
            List<Role> userRoles = user.getRoles();
            for (Role role : userRoles) {
                if (role instanceof Coach) {
                    players.add((Player) role);
                }
            }
        }
        return players;
    }

    public List<User> searchUserByName(String name) {
        List<User> retrievedUsers = new ArrayList<>();
        String[] splitted = name.split(" ");
        if (splitted.length == 1){
            for (User user : userList) {
                if (user.getFirstName().equals(splitted[0])) {
                    retrievedUsers.add(user);
                }
            }
        }else{
            for (User user : userList) {
                if (user.getFirstName().equals(splitted[0]) && user.getLastName().equals(splitted[1])) {
                    retrievedUsers.add(user);
                }
            }
        }
        return retrievedUsers;
    }

    @Override
    public void addComplaint(User user, Complaint newComplaint) {
        if (complaints.containsKey(user)){
            complaints.get(user).add(newComplaint);
        }else{
            List<Complaint>userComplaints = new ArrayList<>();
            userComplaints.add(newComplaint);
            complaints.put(user,userComplaints);
        }
    }

    @Override
    public List<League> searchLeagueByName(String leagueName) {
        List<League> retrievedLeagues = new ArrayList<>();
        for (League league : leagueList) {
            if (league.getName().toLowerCase().equals(leagueName.toLowerCase())) {
                retrievedLeagues.add(league);
            }
        }
        return retrievedLeagues;
    }

    @Override
    public List<Team> searchTeamByName(String teamName) {
        List<Team> retrievedTeams = new ArrayList<>();
        for (Team team : teamList) {
            if (team.getName().toLowerCase().equals(teamName.toLowerCase())) {
                retrievedTeams.add(team);
            }
        }
        return retrievedTeams;
    }

    @Override
    public void addNameHistory(Fan fan, String query) {
        if (fanSearchNameHistory.containsKey(fan)){
            fanSearchNameHistory.get(fan).add(query);
        }else{
            List<String>SearchHistory = new ArrayList<>();
            SearchHistory.add(query);
            fanSearchNameHistory.put(fan,SearchHistory);
        }

    }

    @Override
    public void addKeyWordHistory(Fan fan, String query) {
        if (fanSearchKeyWordHistory.containsKey(fan)){
            fanSearchKeyWordHistory.get(fan).add(query);
        }else{
            List<String>SearchHistory = new ArrayList<>();
            SearchHistory.add(query);
            fanSearchKeyWordHistory.put(fan,SearchHistory);
        }
    }

    @Override
    public void addCategoryHistory(Fan fan, String query) {
        if (fanSearchCategoryHistory.containsKey(fan)){
            fanSearchCategoryHistory.get(fan).add(query);
        }else{
            List<String>SearchHistory = new ArrayList<>();
            SearchHistory.add(query);
            fanSearchCategoryHistory.put(fan,SearchHistory);
        }
    }

    @Override
    public List<String> getCategorySearchHistory(Fan fan) {
        return this.fanSearchCategoryHistory.get(fan);
    }

    @Override
    public List<String> getKeyWordSearchHistory(Fan fan) {
        return this.fanSearchKeyWordHistory.get(fan);
    }

    @Override
    public List<String> getNameSearchHistory(Fan fan) {
        return this.fanSearchNameHistory.get(fan);
    }

    public List<Game> getGameList() {
        return gameList;
    }

    public List<Coach> getCoaches() {
        List<Coach> coaches = new ArrayList<Coach>();
        for (User user : userList) {
            List<Role> userRoles = user.getRoles();
            for (Role role : userRoles) {
                if (role instanceof Coach) {
                    coaches.add((Coach) role);
                }
            }
        }
        return coaches;
    }


    /**
     * ID: dataManager@10
     * adds new alert to the alerts map
     * @param alert the new alert we want to add
     * @param user the user we add the alert to
     */
    public void addAlert(Alert alert,User user){
        if(!complaints.containsValue(alert)){
            List<Alert> list = getAlerts().get(user);
            if(list==null){
                list = new LinkedList<>();
            }
            list.add(alert);
            alerts.put(user,list);
        }
    }
    public void addAlert(User user,Alert alert){
        addAlert(alert,user);
    }

    /**
     * dataManager@11
     * delets a user from the users list
     * @param user the user we want to delete
     */
    public void deleteUser(User user){
        if(userList.contains(user)) {
            userList.remove(user);
        }
    }


}


