package com.football_system.football_system.FMserver.UnitTests;

import com.football_system.football_system.FMserver.LogicLayer.*;
import com.football_system.football_system.FMserver.ServiceLayer.*;
import com.football_system.football_system.FMserver.DataLayer.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.*;

import static org.junit.Assert.*;

public class ControllerTest {
    // Logger
    private static final Logger testLogger = Logger.getLogger(ControllerTest.class);
    // System
    IController systemTester = new Controller();
    // Test Components
    User validUser = new User("mail@gamil.com", "12345678", "test");
    Guest validGuest = new Guest();

    /**
     *  initialization tests
     */
    @BeforeClass
    public static void init(){
        // Initialization of the Logger
        String propertiesPath = "log4j.properties";
        PropertyConfigurator.configure(propertiesPath);
        testLogger.info("System Tests : Started");
    }

    /**
     *  reset IController before tests
     */
    @Before
    public void setUp(){
        systemTester = new Controller();
    }

    /**
     * id: U@63
     *  checks add user func
     */
    @Test
    public void addUserTest() {
        assertTrue(systemTester.addUser(validUser));
        testLogger.info("System Tests : add user - test 1 passed");
        assertFalse(systemTester.addUser(null));
        testLogger.info("System Tests : add user - test 2 passed");
    }

    /**
     * id: U@64
     *  checks remove user func
     */
    @Test
    public void removeUserTest() {
        systemTester.addUser(validUser);
        assertEquals(3, systemTester.getUserList().size());
        testLogger.info("System Tests : remove user - test 1 passed");
        systemTester.removeUser(validUser);
        assertEquals(2, systemTester.getUserList().size());
        testLogger.info("System Tests : remove user - test 2 passed");
    }

    /**
     * id: U@65
     *  checks add guest func
     */
    @Test
    public void addGuestTest() {
        systemTester.addGuest(validGuest);
        assertEquals(1, systemTester.getGuestsList().size());
        testLogger.info("System Tests : add guest - test 1 passed");
        systemTester.addGuest(null);
        assertEquals(1, systemTester.getGuestsList().size());
        testLogger.info("System Tests : add guest - test 2 passed");
    }

    /**
     * id: U@66
     *  checks remove guest func
     */
    @Test
    public void removeGuestTest() {
        systemTester.addGuest(validGuest);
        assertEquals(1, systemTester.getGuestsList().size());
        testLogger.info("System Tests : remove guest - test 1 passed");
        systemTester.removeGuest(validGuest);
        assertEquals(0, systemTester.getGuestsList().size());
        testLogger.info("System Tests : remove guest - test 2 passed");
    }

    /**
     * id: U@67
     *  checks remove user services func
     */
    @Test
    public void removeUserServiceTest() {
        systemTester.addUser(validUser);
        assertEquals(1, systemTester.getUserServices().getOrDefault(validUser,null).size());
        testLogger.info("System Tests : remove guest service - test 1 passed");
        systemTester.removeUserService(validUser);
        assertNull(systemTester.getUserServices().getOrDefault(validUser, null));
        testLogger.info("System Tests : remove guest service - test 2 passed");


    }

    /**
     * id: U@68
     *  checks create fan service func
     */
    @Test
    public void createFanServiceForUserTest() {
        systemTester.addUser(validUser);
        assertNotEquals(2, systemTester.getUserServices().get(validUser).size());
        testLogger.info("System Tests : create fan service - test 1 passed");
        assertEquals(1, systemTester.getUserServices().get(validUser).size());
        testLogger.info("System Tests : create fan service - test 2 passed");
        systemTester.createFanServiceForUser(validUser, new Fan(validUser, validUser.getUserName()));
        assertNotEquals(1, systemTester.getUserServices().get(validUser).size());
        testLogger.info("System Tests : create fan service - test 3 passed");
        assertEquals(2, systemTester.getUserServices().get(validUser).size());
        testLogger.info("System Tests : create fan service - test 4 passed");
    }

    /**
     * id: U@69
     * checks add services func
     */
    @Test
    public void addServicesToUserTest() {
        systemTester.addUser(validUser);
        validUser.addRole(new Player());
        validUser.addRole(new Referee(validUser, "qualification", validUser.getUserName()));
        validUser.addRole(new Owner(validUser, validUser.getUserName()));
        assertEquals(1, systemTester.getUserServices().getOrDefault(validUser,null).size());
        testLogger.info("System Tests : add services - test 1 passed");
        systemTester.addServicesToUser(validUser);
        assertEquals(4, systemTester.getUserServices().getOrDefault(validUser,null).size());
        testLogger.info("System Tests : add services - test 2 passed");
    }

    /**
     *
     * closing tests
     */
    @AfterClass
    public static void afterClass(){
        testLogger.info("System Tests : Ended");
    }
}
