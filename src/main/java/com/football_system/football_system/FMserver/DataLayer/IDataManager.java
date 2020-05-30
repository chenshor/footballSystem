package com.football_system.football_system.FMserver.DataLayer;
import com.football_system.football_system.FMserver.DataLayer.Repositories.IRefereeRepository;
import com.football_system.football_system.FMserver.LogicLayer.*;

import java.util.List;
import java.util.Map;

public interface IDataManager {

    void addTeam(Team team);
    void deleteUser(User user);
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
    void addAlert(Alert alert) ;
    List<Alert> getAlerts();
    //void addUser(User ownerUser);
    User getUser(String userName);
    boolean checkIfEmailExists(String email);
    void addNewUser(User newUser);
    //List<Guest> getGuestsList();
    //void setGuestsList(List<Guest> guestsList);
    //void setUserList(List<User> userList);
    //void setAlerts(Map<User, List<Alert>> alerts);
    List<Complaint> getComplaint();
    //void setComplaint(Map<User, List<Complaint>> complaint);
    //void setLeagueList(List<League> leagueList);
    //void setSeasonList(List<Season> seasonList);
    List<Team> getTeamList();
    //void setTeamList(List<Team> teamList);
    List<Page> getPageList();
    List<Game> getGameList();
    List<Coach> getCoaches();
    List<Owner> getOwners();
    List<Manager> getManagers();
    List<Player> getPlayers();
    List<User> searchUserByName(String name);
    void addComplaint(Complaint newComplaint);
    List<League> searchLeagueByName(String league);
    List<Team> searchTeamByName(String team);
    void addNameHistory(Fan fan, String query);
    void addKeyWordHistory(Fan fan, String query);
    void addCategoryHistory(Fan fan, String query);
    List<String> getCategorySearchHistory(Fan fan);
    List<String> getKeyWordSearchHistory(Fan fan);
    List<String> getNameSearchHistory(Fan fan);
    //HashMap<Fan, List<String>> getFanSearchKeyWordHistory();
    //HashMap<Fan, List<String>> getFanSearchCategoryHistory();
    //HashMap<Fan, List<String>> getFanSearchNameHistory();
    //void addComplaint(Complaint complaint,User user) ;
    void addFan(Fan fan);
    void addGame(Game game);
    void addManager(Manager manager);

    void addCoach(Coach coach);

    void addPlayer(Player player);

    void deletePlayer(Player player);

    void deleteCoach(Coach coach);

    void addOwner(Owner owner);

    void deleteManager(Manager manager);

    void deleteOwner(Owner nominated);

    void addPage(Page page);

    void addPolicy(Policy policy);

    void addRankedPolicy(RankPolicy rankPolicy);

    void addResult(Result result);

    void addGameEvent(GameEventCalender event);

    Fan getFan(int r_id);

    void deleteFan(int r_id);

    void addGameReport(GameReport gameReport);

    void addJudgementApproval(JudgmentApproval judgmentApproval);

    void addRepresentetive(Representative representative);

    void deleteJudgmentApproval(JudgmentApproval approval);

    void addAdministrator(Administrator administrator);

    void addUser(User user);

    List<JudgmentApproval> getJudge();
}
