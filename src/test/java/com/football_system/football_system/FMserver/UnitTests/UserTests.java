package com.football_system.football_system.FMserver.UnitTests;

import com.football_system.football_system.FMserver.LogicLayer.*;
import com.football_system.football_system.FMserver.ServiceLayer.*;
import com.football_system.football_system.FMserver.DataLayer.*;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.List;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserTests {
    private static final Logger testLogger = Logger.getLogger(UserTests.class);
    private static User user;

    @BeforeClass
    public static void init(){
        String propertiesPath = "log4j.properties";
        PropertyConfigurator.configure(propertiesPath);
        user = new User("Eitan@gmail.com","1234","Eitan","David");
    }

    /**
     * id:U@40
     * checks getPersonalDetails func
     */
    @Test
    public void checkGetPersonalDetails(){
        //tests personal details getter
        testLogger.info("Run: checkGetPersonalDetails");
        List<String>personalDetail = user.getPersonalDetails();
        assertTrue(personalDetail.get(0).equals("Eitan"));
        assertTrue(personalDetail.get(1).equals("David"));
        assertTrue(personalDetail.get(2).equals("Eitan@gmail.com"));
        testLogger.info("Ended: checkGetPersonalDetails");
    }

    /**
     * id:U@41
     * checks updatePersonalDetails func
     */
    @Test
    public void updatePersonalDetailCheck(){
        //check if details updated by entering legal values
        testLogger.info("Run: updatePersonalDetailCheck");
        String firstName = "David";
        String lastName = "Eitan";
        String email = "David@gmail.com";
        user.updatePersonalInformation(firstName,lastName, email);
        assertTrue(user.getFirstName().equals("David"));
        assertTrue(user.getLastName().equals("Eitan"));
        assertTrue(user.getEmail().equals("David@gmail.com"));
        //check if details updated with null argument
        firstName = null;
        user.updatePersonalInformation(firstName,lastName, email);
        assertTrue(user.getFirstName().equals("David"));
        //checks if details changed with illegal mail
        assertFalse(user.updatePersonalInformation("Eitan","David","Eitan"));
        testLogger.info("Ended: updatePersonalDetailCheck");

    }

    /**
     * id:U@42
     * checks equals func
     */
    @Test
    public void equalTest(){
        //checks equals between the same users
       User testUser =  new User("Eitan@gmail.com","1234","Eitan","David");
       assertTrue(user.equals(testUser));
        //checks equals with different email
       testUser = new User("Eitab@gmail.com","1234","Eitan","David");
       assertFalse(user.equals(testUser));
        //checks equals with different password
        testUser = new User("Eitab@gmail.com","123","Eitan","David");
        assertFalse(user.equals(testUser));
        //checks equals with different firstName
        testUser = new User("Eitan@gmail.com","123","Eita","David");
        assertFalse(user.equals(testUser));
        testUser = null;
        //checks equals while testUser is null
        assertFalse(user.equals(testUser));
    }

    /**
     * id:U@43
     * checks addRole func
     */
    @Test
    public void addRoleTest(){
        Fan fan = new Fan(user,"Eitan");
        //checks adding role while user doesn't contains any roles
        assertTrue(user.addRole(fan));
        //checks adding role while user has this role
        assertFalse(user.addRole(fan));
        //trying adding null as role to user
        fan = null;
        assertFalse(user.addRole(fan));
    }

    /**id:U@44
     * checks RemoveRole func
     */
    @Test
    public void removeRoleTest(){
        Fan fan = new Fan(user,"Eitan");
        user.addRole(fan);
        //checks remove of legal role
        assertTrue(user.removeRole(fan));
        //checks remove of role that user doesn't has
        assertFalse(user.removeRole(fan));
        fan = null;
        //checks trying ti remove null
        assertFalse(user.removeRole(fan));
    }

    /**
     * id:U@45
     * checks ifUserRoleIncludeReferee func
     */
    @Test
    public void ifUserRoleIncludeRefereeTest(){
        Referee ref = new Referee(user, null, null , null);
        user.addRole(ref);
        //checks ifUserRoleIncludeReferee func while user contains referee role
        assertTrue(user.ifUserRoleIncludeReferee() != null);
        user.removeRole(ref);
        //checks ifUserRoleIncludeReferee func while user doesn't contain referee role
        assertTrue(user.ifUserRoleIncludeReferee() == null);
    }


}
