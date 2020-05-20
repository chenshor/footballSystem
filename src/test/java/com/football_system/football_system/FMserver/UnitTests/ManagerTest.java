package com.football_system.football_system.FMserver.UnitTests;

import com.football_system.football_system.FMserver.LogicLayer.*;
import com.football_system.football_system.FMserver.ServiceLayer.*;
import com.football_system.football_system.FMserver.DataLayer.*;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * tests uc 6.4 in the in the aspect of manager permissions.
 * testing the exceptions thrown either.
 * assumes that in the GUI the manager selects a player/coach to delete and the Player/Coach are given already
 **/
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ManagerTest {

    Controller controller = new Controller();

    User ownerUser = new User("alonalas@post.bgu.ac.il","123","alona");
    User u1 = new User("a@b@c","1234","alonalas");
    User u2 = new User("amir@post", "234","amirLasry");
    User u3 = new User("abc", "aaa","haermi");
    User u4 = new User("Hogwarts.com","12345678","Albus Dumbeldore");

    Owner own = new Owner(ownerUser,"Alona the queen");
    Page p = new Page(null);
    Team team = new Team("Blumfield", "Hapoel",p);

    @Before
    public void initializeSystem() {
        DataComp.setDataManager(new DataManager());
        controller.addUser(ownerUser);
        DataComp.getInstance().addUser(ownerUser);
        DataComp.getInstance().addUser(u4);
        DataComp.getInstance().addUser(u3);
        DataComp.getInstance().addUser(u1);
        DataComp.getInstance().addUser(u2);
        ownerUser.setRole(own);
        own.addTeam(team);
        team.addOwner(own);

    }

    @Before
    public void reset() {
        DataComp.setDataManager(new DataManager());
    }

    @Test
    /**
     * U@34
     * tesets manager player addition permission. if the manager is not permitted to add a player, an exception is thrown
     */
    public void A_testManagerInsertPlayer_U() {

        Map<Manager.Permission,Boolean> permissionBooleanMap = new HashMap<>();
        permissionBooleanMap.put(Manager.Permission.playerAddition,true);

        Manager m;

        try {
            own.insertNewManager(team.getName(),"Rupert",u2.getUserName(),u2.getEmail(),permissionBooleanMap);
            m = team.getManager(u2);
            m.insertNewPlayer(team.getName(),"chen","harta",1,2,1990,u1.getUserName(),u1.getEmail());
            assertEquals(team.getRoleHolders().size(),2);
            assertTrue(team.getPlayerList().contains(u1.getRoles().get(0)));
        }
        catch (IOException e) {
            //System.out.println(e.getMessage());
        }
        permissionBooleanMap.replace(Manager.Permission.playerAddition,false);

        try {
            m = team.getManager(u2);
            m.insertNewPlayer(team.getName(),"Hermione","dumbledore's army",1,2,1990,u3.getUserName(),u3.getEmail());
        }
        catch (IOException e) {
            //System.out.println(e.getMessage());
            assertEquals(team.getRoleHolders().size(),2);
            assertEquals(e.getMessage(),"This manager is not permitted to nominate a new player to the team");
        }
    }

    @Test
    /**
     * U@35
     * tesets manager player deletion permission. if the manager is not permitted to delete a player, an exception is thrown
     */
    public void B_testManagerDeletePlayer_U() {

        Map<Manager.Permission,Boolean> permissionBooleanMap = new HashMap<>();
        permissionBooleanMap.put(Manager.Permission.playerDeletion,true);

        Manager m;

        try {
            own.insertNewManager(team.getName(),"Rupert",u2.getUserName(),u2.getEmail(),permissionBooleanMap);
            m = team.getManager(u2);
            own.insertNewPlayer(team.getName(),"chen","harta",1,2,1990,u1.getUserName(),u1.getEmail());
            m.deletePlayer(team.getName(),u1.getUserName(),u1.getEmail());
        }
        catch (IOException e) {
            //System.out.println(e.getMessage());
        }

        assertEquals(team.getPlayerList().size(),0);
        assertEquals(u1.getRoles().size(),0);

        permissionBooleanMap.replace(Manager.Permission.playerDeletion,false);

        try {
            m = team.getManager(u2);
            own.insertNewPlayer(team.getName(),"Hermione","dumbledore's army",1,2,1990,u3.getUserName(),u3.getEmail());
            m.deletePlayer(team.getName(),u3.getUserName(),u3.getEmail());
        }
        catch (IOException e) {
            //System.out.println(e.getMessage());
            assertEquals(team.getRoleHolders().size(),2); //m & player
            assertEquals(e.getMessage(),"This manager is not permitted to delete a player from the team");
        }
    }

    @Test
    /**
     * U@36
     * tesets manager coach addition permission. if the manager is not permitted to add a coach, an exception is thrown
     */
    public void C_testManagerInsertCoach_U() {

        Map<Manager.Permission,Boolean> permissionBooleanMap = new HashMap<>();
        permissionBooleanMap.put(Manager.Permission.coachAddition,true);

        Manager m;

        try {
            own.insertNewManager(team.getName(),"Rupert",u2.getUserName(),u2.getEmail(),permissionBooleanMap);
            m = team.getManager(u2);
            m.insertNewCoach(team.getName(),"chen","harta","barta",u3.getUserName(),u3.getEmail());
            assertEquals(team.getRoleHolders().size(),2);
            assertTrue(team.getCoachList().contains(u3.getRoles().get(0)));
        }
        catch (IOException e) {
            //System.out.println(e.getMessage());
        }
        permissionBooleanMap.replace(Manager.Permission.coachAddition,false);

        try {
            m = team.getManager(u2);
            m.insertNewCoach(team.getName(),"Hermione","dumbledore's army","i dont know",u4.getUserName(),u4.getEmail());
        }
        catch (IOException e) {
            //System.out.println(e.getMessage());
            assertEquals(team.getRoleHolders().size(),2);
            assertEquals(e.getMessage(),"This manager is not permitted to nominate a new coach to the team");
        }
    }

    @Test
    /**
     * U@37
     * tesets manager coach deletion permission. if the manager is not permitted to delete a coach, an exception is thrown
     */
    public void D_testManagerDeleteCoach_U() {

        Map<Manager.Permission,Boolean> permissionBooleanMap = new HashMap<>();
        permissionBooleanMap.put(Manager.Permission.coachDeletion,true);

        Manager m;
        String message="";

        try {
            own.insertNewManager(team.getName(),"Rupert",u2.getUserName(),u2.getEmail(),permissionBooleanMap);
            m = team.getManager(u2);
            own.insertNewCoach(team.getName(),"chen","harta","barta",u1.getUserName(),u1.getEmail());
            own.insertNewCoach(team.getName(),"mollyWisley","mom","of ron",u4.getUserName(),u4.getEmail());
            m.deleteCoach(team.getName(),u1.getUserName(),u1.getEmail());
            m.deleteCoach(team.getName(),u4.getUserName(),u4.getEmail());
        }
        catch (IOException e) {
            //System.out.println(e.getMessage());
            message = e.getMessage();
        }

        assertEquals(message,"Cannot remove the last coach of the team");
        assertEquals(team.getCoachList().size(),1);
        assertEquals(u1.getRoles().size(),0);

        permissionBooleanMap.replace(Manager.Permission.coachDeletion,false);

        try {
            m = team.getManager(u2);
            own.insertNewCoach(team.getName(),"Hermione","dumbledore's army","idk",u1.getUserName(),u1.getEmail());
            m.deleteCoach(team.getName(),u1.getUserName(),u1.getEmail());
        }
        catch (IOException e) {
            //System.out.println(e.getMessage());
            assertEquals(team.getRoleHolders().size(),3); //m & 2 coaches
            assertEquals(e.getMessage(),"This manager is not permitted to delete a coach from the team");
            assertEquals(u1.getRoles().size(), 1);
        }
    }
}
