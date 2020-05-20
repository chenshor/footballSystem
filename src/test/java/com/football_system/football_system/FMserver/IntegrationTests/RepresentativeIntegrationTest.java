package com.football_system.football_system.FMserver.IntegrationTests;
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


public class RepresentativeIntegrationTest {

    @BeforeClass
    public static void SystemInit(){
        DataComp.setDataManager(new DataManager());

        Representative representative = new Representative(new User("l@gmail.com","1234","gabi"),"bingo")  ;
        Controller controller = new Controller(representative,null);
        controller.controllerSingleTone = controller ;
        controller.addUser(new User ("f@f.f","12","fadi"));
    }

    private Representative representative ;
    private RepresentativeService representativeService ;
    private Controller controller = Controller.controllerSingleTone ;
    private List<User> users ;

    @Before
    public void init(){
        DataComp.setDataManager(new DataManager());
      //  List<User>  users = controller.getUserList();
        representativeService = new RepresentativeService(new Controller()) ;
        DataComp.getInstance().addUser(new User("fudi@gamil.com","12","fadi"));
        DataComp.getInstance().addUser(new User("david@gamil.com","12","Adi"));
    }

    /**
     * id: I@26
     * UC: 9.1
     */
    @Test
    public void addLeague() {
        try {
            assertTrue(representativeService.addLeague(League.LeagueType.LEAGUE_C , "Champions League"));
            assertFalse(representativeService.addLeague(League.LeagueType.LEAGUE_C,"Champions League"));//already exist

        } catch (Exception e) {
        }
    }

    /**
     * id: I@27
     * UC: 9.2
     */
    @Test
    public void addSeason(){
        try {
           representativeService.addLeague(League.LeagueType.LEAGUE_C,"Champions League");
           //------ test begins here
           League league = representativeService.showAllLeagus().get(0);
           representativeService.addSeason("2020-02-01","2020-02-03" , league);
           assertTrue(representativeService.showAllSeasons().size()==1);
        }catch (Exception e){ }
    }

    /**
     * id: id: I@28
     * UC: 9.3.1
     */
    @Test
    public void addReferee(){
        try {
            //----- referee not exist test
            User user = representativeService.getSystemUsers().get(0);
            representativeService.addNewRefereeFromUsers(user , "good","gabi");
            assertTrue(representativeService.showAllReferees().size() == 1);
            //----- referee exist test
            assertFalse(representativeService.addNewRefereeFromUsers(user , "good","gabi"));
            assertTrue(representativeService.showAllReferees().size() == 1);
        }catch (Exception e){ }
    }

    /**
     * id: I@29
     * UC: 9.3.2
     */
    @Test
    public void removeReferee() {
        try {
            //----- referee remove test
            User user = representativeService.getSystemUsers().get(0);
            assertTrue(representativeService.addNewRefereeFromUsers(user, "good", "gabi"));
            assertTrue(representativeService.showAllReferees().size() == 1);
            assertTrue(representativeService.removeRefereeFromUsers(user));
            assertTrue(representativeService.showAllReferees().size() == 0);
            //----- referee already removed test
            assertFalse(representativeService.removeRefereeFromUsers(user));
            assertTrue(representativeService.showAllReferees().size() == 0);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    /**
     * id: id: I@30
     * UC: 9.4
     * add first time  - Assert true
     * alreadyExist -> Assert False
     */
    @Test
    public void addApprovalForReferee(){
        try {
            addDataToSystem();
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
     * id: I@31
     * UC: 9.4
     * add first time  - Assert true
     * alreadyExist -> Assert False
     */
    @Test
    public void removeApprovalForReferee(){
        try {
            addApprovalForReferee();
            //  ----- test begins here
            Referee referee = representativeService.showAllReferees().get(0);
            League league = representativeService.showAllLeagus().get(0);
            Season season = representativeService.showAllSeasons().get(0);
            assertTrue(representativeService.removeJudgmentApproval(referee , league ,season)) ;
            //-------- test if approval already removed
            assertFalse(representativeService.removeJudgmentApproval(referee , league ,season)); ;
        }catch (Exception e){
            assertTrue(false);
        }
    }
    /**
     * init data for UC9.4 testing
     */
    public void addDataToSystem(){
        try {
            User user = representativeService.getSystemUsers().get(0);
            representativeService.addNewRefereeFromUsers(user, "good", "gabi");
            addSeason();
        }catch (Exception e){

        }
    }

    /**
     * id: I@32
     * UC9.6 game schedule policy test
     */
    @Test
    public void gameSchedule(){
        try {
            addApprovalForReferee();
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
