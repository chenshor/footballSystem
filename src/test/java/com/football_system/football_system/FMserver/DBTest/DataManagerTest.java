package com.football_system.football_system.FMserver.DBTest;
import com.football_system.football_system.FMserver.DataLayer.DataManager;
import com.football_system.football_system.FMserver.LogicLayer.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;;
import java.util.List;
import static junit.framework.TestCase.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class DataManagerTest {

    @Autowired
    private DataManager dataManager;

    @Test
    public void addFanTest(){
        User user = new User("a@e.com","12345","eitan","platok");
        Fan fan = new Fan(user,"eitan");
        user.addRole(fan);
        dataManager.addNewUser(user);
        dataManager.addFan(fan);
        Fan fanFromDb = dataManager.getFan(fan.getR_id());
        assertTrue(fan.getUser().equals(fanFromDb.getUser()));
        assertTrue(fan.getName().equals(fanFromDb.getName()));
    }

    @Test
    public void getFan(){
        User user = new User("a@e.com","12345","eitan","platok");
        Fan fan = new Fan(user,"eitan");
        user.addRole(fan);
        dataManager.addNewUser(user);
        dataManager.addFan(fan);
        Fan fanFromDb = dataManager.getFan(fan.getR_id());
        assertTrue(fan.getName().equals(fanFromDb.getName()));
    }

    @Test
    public void deleteFan(){
        User user = new User("a@e.com","12345","eitan","platok");
        Fan fan = new Fan(user,"eitan");
        user.addRole(fan);
        dataManager.addNewUser(user);
        dataManager.addFan(fan);
        dataManager.deleteUser(user);
        Fan fanFromDb = dataManager.getFan(fan.getR_id());
        assertNull(fanFromDb);
    }

    @Test
    public void checkIfEmailExistsTest(){
        User user = new User("a@e.com","12345","eitan","platok");
        dataManager.addNewUser(user);
        assertTrue(dataManager.checkIfEmailExists(user.getEmail()));
    }

    @Test
    public void addNewUserTest(){
        User user = new User("a@e.com","12345","eitan","platok");
        dataManager.addNewUser(user);
        assertTrue(dataManager.checkIfEmailExists(user.getEmail()));
    }

    @Test
    public void getUserByEmailTest(){
        User user = new User("a@e.com","12345","eitan","platok");
        dataManager.addNewUser(user);
        User user1 = dataManager.getUserByMail(null , user.getEmail());
        assertTrue(user1.getEmail().equals(user.getEmail()));
        assertNull(dataManager.getUserByMail(null , "a@a.com"));
    }

    @Test
    public void SearchLeagueByTypeTest(){
        League league = new League(League.LeagueType.MAJOR_LEAGUE,null,null,null);
        dataManager.addLeague(league);
        League leagueFromDb = dataManager.getLeagueList().get(0);
        assertTrue(league.getTypeString().equals(leagueFromDb.getTypeString()));
    }

    @Test
    public void addLeagueTest(){
        League league = new League(League.LeagueType.MAJOR_LEAGUE,null,null,null);
        dataManager.addLeague(league);
        League leagueFromDb = dataManager.getLeagueList().get(0);
        assertTrue(league.getTypeString().equals(leagueFromDb.getTypeString()));
    }

    @Test
    public void searchSeasonTest(){
        Season s = new Season(null,"05-05-1995","09-09-1998", null, null, null);
        dataManager.addSeason(s);
        assertTrue(dataManager.getSeasonList().contains(s));
    }

    @Test
    public void addRefereeTest() {
        League league = new League(League.LeagueType.MAJOR_LEAGUE,null,null,null);
        User user = new User("alona@queen","02580258","alona","lasry");
        Referee ref = new Referee(user,"harta","barta",league);
        dataManager.addNewUser(user);
        dataManager.addReferee(ref);
        dataManager.addLeague(league);
        assertTrue(dataManager.getRefereeList().contains(ref));
        assertTrue(dataManager.getUserList().contains(user));
        assertTrue(dataManager.getLeagueList().contains(league));
    }

    @Test
    public void removeRefereeTest(){
        User user = new User("alona@qn","02580258","al","la");
        Referee referee = new Referee(user,"eitan",null,null);
        dataManager.addNewUser(user);
        dataManager.addReferee(referee);
        dataManager.removeReferee(referee);
        assertFalse(dataManager.getRefereeList().contains(referee));
    }

    @Test
    public void addTeam(){
        Team team = new Team("maccabi",null,null);
        dataManager.addTeam(team);
        dataManager.getTeamList().contains(team);
    }


    @Test
    public void addComplaint(){
        User user = new User("aon@qn","02580258","al","la");
        Complaint complaint = new Complaint(user,"null",null);
        dataManager.addNewUser(user);
        dataManager.addComplaint(complaint);
        List<Complaint> complaintList = dataManager.getComplaint();
        assertTrue(dataManager.getComplaint().contains(complaint));
    }

    @Test
    public void getOwners(){
        User user = new User("aona@qn","02580258","al","la");
        Owner owner = new Owner(user,"foo");
        user.addRole(owner);
        dataManager.addOwner(owner);
        dataManager.addNewUser(user);
        List<Owner>owners = dataManager.getOwners();
        assertTrue(owners.contains(owner));
    }

    @Test
    public void getManagers(){
        User user = new User("a@qn","02580258","al","la");
        Manager manager = new Manager(user,"fo",null);
        user.addRole(manager);
        dataManager.addManager(manager);
        dataManager.addNewUser(user);
        List<Manager>managers = dataManager.getManagers();
        assertTrue(managers.contains(manager));

    }

    @Test
    public void getPlayers(){
        User user = new User("b@qn","02580258","al","la");
        Player player = new Player(user,"null",null,null,null,null);
        user.addRole(player);
        dataManager.addPlayer(player);
        dataManager.addNewUser(user);
        assertTrue(dataManager.getPlayers().contains(player));
    }

    @Test
    public void getCoaches(){
        User user = new User("r@qn","02580258","al","la");
        Coach coach = new Coach(user,"coach",null,null,null,null);
        user.addRole(coach);
        dataManager.addCoach(coach);
        dataManager.addNewUser(user);
        assertTrue(dataManager.getCoaches().contains(coach));
    }

    @Test
    public void searchUserByNameTest(){
        User user = new User("eeE@E","aaaa","eitan","platok");
        dataManager.addNewUser(user);
        List<User>users = dataManager.searchUserByName("eitan");
        assertTrue(users.contains(user));
    }

    @Test
    public void searchLeagueByNameTest(){
        League league = new League(League.LeagueType.LEAGUE_B,null,null,null);
        league.setName("seria A");
        dataManager.addLeague(league);
        List<League>leagueList = dataManager.searchLeagueByName("seria A");
        assertTrue(leagueList.contains(league));
    }

    @Test
    public void searchTeamByNameTest(){
        Team team = new Team("Hapoel BS",null,null);
        dataManager.addTeam(team);
        List<Team>teams = dataManager.searchTeamByName("Hapoel BS");
        assertTrue(teams.contains(team));
    }

    @Test
    public void addGameTest(){
        Game game = new Game(null,null,null,null,null,null,null,"16/06/16",null);
        GameReport gameReport = new GameReport(game,null);
        game.setGameReport(gameReport);
        dataManager.addGame(game);
        List<Game>games = dataManager.getGameList();
        assertTrue(games.contains(game));
    }

    @Test
    public void addAlertTest(){
        User user = new User("rt@qn","02580258","al","la");
        Alert alert = new Alert(user,"yoyo",null);
        dataManager.addNewUser(user);
        dataManager.addAlert(alert);
        List<Alert>alerts = dataManager.getAlerts();
        assertTrue(alerts.contains(alert));
    }
}
