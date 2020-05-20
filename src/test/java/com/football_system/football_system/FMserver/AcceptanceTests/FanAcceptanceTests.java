package com.football_system.football_system.FMserver.AcceptanceTests;

import com.football_system.football_system.FMserver.LogicLayer.*;
import com.football_system.football_system.FMserver.ServiceLayer.*;
import com.football_system.football_system.FMserver.DataLayer.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FanAcceptanceTests {
    private static IController system;
    private static User user ;
    private static Representative representative ;
    public static Guest testGuest ;
    private static IGuestService testGuestService;
    private static User testUser;
    private static FanService testFanService;
    private static UserService testUserService;
    private static Fan testFan;

    /**
     * for all use cases
     * @throws Exception
     */
    @Before
    public void before() throws Exception {
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
        testFanService = (FanService)system.getUserServices().get(testUser).get(1);
        testUserService = (UserService) system.getUserServices().get(testUser).get(0);
        testFan = (Fan)testUser.getRoles().get(0);
    }

    /**
     * for use case 3.2
     */
    public void before32(){
        Page testPage1  = new Page(null);
        Page testPage2 = new Page(null);
        DataComp.getInstance().getPageList().add(testPage1);
        DataComp.getInstance().getPageList().add(testPage2);
    }

    /**
     * for use case 3.5
     */
    @Test
    public void before35(){
        Team testTeam1 = new Team("Hapoel Beer-Sheva","Terner",null);
        Team testTeam2 = new Team("Maccabi Beer-Sheva","Mekif Z",null);
        DataComp.getInstance().getTeamList().add(testTeam1);
        DataComp.getInstance().getTeamList().add(testTeam2);
        try {
            testFanService.searchInformation(Criteria.Category,"teams");
            testFanService.searchInformation(Criteria.KeyWord,"Lior");
            testFanService.searchInformation(Criteria.KeyWord,"Eitan");
            testFanService.searchInformation(Criteria.Name,"Lior");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * id: A@34
     * acceptance test 3.1.1 - use case 3.1
     * checks successful log out of system
     */
    @Test
    public void successfulLogOut(){
        try {
            testUserService.logOut();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(system.getUserList().size() == 2);
        assertFalse(system.getUserServices().containsKey(testUser));
    }

    /**
     * id: A@35
     * acceptance test 3.2.1 - use case 3.2
     * checks successful adding pages to follow
     */
    @Test
    public void addPagesTest(){
        before32();
        List<Page>pages = new ArrayList<>();
        pages.add(DataComp.getInstance().getPageList().get(0));
        pages.add(DataComp.getInstance().getPageList().get(1));
        try {
            testFanService.addPages(pages);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(testFan.getPages().size() == 2);
    }

    /**
     * id: A@36
     * acceptance test 3.4.1 - use case 3.4
     * checks successful adding complaint
     */
    @Test
    public void successfulAddComplaintTest(){
        try {
            testFanService.report("basa");
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(DataComp.getInstance().getComplaint().containsKey(testUser));
        assertTrue(DataComp.getInstance().getComplaint().get(testUser).get(0).getDescription().equals("basa"));
    }

    /**
     * id: A@37
     * acceptance test 3.4.2 - use case 3.4
     * checks successful adding complaint
     */
    @Test
    public void unsuccessfulAddComplaintTest(){
        try {
            testFanService.report(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertFalse(DataComp.getInstance().getComplaint().containsKey(testUser));
    }

    /**
     * id: A@38
     * acceptance test 3.5.1 - use case 3.5
     * checks successful retrieve history
     */
    @Test
    public void retrieveHistorySearch(){
        before35();
        List<String>keyWordSearchHistory = new ArrayList<>();
        try {
           keyWordSearchHistory = testFanService.retrieveHistory(Criteria.KeyWord);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(keyWordSearchHistory.size() == 2);
        assertTrue(keyWordSearchHistory.get(0).equals("Lior"));
        assertTrue(keyWordSearchHistory.get(1).equals("Eitan"));
        List<String>NameSearchHistory = new ArrayList<>();
        try {
            NameSearchHistory = testFanService.retrieveHistory(Criteria.Name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(NameSearchHistory.size() == 1);
        assertTrue(NameSearchHistory.get(0).equals("Lior"));
        List<String>CategorySearchHistory = new ArrayList<>();
        try {
            CategorySearchHistory = testFanService.retrieveHistory(Criteria.Category);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(CategorySearchHistory.size() == 1);
        assertTrue(CategorySearchHistory.get(0).equals("teams"));
    }

    /**
     * id: A@39
     * acceptance test 3.6.1 - use case 3.6
     * checks show personal information
     */
    @Test
    public void showPersonalInformationTest(){
        List<String>personalInfo = null;
        try {
            personalInfo = testUserService.showPersonalInformation();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(personalInfo.size() == 3);
        assertTrue(personalInfo.get(0).equals("Lior"));
        assertTrue(personalInfo.get(1).equals("Eitan"));
        assertTrue(personalInfo.get(2).equals("Lior@gmail.com"));
    }

    /**
     * id: A@40
     * acceptance test 3.7.1 - use case 3.7
     * checks edit personal information
     */
    @Test
    public void editPersonalInformationTest(){
        boolean updated = false;
        try {
            updated = testUserService.editPersonalInformation("Eitan","Platok","f@f.com");
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(updated);
        assertTrue(testUser.getFirstName().equals("Eitan"));
        assertTrue(testUser.getLastName().equals("Platok"));
        assertTrue(testUser.getEmail().equals("f@f.com"));
    }

    /**
     * id: A@41
     * acceptance test 3.7.2 - use case 3.7
     * checks edit personal information with invalid arguments
     */
    @Test
    public void unSuccessfulEditPersonalInformationTest(){
        //wrong email form
        boolean updated = false;
        try {
            updated = testUserService.editPersonalInformation("Eitan","Platok","f@.com");
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertFalse(updated);
        assertTrue(testUser.getFirstName().equals("Lior"));
        assertTrue(testUser.getLastName().equals("Eitan"));
        assertTrue(testUser.getEmail().equals("Lior@gmail.com"));
        //null as argument
        try {
            updated = testUserService.editPersonalInformation(null,"Platok","f@.com");
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertFalse(updated);
        assertTrue(testUser.getFirstName().equals("Lior"));
        assertTrue(testUser.getLastName().equals("Eitan"));
        assertTrue(testUser.getEmail().equals("Lior@gmail.com"));
    }
}
