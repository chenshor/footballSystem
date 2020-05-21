package com.football_system.football_system.FMserver.IntegrationTests;
import com.football_system.football_system.FMserver.LogicLayer.*;
import com.football_system.football_system.FMserver.ServiceLayer.*;
import com.football_system.football_system.FMserver.DataLayer.*;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.io.IOException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RefereeIntegrationTests {
    private User user;
    private Game game1;
    private Game game2;
    private Referee refereeA;
    private Referee refereeB;
    private RefereeService refereeService1;
    private RefereeService refereeService2;


    @Before
    public void init(){
        League league = new League("MAJOR_LEAGUE",null,null,null);
        user = new User("@","d","d");
        game1 = new Game(null,null,null,null,null,"2020-04-13","12:00","10:00");
        game2 = new Game(null,null,null,null,null,"2020-04-13","15:00","20:00");
        refereeA = new Referee(user,"main","yossi",league);
        refereeB = new Referee(user,"main","haim",league);
        refereeService1= new RefereeService(refereeA);
        refereeService2=new RefereeService(refereeB);
    }

    /**
     * ID: I@1
     */
    @Test
    public void showDetails(){
        System.out.println(refereeService1.showDetails());
        System.out.println(refereeService2.showDetails());
    }

    /**
     * ID: I@2
     */
    @Test
    public void changeDetails(){
        try {
            refereeService1.showDetails();
            refereeService1.changeDetails("d4d","ss");// prints error
            refereeService1.changeDetails("yossi","pp");
            refereeService1.showDetails();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * ID: I@3
     */
    @Test
    public void displayGames(){
        refereeService1.getReferee().addAGameMain(game1);
        refereeService1.getReferee().addAGameMain(game2);
        try {
            refereeService1.displayGames();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // for the next two tests you must change the games time and date for a present game to check it
    /**
     * ID: I@4
     */
    @Test
    public void addGameEvent() throws IOException {
        game1.setMain(refereeService1.getReferee());
        game1.setLine(refereeService2.getReferee());
        game2.setLine(refereeService1.getReferee());
        game2.setMain(refereeService2.getReferee());
        refereeService1.getReferee().addAGameMain(game1);
        refereeService1.getReferee().addAGameLine(game2);

        refereeService1.addGameEvent(game1,"cc","offside"); // game ended
        refereeService1.addGameEvent(game2,"dd","dd");// no type
        refereeService2.addGameEvent(game2,"ee","offside");

    }



    /**
     * ID: I@5
     */
    @Test
    public void addEvntAfterGame(){
        game1.setMain(refereeService1.getReferee());
        game1.setLine(refereeService2.getReferee());
        game2.setLine(refereeService1.getReferee());
        game2.setMain(refereeService2.getReferee());
        refereeService1.getReferee().addAGameMain(game1);
        refereeService1.getReferee().addAGameLine(game2);

        try {
            refereeService1.addGameEventAfterGame(game1,"ff","dd"); // not a type
            refereeService1.addGameEventAfterGame(game1,"ff","offside");
            refereeService2.addGameEventAfterGame(game1,"ee","offside"); // not the main

            refereeService2.addGameEventAfterGame(game2,"ss","offside"); //
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * ID: I@6
     */
    @Test
    public void createGameReport(){
        game1.setMain(refereeService1.getReferee());
        game1.setLine(refereeService2.getReferee());
        refereeService1.getReferee().addAGameMain(game1);
        refereeService2.getReferee().addAGameLine(game1);

        try {
            refereeService1.createGameReport(game1.getId(),"good");
            game1.getGameReport().displayReport();

            refereeService2.createGameReport(game1.getId(),"bad"); // not the main referee
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
