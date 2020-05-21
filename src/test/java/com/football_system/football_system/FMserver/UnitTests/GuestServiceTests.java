package com.football_system.football_system.FMserver.UnitTests;

import com.football_system.football_system.FMserver.LogicLayer.*;
import com.football_system.football_system.FMserver.ServiceLayer.*;
import com.football_system.football_system.FMserver.DataLayer.*;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class GuestServiceTests {
    private static final Logger testLogger = Logger.getLogger(GuestTests.class);
    private static GuestService guestService;
    private static Guest guest;
    private static IController system;

    @Before
    public void setUp() throws Exception {
        DataComp.setDataManager(new DataManager());
    }



    @BeforeClass
    public static void init(){
        system = new Controller();
        guest = new Guest();
        guestService = new GuestService(guest,system);
        String propertiesPath = "log4j.properties";
        PropertyConfigurator.configure(propertiesPath);
    }

    /**
     * id: U@57
     * checks Authentication func
     */
    @Test
    public void AuthenticationTest(){
        testLogger.info("Run: AuthenticationTest");
        String password = "";
        //check password contains unSupported letters
        password = "/-789";
        assertFalse(guestService.Authentication(password));
        //checks password with size less than 8
        password = "1234567";
        assertFalse(guestService.Authentication(password));
        //check legal password
        password = "12345687";
        assertTrue(guestService.Authentication(password));
        testLogger.info("Ended: AuthenticationTest");
    }

    /**
     * id: U@58
     * checks mailAuthentication func
     */
    @Test
    public void mailAuthenticationTest(){
        String email = "";
        //checks legal email form as input
        email = "Eitan@gmail.com";
        assertTrue(guestService.mailAuthentication(email));
        //checks illegal email form as input
        email = "eitangmail.com";
        assertFalse(guestService.mailAuthentication(email));
        //checks null as argument
        email = null;
        assertFalse(guestService.mailAuthentication(email));
    }

    /**
     * id: U@59
     * checks show information by category func
     */
    @Test
    public void showInfoTest() {
        GuestStub guest = new GuestStub();
        guestService = new GuestService(guest, system);
        guestService.showInformationByCategory(Interest.Players);
        assertEquals(guestService.getLastSearchResults().size(),guest.retrievePlayers().size());
        assertNotEquals(guestService.getLastSearchResults().size(), 0);
        assertArrayEquals(guestService.getLastSearchResults().toArray(),guest.retrievePlayers().toArray());
        guestService.showInformationByCategory(Interest.Games);
        assertNotEquals(guestService.getLastSearchResults().size(), 3);
        assertEquals(guestService.getLastSearchResults().size(), 2);
    }

    /**
     * id: U@60
     * checks search information func
     */
    @Test
    public void searchInfoTest() {
        GuestStub guest = new GuestStub();
        guestService = new GuestService(guest, system);
        // Search By Key Word
        guestService.searchInformation(Criteria.KeyWord, "David");
        assertEquals(((User)guestService.getLastSearchResults().get(0)).getFirstName(),"David");
        assertEquals(guestService.getLastSearchResults().size(), 1);
        // Search By Name
        guestService.searchInformation(Criteria.Name, "David");
        assertEquals(((User)guestService.getLastSearchResults().get(0)).getFirstName(),"David");
        assertEquals(guestService.getLastSearchResults().size(), 1);
        // Search By Category
        guestService.searchInformation(Criteria.Category, "plaYErs");
        assertEquals(((Player)guestService.getLastSearchResults().get(0)).getName(),"David");
        assertEquals(guestService.getLastSearchResults().size(), 3);
    }

    /**
     * id: U@61
     * checks filter search results func
     */
    @Test
    public void filterSearchTest() {
        GuestStub guest = new GuestStub();
        guestService = new GuestService(guest, system);
        // Search By Key Word
        guestService.searchInformation(Criteria.KeyWord, "playERS");
        guestService.filterResults(Filter.Team, "FCB");
    }
}
