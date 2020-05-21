package com.football_system.football_system.FMserver.UnitTests;

import com.football_system.football_system.FMserver.LogicLayer.*;
import com.football_system.football_system.FMserver.ServiceLayer.*;
import com.football_system.football_system.FMserver.DataLayer.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CoachTests {
    private static final Logger testLogger = Logger.getLogger(GuestTests.class);
    private static Coach coach;
    private static User user;
    private static Page page;

    @BeforeClass
    public static void init(){
        user = new User("Eitan@gmail.com","1234","Eitan","David");
        coach = new Coach(user,user.getFirstName(),null,null,null,null);
        String propertiesPath = "log4j.properties";
        PropertyConfigurator.configure(propertiesPath);
        page = new Page(coach);
        coach.setPage(page);
    }

    /**
     * id: U@56
     * test addUpdateToPage func
     */
    @Test
    public void addUpdateTest(){
        testLogger.info("Run: addUpdateTest");
        coach.addUpdateToPage("Eitan");
        assertTrue(page.getUpdates().size() == 1);
        assertTrue(page.getUpdates().get(0).equals("Eitan"));
        testLogger.info("Ended: addUpdateTest");
    }







}
