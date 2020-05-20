package com.football_system.football_system.FMserver.AcceptanceTests;

import com.football_system.football_system.FMserver.LogicLayer.*;
import com.football_system.football_system.FMserver.ServiceLayer.*;
import com.football_system.football_system.FMserver.DataLayer.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class RepresentativeAcceptanceTest {

    private static IController system;
    private static  User user ;
    private static Representative representative ;
    public static Guest guest ;
    private static  User newUser ;

    @BeforeClass
    public static void beforeClass() throws Exception {
        DataComp.setDataManager(new DataManager());
        Administrator administrator = new Administrator("A","B","C");
        user = new User("AA","BB","CC");
        representative = new Representative(user, "lama name");
        user.addRole(representative);
        system = new Controller(representative, administrator);
        guest = new Guest();
        system.addGuest(guest);
        IGuestService guestService = system.getGuestServices().get(guest);
        guestService.register("David","Fadida","Eitan@gmail.com","password");
        guestService.register("fff","Fadida","fftan@gmail.com","password");
        newUser = guestService.signIn("Eitan@gmail.com","password");
    }

    private RepresentativeService representativeService;
    @Before
    public void init(){
        for(IUserService iUserService : system.getUserServices().get(user)){
            if(iUserService instanceof  RepresentativeService){
                representativeService  = (RepresentativeService) iUserService ;
            }
        }
        if(representativeService == null) assertTrue(false);
    }



    @Test
    public void  testSystem(){
        addLeague();
        addSeason();
        addReferee();
        checkSystemStatus(true);
        removeReferee();
        checkSystemStatus(false);
        addReferee();
        checkSystemStatus(true);
        addApprovalForReferee();
        gameSchedule();
    }

    public void checkSystemStatus(boolean exist){
        boolean check = false ;
        if(system.getUserServices().get(newUser) == null ) assertTrue(false);
        for(IUserService iUserService : system.getUserServices().get(newUser)){
            if(iUserService instanceof  RefereeService){
                check = true ;
            }
        }
        assertTrue(exist == check);
    }

    /**
     * Id: A@1
     * UC: 9.1
     */
    public void addLeague() {
        try {
            assertTrue(representativeService.addLeague(League.LeagueType.LEAGUE_C ,"Champions League"));
            assertFalse(representativeService.addLeague(League.LeagueType.LEAGUE_C,"Champions League"));//already exist

        } catch (Exception e) {
        }
    }

    /**
     * Id: A@2
     * UC: 9.2
     */
    public void addSeason(){
        try {
            League league = representativeService.showAllLeagus().get(0);
            representativeService.addSeason("2020-02-01","2020-02-03" , league);
            assertTrue(representativeService.showAllSeasons().size()==1);
        }catch (Exception e){ }
    }

    /**
     * id : A@3
     * UC 9.3.1
     */
    public void addReferee(){
        try {
            //----- referee not exist test
            representativeService.addNewRefereeFromUsers(newUser , "good","gabi");
            assertTrue(representativeService.showAllReferees().size() == 1);
            //----- referee exist test
            assertFalse(representativeService.addNewRefereeFromUsers(newUser , "good","gabi"));
            assertTrue(representativeService.showAllReferees().size() == 1);
        }catch (Exception e){ }
    }

    /**
     * id: A@4
     * UC 9.3.2
     */
    public void removeReferee() {
        try {
            //----- referee remove test
            assertTrue(representativeService.removeRefereeFromUsers(newUser));
            assertTrue(representativeService.showAllReferees().size() == 0);
            //----- referee already removed test
            assertFalse(representativeService.removeRefereeFromUsers(newUser));
            assertTrue(representativeService.showAllReferees().size() == 0);
        } catch (Exception e) {
        }
    }

    /**
     * id : A@5
     * UC 9.4
     */
    public void addApprovalForReferee(){
        try {
            //  ------------------- add approval test begins here
            Referee referee = representativeService.showAllReferees().get(0);
            League league = representativeService.showAllLeagus().get(0);
            Season season = representativeService.showAllSeasons().get(0);
            assertTrue(representativeService.addJudgmentApproval(referee , league ,season)) ;
            //------------------- alreadyExist approval test
            assertFalse(representativeService.addJudgmentApproval(referee , league ,season));
        }catch (Exception e){
            assertTrue(false);
        }
    }

    /**
     * id: A@6
     * UC: 9.6
     */
    public void gameSchedule(){
        try {
            User user = representativeService.getSystemUsers().get(1);
            representativeService.addNewRefereeFromUsers(user , "good","gabi");
            // Referee referee = representativeService.showAllReferees().get(0);
            League league = representativeService.showAllLeagus().get(0);
            Season season = representativeService.showAllSeasons().get(0);
            Referee referee = representativeService.showAllReferees().get(1);
            representativeService.addJudgmentApproval(referee , league ,season);
            List<String[]> dates = new LinkedList<>();
            String[] date1 = {"2020-01-09" ,"19:00","20:30"};
            dates.add(date1 );
            String[] date2 = {"2020-02-09" ,"19:00","20:30"};
            dates.add(date2 );
            String[] date3 = {"2020-03-09" ,"19:00","20:30"};
            dates.add(date3);
            String[] date4 = {"2020-04-09" ,"19:00","20:30"};
            dates.add(date4 );
            String[] date5 = {"2020-05-09" ,"19:00","20:30"};
            dates.add(date5 );
            String[] date6 = {"2020-06-09" ,"19:00","20:30"};
            dates.add(date6 );
            Team t1 = new Team("1", "bi", null);
            t1.setLeague(league);
            Team t2 = new Team("2", "bi", null);
            t2.setLeague(league);
            Team t3 = new Team("3", "bi", null);
            t3.setLeague(league);
            DataComp.getInstance().addTeam(t1);
            DataComp.getInstance().addTeam(t2);
            DataComp.getInstance().addTeam(t3);
            System.out.println("----------new schedule ----------");
            assertTrue(representativeService.scheduleGame(league,1,season,dates).size()==3);
            System.out.println("----------new schedule ----------");
            assertTrue(representativeService.scheduleGame(league,2,season,dates).size()==6);
            System.out.println(t1.getHome().get(0).getMain());
            Team t4 = new Team("4", "bi", null);
            t4.setLeague(league);
            DataComp.getInstance().addTeam(t4);
            System.out.println("----------new schedule ----------");
            assertTrue(representativeService.scheduleGame(league,1,season,dates).size()==6);
            //  System.out.println(t4.getHome().get(0).getMain());
        }catch (Exception e){
            e.fillInStackTrace();
            assertTrue(false);
        }
    }
}