package com.football_system.football_system.FMserver.DataLayer;
import com.football_system.football_system.FMserver.LogicLayer.*;

import java.io.File;
import java.util.*;

import java.util.List;
import java.util.Map;

public interface IDataManager {

    void addTeam(Team team);
    void addAlert(Alert alert,User user);
    void deleteUser(User user);
    User getUserByPassword(String userName, String password);
    User getUserByMail(String userName, String password);
    League SearchLeagueByType(League.LeagueType leagueType);
    void addLeague(League league) ;
    void addSeason(Season season) ;
    Season SearchSeason(String start , String End);
    List<League> getLeagueList();
    List<Season> getSeasonList();
    boolean addReferee(Referee referee) ;
    boolean removeReferee(Referee referee) ;
    List<User> getUserList();
    List<Referee> getRefereeList() ;
    void addAlert(User user,Alert alert) ;
    Map<User, List<Alert>> getAlerts();
    void addUser(User ownerUser);
    User getUser(String userName, String password);
    boolean checkIfEmailExists(String email);
    void addNewUser(User newUser);
    List<Guest> getGuestsList();
    void setGuestsList(List<Guest> guestsList);
    void setUserList(List<User> userList);
    void setAlerts(Map<User, List<Alert>> alerts);
    Map<User, List<Complaint>> getComplaint();
    void setComplaint(Map<User, List<Complaint>> complaint);
    void setLeagueList(List<League> leagueList);
    void setSeasonList(List<Season> seasonList);
    List<Team> getTeamList();
    void setTeamList(List<Team> teamList);
    List<Page> getPageList();
    List<Game> getGameList();
    List<Coach> getCoaches();
    List<Owner> getOwners();
    List<Manager> getManagers();
    List<Player> getPlayers();
    List<User> searchUserByName(String name);
    void addComplaint(User user, Complaint newComplaint);
    List<League> searchLeagueByName(String league);
    List<Team> searchTeamByName(String team);
    void addNameHistory(Fan fan, String query);
    void addKeyWordHistory(Fan fan, String query);
    void addCategoryHistory(Fan fan, String query);
    List<String> getCategorySearchHistory(Fan fan);
    List<String> getKeyWordSearchHistory(Fan fan);
    List<String> getNameSearchHistory(Fan fan);
    HashMap<Fan, List<String>> getFanSearchKeyWordHistory();
    HashMap<Fan, List<String>> getFanSearchCategoryHistory();
    HashMap<Fan, List<String>> getFanSearchNameHistory();
    void addComplaint(Complaint complaint,User user) ;

}
