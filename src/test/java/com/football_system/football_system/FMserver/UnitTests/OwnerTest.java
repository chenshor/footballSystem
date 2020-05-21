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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OwnerTest {

    Controller controller = new Controller();

    User ownerUser = new User("alonalas@post.bgu.ac.il","123","alona");

    User u1 = new User("a@b@c","1234","alonalas");
    User u2 = new User("amir@post", "234","amirLasry");
    User u3 = new User("abc", "aaa","haermi");
    User u4 = new User("Hogwarts.com","12345678","Albus Dumbeldore");

    Owner own = new Owner(ownerUser,"Alona the queen");
    Page p = new Page(null);
    Team team = new Team("Hapoel", "Blumfield",p);
    Team anotherTeam = new Team("Macabi","Sami offer",new Page(null));

    @Before
    public void initializeSystem() {

        controller.addUser(ownerUser);
        DataComp.getInstance().addUser(ownerUser);
        DataComp.getInstance().addUser(u4);
        DataComp.getInstance().addUser(u3);
        DataComp.getInstance().addUser(u1);
        DataComp.getInstance().addUser(u2);
        ownerUser.setRole(own);
        own.addTeam(team);
        team.addOwner(own);

        own.addTeam(anotherTeam);
        anotherTeam.addOwner(own);

    }

    @Before
    public void reset() {
        DataComp.setDataManager(new DataManager());
    }

    /**
     * U@1
     * the 5 following tests are testing UC 6.1.1
     *THE FOLLOWING TEST is testing also UC 6.4
     *
     */
    @Test
    public void A_testOwnerAddManager_U() {

        Map<Manager.Permission,Boolean> permissionBooleanMap = new HashMap<>();
        permissionBooleanMap.put(Manager.Permission.playerDeletion,true);
        permissionBooleanMap.put(Manager.Permission.coachDeletion, true);
        permissionBooleanMap.put(Manager.Permission.coachAddition, true);
        permissionBooleanMap.put(Manager.Permission.playerAddition, true);

        try {
            own.insertNewManager(team.getName(), "Voldemort", u1.getUserName(),u1.getEmail(), permissionBooleanMap);
        }
        catch (IOException e){
            //System.out.println(e.getMessage());
        }

        try {
            own.insertNewManager(team.getName(),"Yossi",u1.getUserName(),u1.getEmail(),permissionBooleanMap);
        } catch (IOException e) {
            //System.out.println(e.getMessage());
            assertEquals(e.getMessage(),"The selected user is allready nominated as manager in this team");
        }
    }

    @Test
    /**
     * U@2
     */
    public void B_testOwnerAddCoach_U() {

        try {
            own.insertNewCoach(team.getName(),"Dani","university of life","fitting rooms", u2.getUserName(),u2.getEmail());
        }
        catch (IOException e){
            //System.out.println(e.getMessage());
        }
        assertTrue(team.getCoachList().size() > 0);
        assertNotNull(team.getRoleHolder(u2.getUserName(),u2.getEmail()));
    }

    @Test
    /**
     * U@3
     */
    public void C_testOwnerAddPlayer_U() {

        try {
            own.insertNewPlayer(team.getName(),"David","GoalKeeper",1,2,1995,u1.getUserName(),u1.getEmail());
        }
        catch (IOException e){
            //System.out.println(e.getMessage());
        }
        assertTrue(team.getPlayerList().size() > 0);
        assertNotNull(team.getRoleHolder(u1.getUserName(),u1.getEmail()));
    }


    @Test
    /**
     * U@4
     */
    public void D_testOwnerAddStadium_U() {

        try {
            own.insertNewStadium(team.getName(),"Turner");
        }
        catch (IOException e){
            //System.out.println(e.getMessage());
        }
        assertEquals(team.getStadium(),"Turner");
    }

     /**
     * U@5
     * test stadium deletion
     * assumes that the user have chosen the button "stadium"
     */
    @Test
    public void E_testOwnerDeleteStadium_U() {
        try {
            own.deleteStadium(team.getName(),team.getStadium());
        }
        catch (IOException e) {
            //System.out.println(e.getMessage());
        }
        assertEquals(team.getStadium(),"NO_STADIUM");
    }


    @Test
    /**
     * U@6
     * The following test is for UC 6.1.3
     */
    public void F_testOwnerUpdateAsset_U() {

        C_testOwnerAddPlayer_U(); // player Exists in the system
        // u1 = ("a@b@c","1234","alonalas");
        RoleHolder toUpdate = team.getRoleHolder(u1.getUserName(),u1.getEmail());

        Map<String, String> attributes = new HashMap<>();
        String newAttributeName = "Ballerina";
        String attributeName = "position";
        attributes.put(attributeName,newAttributeName);

        try {
            own.updateAssetAttributes(team.getName(), toUpdate, attributes);
        }
        catch (IOException e) {
            //System.out.println(e.getMessage());
        }

        assertEquals(team.getPlayer(u1).getPosition(), newAttributeName);

        attributes.clear();
        attributes.put("harta","barta");

        try {
            own.updateAssetAttributes(team.getName(), toUpdate, attributes);
        }
        catch (IOException e) {
            //System.out.println(e.getMessage());
            assertEquals(e.getMessage(),"Invalid attribute selected: harta");
        }

        B_testOwnerAddCoach_U();
        toUpdate = team.getRoleHolder(u2.getUserName(),u2.getEmail());
        attributes.clear();
        attributes.put("Qualification","Harry Potter expert");

        try {
            own.updateAssetAttributes(team.getName(), toUpdate, attributes);
        }
        catch (IOException e) {
            //System.out.println(e.getMessage());
        }

        assertEquals(team.getCoach(u2).getQualification(), "Harry Potter expert");

    }


    ////////////////////////////////////////////////////////////////uc2

    /**
     * U@7
     * test U.C 6.2
     */
    @Test
    public void G_testOwnerNominateOwner_U() {

        String name = "newOnwerChen";

        try {
            own.nominateNewOwner(u2,team.getName(),name);
        }
        catch (IOException e) {
            //System.out.println(e.getMessage());
        }

        assertNotNull(team.getOwner(u2));
        assertTrue(u2.getRoles().size() == 1);
        assertTrue(team.getOwnerList().size() == 2);

        try {
            own.nominateNewOwner(u2,team.getName(),name);
        }
        catch (IOException e) {
            //System.out.println(e.getMessage());
            assertEquals(e.getMessage(),"User is allready nominated as owner in this team");
        }

    }

    ///////////////////////////////////////////// uc3

    /**
     * U@8
     * Test UC 6.3
     */
    @Test
    public void H_testRemoveOwnership_U() {

        G_testOwnerNominateOwner_U(); // the owner "own" nominated the user u2 to be a new owner of Team team
        int roles = 0;
        try {
            own.insertNewPlayer(team.getName(),"harry","seeker",1,2,1987,u2.getUserName(),u2.getEmail());
            roles = u2.getRoles().size();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Role role : u2.getRoles()) {
            if (role instanceof Owner) {
                Owner nominatedOwner = (Owner)role;
                try {
                    own.removeOwnership(nominatedOwner, team.getName());
                } catch (IOException e) {
                    //System.out.println(e.getMessage());
                }
            }
        }
        assertEquals(team.getOwnerList().size() , 1);
        assertNotEquals(roles,u2.getRoles().size());
    }

    @Test
    /**
     * U@9
     * test UC 6.5
     */
    public void I_testRemoveManagerNomination_U() {

        A_testOwnerAddManager_U();
        G_testOwnerNominateOwner_U(); // u1
        Map<Manager.Permission,Boolean> permissionBooleanMap = new HashMap<>();
        try {
            own.insertNewManager(team.getName(),"Prof. snape",u3.getUserName(),u3.getEmail(),permissionBooleanMap);
        } catch (IOException e) {
            //System.out.println(e.getMessage());
        }
        //now there are 2 managers to team
        Owner anotherOwner = team.getOwner(u2);
        try {
            anotherOwner.deleteManager(team.getName(),u3.getUserName(),u3.getEmail());
        } catch (IOException e) {
            //System.out.println(e.getMessage());
            assertEquals(e.getMessage(),"The selected manager was not nominated by you");
        }
        try {
            own.deleteManager(team.getName(),u3.getUserName(),u3.getEmail());
        } catch (IOException e) {
            //System.out.println(e.getMessage());
        }
        assertEquals(team.getManagerList().size(),1);
    }

    @Test
    /**
     * U@10
     * The 2 following tests are testing UC6.6.1 and 6.6.2
     */
    public void J_closeTeamActivity_U(){

        //after this there are 2 roleholders in the team
        G_testOwnerNominateOwner_U();
        A_testOwnerAddManager_U();

        try {
            own.closeTeamActivity(team);
        }
        catch (IOException e) {
            //System.out.println(e.getMessage());
        }

        assertEquals(DataComp.getInstance().getAlerts().get(u2).size(), 1);
        assertEquals(DataComp.getInstance().getAlerts().get(u1).size(),1);
        assertEquals(DataComp.getInstance().getAlerts().get(u1).get(0).getDescription(), "The team: " + team.getName() + " is closed temporarily");
    }

    @Test
    /**
     * U@30
     */
    public void K_renewTeamActivity_U(){

        J_closeTeamActivity_U();

        try {
            own.openTeamActivity(team);
        }
        catch (IOException e) {
            //System.out.println(e.getMessage());
        }
        assertEquals(DataComp.getInstance().getAlerts().get(u2).size(), 2);
        assertEquals(DataComp.getInstance().getAlerts().get(u1).size(),2);
        assertEquals(DataComp.getInstance().getAlerts().get(u1).get(1).getDescription(), "The team: " + team.getName() + " is open");
    }

    @Test
    /**
     * U@31
     * The following 2 tests are testing UC 6.1.2
     */
    public void L_testDeletePlayer_U() {

        C_testOwnerAddPlayer_U();
        List<Role> roles = new LinkedList<>(u1.getRoles());
        try {
            own.deletePlayer(team.getName(),u1.getUserName(),u1.getEmail());
        } catch (IOException e) {
            //System.out.println(e.getMessage());
        }
        assertEquals(team.getPlayerList().size(),0);
        assertEquals(u1.getRoles().size(),0);
        assertNotEquals(roles,u1.getRoles());

        boolean thrown = false;
        String message = "";
        try {
            own.deletePlayer(team.getName(),u2.getUserName(),u2.getEmail());
        } catch (IOException e) {
            //System.out.println(e.getMessage());
            thrown = true;
            message=e.getMessage();
        }
        assertTrue(thrown);
        assertEquals(message,"The selected user is not a player in the selected team");

        try {
            own.insertNewPlayer(anotherTeam.getName(),"bellatrix","death eater",1,2,2010,u3.getUserName(),u3.getEmail());
        } catch (IOException e) {

        }
        thrown= false;
        try {
            own.deletePlayer(team.getName(),u3.getUserName(),u3.getEmail());
        } catch (IOException e) {
            //System.out.println(e.getMessage());
            thrown = true;
            message=e.getMessage();
        }
        assertTrue(thrown);
        assertEquals(message,"The selected user is not a player in the selected team");
    }

    @Test
    /**
     * U@33
     */
    public void M_testDeleteCoach_U(){

        try {
            own.insertNewCoach(team.getName(),"Narcissa","death eater","kill harry",u1.getUserName(),u1.getEmail());
            own.insertNewCoach(team.getName(),"McGonagall","Transformation lessons","teacher",u2.getUserName(),u2.getEmail());
        } catch (IOException e) {
            //System.out.println(e.getMessage());
        }
        List<Role> roles = new LinkedList<>(u1.getRoles());
        try {
            own.deleteCoach(team.getName(),u1.getUserName(),u1.getEmail());
        } catch (IOException e) {
            //System.out.println(e.getMessage());
        }
        assertEquals(team.getCoachList().size(),1);
        assertEquals(u1.getRoles().size(),0);
        assertNotEquals(roles,u1.getRoles());

        boolean thrown = false;
        String message = "";
        try {
            own.deleteCoach(team.getName(),u2.getUserName(),u2.getEmail());
        } catch (IOException e) {
            //System.out.println(e.getMessage());
            thrown = true;
            message=e.getMessage();
        }
        assertTrue(thrown);
        assertEquals(message,"Cannot remove the last coach of the team");

        try {
            own.insertNewCoach(anotherTeam.getName(),"bellatrix","death eater","KILL",u3.getUserName(),u3.getEmail());
        } catch (IOException e) {

        }
        thrown= false;
        try {
            own.deleteCoach(team.getName(),u3.getUserName(),u3.getEmail());
        } catch (IOException e) {
            //System.out.println(e.getMessage());
            thrown = true;
            message=e.getMessage();
        }
        assertTrue(thrown);
        assertEquals(message,"The selected user is not a coach in the selected team");

    }















}
