package com.football_system.football_system.FMserver.ServiceLayer;
import com.football_system.football_system.FMserver.LogicLayer.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface IUserService {

    void addPage(Page newPage) throws IOException;

    List<User> getSystemUsers();

    void addPages(List<Page> newPages) throws IOException;

    void logOut() throws IOException;

    List<String> showPersonalInformation() throws IOException;

    boolean editPersonalInformation(String firstName, String lastName, String email) throws IOException;

    List<String> retrieveHistory(Criteria criteria) throws IOException;

    void searchInformation(Criteria criteria, String query) throws IOException;

    void report(String description) throws IOException;

    void addUpdate(String update) throws IOException;

    void addSeason(Date start, Date end, League league) throws IOException;

    void closeTeam(Team team) throws IOException;

    Collection<List<Complaint>> showComplaints() throws IOException;

    void commentComplaint(Complaint complaint, String comment) throws IOException;

    boolean removeRefereeFromUsers(User user, String qualification, String name) throws IOException;

    boolean RemoveNewRefereeFromUsers(User user) throws IOException;

    String setLeague(League.LeagueType leagueType) throws IOException;

    List<League> showAllLeagus() throws IOException;

    List<Season> showAllSeasons() throws IOException;

    void createGameReport(Integer game_id, String description)throws IOException ;

    void addGameEventAfterGame(Game game, String description, String eventType) throws IOException;

    void addGameEvent(Game game, String description, String eventType) throws IOException;

    String[] displayGames() throws IOException;

    void changeDetails(String newName, String newCualif) throws IOException;

    String showDetails() throws IOException;

    void deleteUser(User user) throws IOException;

    void displayLog() throws IOException;

    List<Game> scheduleGame(League league, int numberOfGamesPerTeam, Season season, List<String[]> allPossiableTimes) throws IOException;

}
