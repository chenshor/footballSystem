package com.football_system.football_system.FMserver.ServiceLayer;

import com.football_system.football_system.FMserver.DataLayer.*;
import com.football_system.football_system.FMserver.LogicLayer.*;


import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public abstract class AUserService implements IUserService {

    Controller control;

    public AUserService() {
    }

    public AUserService(Controller control) {
        this.control = control;
    }

    /**
     * Id: AUserService@1
     *
     * @return system users
     */
    public List<User> getSystemUsers() {
        return User.getAllUsers();
    }

    @Override
    public void addPage(Page newPage) throws IOException {
        throw new IOException("not possible");
    }


    @Override
    public String showDetails() throws IOException {
        throw new IOException("No details to be shown");
    }

    @Override
    public void changeDetails(String newName, String newCualif) throws IOException {
        throw new IOException("No details to be changed");
    }

    @Override
    public String[] displayGames() throws IOException {
        throw new IOException("no games");
    }

    @Override
    public void addGameEvent(Game game, String description, String eventType) throws IOException {
        throw new IOException("no games");
    }

    @Override
    public void addGameEventAfterGame(Game game, String description, String eventType) throws IOException {
        throw new IOException("no games");
    }

    @Override
    public void createGameReport(Integer game_id , String description) throws IOException {
        throw new IOException("no games");
    }

    @Override
    public String setLeague(League.LeagueType leagueType) throws IOException {
        throw new IOException("Unsupported method");
    }

    @Override
    public List<League> showAllLeagus() throws IOException {
        throw new IOException("Unsupported method");
    }

    @Override
    public void addSeason(Date start, Date end, League league) throws IOException {
        throw new IOException("Unsupported method");
    }

    @Override
    public List<Season> showAllSeasons() throws IOException {
        throw new IOException("Unsupported method");
    }

    @Override
    public boolean removeRefereeFromUsers(User user, String qualification, String name) throws IOException {
        throw new IOException("Unsupported method");
    }

    @Override
    public boolean RemoveNewRefereeFromUsers(User user) throws IOException {
        throw new IOException("Unsupported method");
    }

    @Override
    public void addPages(List<Page> newPages) throws IOException {
        throw new IOException("not possible");
    }

    @Override
    public void logOut() throws IOException {
        throw new IOException("not possible");
    }

    @Override
    public List<String> showPersonalInformation() throws IOException {
        throw new IOException("not possible");
    }

    @Override
    public boolean editPersonalInformation(String firstName, String lastName, String email) throws IOException {
        throw new IOException("not possible");
    }

    @Override
    public List<String> retrieveHistory(Criteria criteria) throws IOException {
        throw new IOException("not possible");
    }

    @Override
    public void searchInformation(Criteria criteria, String query) throws IOException {
        throw new IOException("not possible");
    }

    @Override
    public void report(String description) throws IOException {
        throw new IOException("not possible");
    }

    @Override
    public void addUpdate(String update) throws IOException {
        throw new IOException("not possible");
    }

    @Override
    public void closeTeam(Team team) throws IOException {
        throw new IOException("Unsupported method");
    }

    @Override
    public Collection<List<Complaint>> showComplaints() throws IOException {
        throw new IOException("Unsupported method");

    }

    @Override
    public void commentComplaint(Complaint complaint, String comment) throws IOException {
        throw new IOException("Unsupported method");

    }

    @Override
    public void deleteUser(User user) throws IOException {
        throw new IOException("Unsupported method");
    }

    @Override
    public void displayLog() throws IOException {
        throw new IOException("Unsupported method");
    }

    @Override
    public List<Game> scheduleGame(League league, int numberOfGamesPerTeam, Season season, List<String[]> allPossiableTimes) throws IOException {
        throw new IOException("Unsupported method");
    }
}
