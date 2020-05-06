package com.football_system.football_system.FMserver.ServiceLayer;

import com.football_system.football_system.FMserver.LogicLayer.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuestService implements IGuestService{
    private Guest guest;
    private List<Object> lastSearchResults;
    private IController system;

    public GuestService(Guest guest, IController system) {
        this.guest = guest;
        this.system = system;
        this.lastSearchResults = new ArrayList<>();
    }

    public GuestService(Controller control) {
        super();
    }

    /**
     * returns last search results searched by guest
     * @return
     */
    public List<Object> getLastSearchResults() {
        return lastSearchResults;
    }

    /**
     * Use Case - 2.1
     * Register to the System
     * id: GuestService@1
     * @param firstName
     * @param lastName
     * @param email
     * @param password
     */
    public User register(String firstName, String lastName, String email, String password){
        boolean passwordIsOk = Authentication(password);
        if(passwordIsOk == false){
            return null;
        }
        boolean mailIsOk = mailAuthentication(email);
        if (mailIsOk == false){
            System.out.println("## email isn't in the right format ##");
            return null;
        }
        boolean isExists = guest.checkIfEmailExists(email);
        if (isExists == true){
            System.out.println("## user with this email exists in system. ##");
            return null;
        }
        User newUser = guest.createNewUser(email,password,firstName,lastName);
        System.out.println("## Registered to system successfully ##");
        system.addUser(newUser);
        system.createFanServiceForUser(newUser, (Fan)newUser.getRoles().get(0));
        system.removeGuest(guest);
        return newUser;
    }

    /**
     * id: GuestService@2
     * checks if password entered is valid
     * @param password
     * @return
     */
    public boolean Authentication(String password){
        for (char c : password.toCharArray()){
            if (!((c>='A' && c<='z')||(c>='0' && c<='9'))){
                System.out.println("## Password can contain only digits and letters. ##");
                return false;
            }
        }
        if(password.length()<8){
            System.out.println("## Password must has at least 8 characters. ##");
            return false;
        }
        return true;
    }

    /**
     * id: GuestService@3
     * checks if mail entered is in correct form
     * @param email
     * @return
     */
    public boolean mailAuthentication(String email){
        if (email != null) {
            Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
            Matcher mat = pattern.matcher(email);
            if (mat.matches()) {
                return true;
            }
        }
        return false;
    }

    /**
     * id: GuestService@4
     * Use Case - 2.2
     * User Login
     * @param email
     * @param password
     */
    public boolean logIn(String email, String password){
        if(signIn(email,password) == null)
            return false;
        return true;
    }

    /**
     * id: GuestService@5
     * sign in func for guest
     * @param email
     * @param password
     * @return
     */
    @Override
    public User signIn(String email, String password) {
        boolean passwordIsOk = Authentication(password);
        if(passwordIsOk == false){
            return null;
        }
        User userToSignIn = guest.signIn(email, password);
        if (userToSignIn == null){
            System.out.println("## Wrong email or password ##");
            return null;
        }
        system.addUser(userToSignIn);
        system.addServicesToUser(userToSignIn);
        system.removeGuest(guest);
        return userToSignIn;
    }

    /**
     * id: GuestService@6
     * Use Case - 2.3
     * Show public information
     * @param interestIn
     */
    public void showInformationByCategory(Interest interestIn){
        lastSearchResults = new ArrayList<>();
        switch (interestIn) {
            case Games:
                List<Game> gamesInfo = guest.retrieveGames();
                for (Game game: gamesInfo){
                    lastSearchResults.add(game);
                    System.out.println(game.toString());
                }
                break;
            case Players:
                List<Player> playersInfo = guest.retrievePlayers();
                for (Player player: playersInfo){
                    lastSearchResults.add(player);
                    System.out.println(player.toString());
                }
                break;
            case Leagues:
                List<League> leaguesInfo = guest.retrieveLeagues();
                for (League league: leaguesInfo){
                    lastSearchResults.add(league);
                    System.out.println(league.toString());
                }
                break;
            case Teams:
                List<Team> teamsInfo = guest.retrieveTeams();
                for (Team team: teamsInfo){
                    lastSearchResults.add(team);
                    System.out.println(team.toString());
                }
                break;
            case Seasons:
                List<Season> seasonsInfo = guest.retrieveSeasons();
                for (Season season: seasonsInfo){
                    lastSearchResults.add(season);
                    System.out.println(season.toString());
                }
                break;
            case Coaches:
                List<Coach> coachesInfo = guest.retrieveCoaches();
                for (Coach coach: coachesInfo){
                    lastSearchResults.add(coach);
                    System.out.println(coach.toString());
                }
                break;
            case Owners:
                List<Owner> ownersInfo = guest.retrieveOwners();
                for (Owner owner: ownersInfo){
                    lastSearchResults.add(owner);
                    System.out.println(owner.toString());
                }
                break;
            case Managers:
                List<Manager> managersInfo = guest.retrieveManagers();
                for (Manager manager: managersInfo){
                    lastSearchResults.add(manager);
                    System.out.println(manager.toString());
                }
                break;
        }
    }

    /**
     * id: GuestService@7
     * USE CASE - 2.4
     * Search Information
     * @param criteria
     * @param query
     */
    @Override
    public void searchInformation(Criteria criteria, String query) {
        lastSearchResults = new ArrayList<>();
        switch (criteria) {
            case Category:
                searchInformationByCategory(query);
                break;
            case KeyWord:
                searchInformationByKeyWord(query);
                break;
            case Name:
                searchInformation(query);
                break;
        }
    }

    /**
     * id: GuestService@8
     * receives String name as argument and returns all users with the same first name
     * related to use case 2.4
     * @param name
     */
    private void searchInformation(String name){
        List<User> retrievedUser =  guest.SearchUserByName(name);
        if(retrievedUser.size() == 0){
            System.out.println("## There is no person with this name ##");
            return;
        }
        for(User user : retrievedUser){
            lastSearchResults.add(user);
            System.out.println(user.toString());
        }
    }

    /**
     * id: GuestService@9
     * @param interested
     */
    private void searchInformationByCategory(Interest interested){
        showInformationByCategory(interested);
    }

    /**
     * id: GuestService@10
     * receives category as string and call to searchInformationByKeyWord func with its argument
     * shows all related information due to query
     * related to use case 2.4
     * @param interested
     */
    private void searchInformationByCategory(String interested){
        searchInformationByKeyWord(interested);
    }

    /**
     * id: GuestService@11
     * receives query and search in DB for all related data
     * related to use case 2.4
     * @param query
     */
    private void searchInformationByKeyWord(String query){
        switch (query.toLowerCase()){
            case "coaches":
                searchInformationByCategory(Interest.Coaches);
                break;
            case "games":
                searchInformationByCategory(Interest.Games);
                break;
            case "managers":
                searchInformationByCategory(Interest.Managers);
                break;
            case "owners":
                searchInformationByCategory(Interest.Owners);
                break;
            case "players":
                searchInformationByCategory(Interest.Players);
                break;
            case "seasons":
                searchInformationByCategory(Interest.Seasons);
                break;
            case "teams":
                searchInformationByCategory(Interest.Teams);
                break;
            case "leagues":
                searchInformationByCategory(Interest.Leagues);
                break;
            default:
                // Users (players, coaches, etc.)
                searchInformation(query);
                // Teams
                searchTeams(query);
                // Leagues
                searchLeagues(query);
        }
    }

    /**
     * id: GuestService@12
     * receives league name and returns leagues with equal league type
     * related to use case 2.4
     * @param leagueName
     */
    private void searchLeagues(String leagueName) {
        List<League> retrievedLeague =  guest.SearchLeagueByName(leagueName);
        if(retrievedLeague == null){
            System.out.println("## There is no league with this name ##");
            return;
        }
        for(League league : retrievedLeague){
            lastSearchResults.add(league);
            System.out.println(league.toString());
        }
    }

    /**
     * id: GuestService@13
     *  receives team name and returns teams with equal team name
     *  related to use case 2.4
     * @param teamName
     */
    private void searchTeams(String teamName) {
        List<Team> retrievedTeam =  guest.SearchTeamByName(teamName);
        if(retrievedTeam == null){
            System.out.println("## There is no team with this name ##");
            return;
        }
        for(Team team : retrievedTeam){
            lastSearchResults.add(team);
            System.out.println(team.toString());
        }
    }

    /**
     * id: GuestService@14
     * USE CASE - 2.5
     * Filter Search Results
     * @param filter
     * @param query
     */
    @Override
    public void filterResults(Filter filter, String query) {
        switch (filter) {
            case Role:
                filterByRole(query);
                break;
            case Team:
                filterByTeam(query);
                break;
            case League:
                filterByLeague(query);
                break;
        }
    }

    /**
     * id: GuestService@15
     * filters search results by league
     * related to use case 2.5
     * @param query
     */
    private void filterByLeague(String query) {
        List<Object> filtered = new ArrayList<>();
        for (Object obj : lastSearchResults){
            if(obj instanceof RoleHolder){
                String league = ((RoleHolder) obj).getTeam().getLeague().getName().toLowerCase();
                if(league.equals(query.toLowerCase()))
                    filtered.add(obj);
            }else if(obj instanceof Team){
                String league = ((Team) obj).getLeague().getName().toLowerCase();
                if(league.equals(query.toLowerCase()))
                    filtered.add(obj);
            }else if(obj instanceof League){
                if(((League) obj).getName().equals(query.toLowerCase()))
                    filtered.add(obj);
            }
        }
        filtered.forEach(obj -> System.out.println(obj.toString()));
    }

    /**
     * id: GuestService@16
     * filter search results by team
     * related to use case 2.5
     * @param query
     */
    private void filterByTeam(String query) {
        List<Object> filtered = new ArrayList<>();
        for (Object obj : lastSearchResults){
            if(obj instanceof RoleHolder){
                if(((RoleHolder) obj).getTeam().getName().toLowerCase().equals(query.toLowerCase()))
                    filtered.add(obj);
            }
        }
        filtered.forEach(obj -> System.out.println(obj.toString()));
    }

    /**
     * id: GuestService@17
     * filter search results by Role(coach, player and etc..)
     * related to use case 2.5
     * @param query
     */
    private void filterByRole(String query) {
        List<Object> filtered = new ArrayList<>();
        switch (query.toLowerCase()) {
            case "player":
                for (Object obj : lastSearchResults){
                    if(obj instanceof Player || obj instanceof User){
                        if(obj instanceof User){
                            ((User) obj).getRoles().forEach(r -> {
                                if(r instanceof Player)
                                    filtered.add(obj);
                            });
                        }
                        filtered.add(obj);
                    }
                }
                break;
            case "manager":
                for (Object obj : lastSearchResults){
                    if(obj instanceof Manager || obj instanceof User){
                        if(obj instanceof User){
                            ((User) obj).getRoles().forEach(r -> {
                                if(r instanceof Manager)
                                    filtered.add(obj);
                            });
                        }
                        filtered.add(obj);
                    }
                }
                break;
            case "owner":
                for (Object obj : lastSearchResults){
                    if(obj instanceof Owner || obj instanceof User){
                        if(obj instanceof User){
                            ((User) obj).getRoles().forEach(r -> {
                                if(r instanceof Owner)
                                    filtered.add(obj);
                            });
                        }
                        filtered.add(obj);
                    }
                }
                break;
            case "coach":
                for (Object obj : lastSearchResults){
                    if(obj instanceof Coach || obj instanceof User){
                        if(obj instanceof User){
                            ((User) obj).getRoles().forEach(r -> {
                                if(r instanceof Coach)
                                    filtered.add(obj);
                            });
                        }
                        filtered.add(obj);
                    }
                }
                break;
            case "referee":
                for (Object obj : lastSearchResults){
                    if(obj instanceof Referee || obj instanceof User){
                        if(obj instanceof User){
                            ((User) obj).getRoles().forEach(r -> {
                                if(r instanceof Referee)
                                    filtered.add(obj);
                            });
                        }
                        filtered.add(obj);
                    }
                }
                break;
        }
        filtered.forEach(obj -> System.out.println(obj.toString()));
    }

    public List<Team> getTeams(){
        return guest.retrieveTeams();
    }
}
