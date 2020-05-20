package com.football_system.football_system.FMserver.AcceptanceTests;

import com.football_system.football_system.FMserver.LogicLayer.*;
import com.football_system.football_system.FMserver.ServiceLayer.*;
import com.football_system.football_system.FMserver.DataLayer.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.*;

public class RefereeAcceptanceTest {
    private static IController system;
    private static User user ;
    private static Representative representative ;
    public static Guest testGuest ;
    private static IGuestService testGuestService;
    private static User testUser;
    private static RefereeService testRefereeService;
    private static RepresentativeService testRepService;
    private static UserService testUserService;
    private static Referee referee;


    @BeforeClass
    public static void beforeClass() throws Exception {
        League league = new League("MAJOR_LEAGUE",null,null,null);
        DataComp.setDataManager(new DataManager());
        Administrator administrator = new Administrator("A","B","C");
        user = new User("AA","BB","CC");
        representative = new Representative(user, "lama name");

        user.addRole(representative);
        system = new Controller(representative, administrator);

        testGuest = new Guest();
        system.addGuest(testGuest);

        testGuestService = system.getGuestServices().get(testGuest);
        testUser = testGuestService.register("Lior","Eitan","Lior@gmail.com","12345678");
//        testUserService = (UserService) system.getUserServices().get(testUser).get(0);
        testRepService= (RepresentativeService) system.getUserServices().get(user).get(1);
        testRepService.addNewRefereeFromUsers(testUser ,"","Avi");
        testRepService.addLeague(league.getType(),"Champions League");
    }


    @Before
    public void addReferee(){

        for(IUserService iUserService : system.getUserServices().get(testUser)){
            if(iUserService instanceof  RefereeService){
                testRefereeService  = (RefereeService) iUserService ;
            }
        }
        if(testRefereeService == null) assertTrue(false);
        for(Role role: testUser.getRoles()){
            if(role instanceof Referee){
                referee=(Referee)role;
                testRefereeService.setReferee(referee);
            }
        }
    }



    @Test
    public void test() throws IOException {
        UpdateDetails();
        displayGamesFail();
        displayGames();
        addEventDuringGame();
        addEventAfterGame();
        createGameReport();

    }
    /**
     * A@7
     * UC: 10.1
     */
//    @Test
    public void UpdateDetails(){
        testRefereeService.showDetails();
        try {
            assertFalse(testRefereeService.getReferee().setName("gggy3"));
            assertTrue(testRefereeService.getReferee().setName("yossi"));
            testRefereeService.changeDetails("yos","bestEver");
            testRefereeService.showDetails();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * A@8
     * UC: 10.2 - SUCCESSFUL
     */
//    @Test
    public void displayGames(){
        Game game1 = new Game(null,null,null,null,null,"2020-04-13","12:00","10:00");
        Game game2 = new Game(null,null,null,null,null,"2020-04-13","15:00","20:00");
        testRefereeService.getReferee().addAGameMain(game1);
        testRefereeService.getReferee().addAGameMain(game2);
        try {
            testRefereeService.displayGames();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * A@9
     * UC: 10.2 - FAIL
     */

    public void displayGamesFail(){
        try {
            assertTrue(testRefereeService.displayGames().length==1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    ///// the next two tests are depend on the choice of the time and dates of the games below so
    // we couldn't check the tests with assertTrue/assertFalse.
    // the time and date of the games are critical for this tests
    // so before check this tests change the date and time according to the time and date of your tests

    /**
     *A@10
     * UC: 10.3
     * @throws IOException
     */
//    @Test
    public void addEventDuringGame() throws IOException {
        Game game1 = new Game(null,null,null,null,null,"2020-04-23","12:00","23:00");/// happens now
        Game game2 = new Game(null,null,null,null,null,"2020-04-23","15:00","20:00");// not happens now
        game1.setMain(testRefereeService.getReferee());
        game1.setLine(testRefereeService.getReferee());
        game2.setLine(testRefereeService.getReferee());
        game2.setMain(testRefereeService.getReferee());
        testRefereeService.getReferee().addAGameMain(game1);
        testRefereeService.getReferee().addAGameLine(game2);

        testRefereeService.addGameEvent(game2,"cc","offside"); // game ended
        testRefereeService.addGameEvent(game1,"dd","dd");// no type
        testRefereeService.addGameEventAfterGame(game1,"ss","offside");
    }


    /**
     * A@11
     * UC: 10.4.1
     */
//    @Test
    public void addEventAfterGame(){
        // ended less then 5 hours ago
        Game game1 = new Game(null,null,null,null,null,"2020-04-23","12:00","20:00");
        // not the main judge
        Game game2 = new Game(null,null,null,null,null,"2020-04-23","15:00","20:00");
        // ended more then 5 hoars ago
        Game game3 = new Game(null,null,null,null,null,"2020-04-23","08:00","10:00");
        game1.setMain(testRefereeService.getReferee());
        game1.setLine(testRefereeService.getReferee());
        game2.setLine(testRefereeService.getReferee());
        game2.setMain(new Referee(null,"aa","ss"));
        game3.setMain(testRefereeService.getReferee());
        game3.setLine(testRefereeService.getReferee());
        testRefereeService.getReferee().addAGameMain(game1);
        testRefereeService.getReferee().addAGameLine(game2);
        testRefereeService.getReferee().addAGameLine(game3);

        try {
            testRefereeService.addGameEventAfterGame(game1,"ff","dd"); // not a type
            testRefereeService.addGameEventAfterGame(game1,"ff","offside");  // ok
            testRefereeService.addGameEventAfterGame(game2,"ee","offside"); // not the main
            testRefereeService.addGameEventAfterGame(game3,"ff","offside"); // more then 5 hours
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * A@12
     * UC: 10.4.2
     */
//    @Test
    public void createGameReport(){
        Game game1 = new Game(null,null,null,null,null,"2020-04-23","12:00","20:00");
        // not the main judge
        Game game2 = new Game(null,null,null,null,null,"2020-04-23","15:00","20:00");
        // ended more then 5 hoars ago
        Game game3 = new Game(null,null,null,null,null,"2020-04-23","08:00","23:00");
        game1.setMain(testRefereeService.getReferee());
        game1.setLine(testRefereeService.getReferee());
        game2.setLine(testRefereeService.getReferee());
        game3.setMain(testRefereeService.getReferee());
        game3.setLine(testRefereeService.getReferee());
        game2.setMain(new Referee(null,"aa","ss"));
        testRefereeService.getReferee().addAGameMain(game1);
        testRefereeService.getReferee().addAGameLine(game2);
        testRefereeService.getReferee().addAGameMain(game3);
        try {
            testRefereeService.createGameReport(game1.getId(),"bestGame");
            assertTrue(game1.getGameReport()!=null); // game report created
            testRefereeService.createGameReport(game2.getId(),"bb");
            assertFalse(game2.getGameReport()==null); // not the main referee
            testRefereeService.createGameReport(game3.getId(),"cc");
            assertFalse(game3.getGameReport()==null);// the game is not over yet


        } catch (IOException e) {
            e.printStackTrace();
        }
    }







}
