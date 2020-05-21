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

public class GuestTests {
    private static final Logger testLogger = Logger.getLogger(GuestTests.class);
    private static Guest guest;

    @BeforeClass
    public static void init(){
        guest = new Guest();
        String propertiesPath = "log4j.properties";
        PropertyConfigurator.configure(propertiesPath);
    }

    @Before
    public void setUp() throws Exception {
        DataComp.setDataManager(new DataManager());
    }

    /**
     * id:U@46
     * checks create new user func
     */
    @Test
    public void checkCreatingNewUser(){
        testLogger.info("Run: checkCreatingNewUser");
        User toCheck = guest.createNewUser("Eitan@gmail.com","1234","Eitan","David");
        //checks user details
        assertEquals(toCheck.getFirstName(),"Eitan");
        assertEquals(toCheck.getLastName(),"David");
        assertEquals(toCheck.getEmail(),"Eitan@gmail.com");
        assertEquals(toCheck.getPassword(),"1234");
        //checks if Role is added
        assertTrue(toCheck.getRoles().get(0) instanceof Fan);
        testLogger.info("Ended: checkCreatingNewUser");
    }

    /**
     * id:U@47
     * checks signIn func
     */
    @Test
    public void checkSignIn(){
        testLogger.info("Run: checkSignIn");
        //checks if right User returned
        User user = guest.createNewUser("Eitan@gmail.com","1234","Eitan","David");
        DataComp.getInstance().getUserList().add(user);
        User userToCheck = guest.signIn("Eitan@gmail.com","1234");
        assertTrue(userToCheck.getEmail().equals(user.getEmail()));
        //checks if wrong email entered
        userToCheck = guest.signIn("Eitab@gmail.com","1234");
        assertNull(userToCheck);
        //checks if wrong password entered
        userToCheck = guest.signIn("Eitan@gmail.com","1235");
        assertNull(userToCheck);
        testLogger.info("Ended: checkSignIn");
    }

    /**
     * id:U@48
     * checks addNewUser func
     */
    @Test
    public void addUserTest(){
        testLogger.info("Run: addUserTest");
        //init fields for specific test
        guest = new Guest();
        //check if user is null
        User user = null;
        guest.addNewUser(user,true);
        assertTrue(DataComp.getInstance().getUserList().size() == 0);
        //checks if legal user as argument added
        user = new User("Eitan@gmail.com","1234","Eitan","David");
        guest.addNewUser(user,true);
        assertTrue(DataComp.getInstance().getUserList().size() == 1);
        assertTrue(DataComp.getInstance().getUserList().get(0).getPassword().equals("1234"));
        testLogger.info("Ended: addUserTest");

    }

}
