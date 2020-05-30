package com.football_system.football_system.FMserver.IntegrationTests;

import com.football_system.football_system.FMserver.LogicLayer.*;
import com.football_system.football_system.FMserver.ServiceLayer.*;
import com.football_system.football_system.FMserver.DataLayer.*;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AdministratorIntegrationTest {

    private Administrator administrator;
    private Team team1;
    private Team team2;
    private Owner owner1;
    private Owner owner2;
    private Manager manager1;
    private Manager manager2;
    private User user1;
    private User user2;
    private User user3;
    private AdministratorService adminService;

    @Before
    public void init() {
        DataComp.setDataManager(new DataManager());
        team1 = new Team("hapoel", "blumfield", null);
        team2 = new Team("macabi", "stadium", null);
        DataComp.getInstance().addTeam(team1);
        DataComp.getInstance().addTeam(team2);

        user1 = new User("email", "aaa", "theQueen");
        user2 = new User("aa", "ss", "dd");
        user3 = new User("ww", "ee", "rr");
        owner1 = new Owner(user1, "haim");
        owner1.addTeam(team1);
        owner2 = new Owner(user2, "moshe");
        owner2.addTeam(team2);
        manager1 = new Manager(user1, "haim", team1);
        manager2 = new Manager(user3, "yossi", team2);
        DataComp.getInstance().addNewUser(user1);
        DataComp.getInstance().addNewUser(user2);

        team1.addOwner(owner1);
        team2.addOwner(owner2);
        team1.addManager(manager1);
        team2.addManager(manager2);

        administrator = new Administrator("aa", "scv", "jdjdj");

        adminService = new AdministratorService(administrator);
    }

    /**
     * I@19
     */
    @Test
    public void closeTeam() {
        adminService.closeTeam(team1);
    }

    /**
     * I@20
     */
    @Test
    public void showComplaints() {
        assertTrue(adminService.showComplaints().size()==0);
        Complaint complaint1 = new Complaint(user1, "bad", "2012-12-12");
        Complaint complaint2 = new Complaint(user2, "good", "2018-12-13");
        DataComp.getInstance().addComplaint(complaint1);
        DataComp.getInstance().addComplaint(complaint2);
        adminService.showComplaints();
        assertTrue(adminService.showComplaints().size()==2);
    }


    /**
     * I@21
     */
    @Test
    public void commentComplaint() {
        Complaint complaint1 = new Complaint(user1, "bad", "2012-12-12");
        Complaint complaint2 = new Complaint(user2, "good", "2018-12-13");
        DataComp.getInstance().addComplaint(complaint1);
        DataComp.getInstance().addComplaint(complaint2);

        adminService.showComplaints();
        System.out.println();
        adminService.commentComplaint(complaint2, "boringgggg");
        adminService.commentComplaint(complaint2, "fff");
        adminService.showComplaints();


    }

    /**
     * I@22
     */
    @Test
    public void deleteUser() {
        adminService.deleteUser(user2);
    }

    /**
     * I@25
     */
    @Test
    public void displayLog() {
        administrator.displayLog();
    }
}

