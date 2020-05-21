package com.football_system.football_system.FMserver.IntegrationTests;
import com.football_system.football_system.FMserver.LogicLayer.*;
import com.football_system.football_system.FMserver.ServiceLayer.*;
import com.football_system.football_system.FMserver.DataLayer.*;

import com.football_system.football_system.FMserver.UnitTests.GuestTests;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertTrue;

public class FanIntegrationTests {
    private static final Logger testLogger = Logger.getLogger(GuestTests.class);
    private static Fan fan;
    private static User user;
    private static FanService fanService;
    private static IController system;


    @BeforeClass
    public static void init(){
        user = new User("Eitan@gmail.com","1234","Eitan","David");
        fan = new Fan(user,user.getFirstName());
        String propertiesPath = "log4j.properties";
        PropertyConfigurator.configure(propertiesPath);
        system = new Controller();
        fanService = new FanService(fan,system);
    }

    @Before
    public void setUp() throws Exception {
        DataComp.setDataManager(new DataManager());
    }

    /**
     * id: I@35
     * checks retrieveHistory func
     */
    @Test
    public void retrieveHistoryTest(){
        //tests if correct search history returns
        List<String>categorySearches = new ArrayList<>();
        categorySearches.add("coaches");
        categorySearches.add("players");
        List<String>nameSearches = new ArrayList<>();
        nameSearches.add("David");
        nameSearches.add("Eitan");
        List<String>keyWordSearches = new ArrayList<>();
        keyWordSearches.add("Platok");
        keyWordSearches.add("Fadida");
        DataComp.getInstance().getFanSearchCategoryHistory().put(fan,categorySearches);
        DataComp.getInstance().getFanSearchNameHistory().put(fan,nameSearches);
        DataComp.getInstance().getFanSearchKeyWordHistory().put(fan,keyWordSearches);
        try {
            assertTrue(fanService.retrieveHistory(Criteria.Category).size() == 2);
            assertTrue(fanService.retrieveHistory(Criteria.Category).get(0).equals("coaches"));
            assertTrue(fanService.retrieveHistory(Criteria.Category).get(1).equals("players"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assertTrue(fanService.retrieveHistory(Criteria.Name).size() == 2);
            assertTrue(fanService.retrieveHistory(Criteria.Name).get(0).equals("David"));
            assertTrue(fanService.retrieveHistory(Criteria.Name).get(1).equals("Eitan"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assertTrue(fanService.retrieveHistory(Criteria.KeyWord).size() == 2);
            assertTrue(fanService.retrieveHistory(Criteria.KeyWord).get(0).equals("Platok"));
            assertTrue(fanService.retrieveHistory(Criteria.KeyWord).get(1).equals("Fadida"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //checks if returned null if there is no search history
        DataComp.getInstance().getFanSearchCategoryHistory().remove(fan);
        try {
            assertTrue(fanService.retrieveHistory(Criteria.Category) == null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * id: I@36
     * checks report func
     */
    @Test
    public void reportTest(){
        //checks if new complaint have been added with valid arguments
        String description = "yalla";
        try {
            fanService.report(description);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(DataComp.getInstance().getComplaint().get(fan.getUser()).size() == 1);
        assertTrue(DataComp.getInstance().getComplaint().get(fan.getUser()).get(0).getDescription().equals("yalla"));
        //checks if null as argument will be added
        try {
            fanService.report(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(DataComp.getInstance().getComplaint().get(fan.getUser()).size() == 1);
    }

    /**
     * id: I@37
     * checks addPages func
     */
    @Test
    public void addPagesTest(){
        //checks func with legal argument
        List<Page>testPages = new ArrayList<>();
        Player player = new Player(user,null,null,null,null,null);
        Page testPage1 = new Page(player);
        testPages.add(testPage1);
        try {
            fanService.addPages(testPages);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(fan.getPages().size() == 1);
        //check func with null as argument
        try {
            fanService.addPages(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(fan.getPages().size() == 1);
    }


}
