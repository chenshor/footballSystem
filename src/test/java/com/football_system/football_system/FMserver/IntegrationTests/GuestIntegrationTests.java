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

import static org.junit.Assert.*;

public class GuestIntegrationTests {
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
     * id: I@33
     * checks register func
     */
    @Test
    public void registerTest(){
        testLogger.info("Run: registerTest");
        //checks register while user with the same email that exists
        User user = new User("Eitan@gmail.com","1234","Eitan","David");
        DataComp.getInstance().addNewUser(user);
        String firstName = "Eitan";
        String lastName = "David";
        String Email = "Eitan@gmail.com";
        String password = "12345687";
        assertNull(guestService.register(firstName,lastName,Email,password));
        //checks register func with illegal password
        password = "1234";
        Email = "walla@walla.com";
        assertNull(guestService.register(firstName,lastName,Email,password));
        //checks register func with legal arguments
        password = "13245678";
        assertNotNull(guestService.register(firstName,lastName,Email,password));
        assertTrue(system.getUserList().size() == 3);
        user.setPassword("13245678");
        user.setFirstName("Eitan");
        user.setEmail("walla@walla.com");
        //checks if user added to controller
        assertTrue(system.getUserList().get(2).equals(user));
        //checks if userService and fanService added to user
        assertTrue(system.getUserServices().get(user).size() == 2);
        assertTrue(system.getUserServices().get(user).get(0) instanceof UserService);
        assertTrue(system.getUserServices().get(user).get(1) instanceof FanService);
        testLogger.info("Ended: registerTest");
    }

    /**
     * id: I@34
     * checks log in func
     */
    @Test
    public void logInTest(){
        testLogger.info("Run: logInTest");
        //check log in with illegal password
        User user = new User("Eitan@gmail.com","12345678","Eitan","David");
        DataComp.getInstance().addNewUser(user);
        assertFalse(guestService.logIn("Eitan@gmail.com","123456"));
        //check log in with incorrect email
        assertFalse(guestService.logIn("Eitab@gmail.com","12345678"));
        //check legal arguments log in
        assertTrue(guestService.logIn("Eitan@gmail.com","12345678"));
        //check log in of user with multiple roles
        User newUser = new User("Eitab@gmail.com","12345678","David","David");
        Player player = new Player(user,null,null,null,null);
        newUser.addRole(player);
        Owner owner = new Owner(user, null);
        newUser.addRole(owner);
        DataComp.getInstance().addNewUser(newUser);
        assertTrue(guestService.logIn("Eitab@gmail.com","12345678"));
        assertTrue(system.getUserServices().get(newUser).size() == 3);
        assertTrue(system.getUserServices().get(newUser).get(1) instanceof PlayerService);
        assertTrue(system.getUserServices().get(newUser).get(2) instanceof OwnerService);
        testLogger.info("Ended: logInTest");
    }
}
