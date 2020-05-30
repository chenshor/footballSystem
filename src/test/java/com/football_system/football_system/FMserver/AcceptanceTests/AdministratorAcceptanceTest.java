package com.football_system.football_system.FMserver.AcceptanceTests;
import com.football_system.football_system.FMserver.LogicLayer.*;
import com.football_system.football_system.FMserver.ServiceLayer.*;
import com.football_system.football_system.FMserver.DataLayer.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;


public class AdministratorAcceptanceTest {
    private static IController system;
    private static User user ;
    private static Representative representative ;
    private static Guest guest;
    private static IGuestService guestService;
    private static RepresentativeService representativeService;
    public static AdministratorService administratorService;


    private static Team team;
    private static User user1;

    private static Owner owner;
    private static User user2;
    private static OwnerService ownerService;

    private static Player player;
    private static User user3;

    private static Manager manager;
    private static User user4;

    @BeforeClass
    public static void beforeClass() throws Exception {
        DataComp.setDataManager(new DataManager());
        Administrator administrator = new Administrator("A", "B", "C");
        user = new User("AA", "BB", "CC");
        representative = new Representative(user, "lama name");
        user.addRole(representative);
        system = new Controller(representative, administrator);

        guest = new Guest();
        system.addGuest(guest);
        guestService = system.getGuestServices().get(guest);
        administratorService=new AdministratorService(administrator);

        user1 = guestService.register("Lior","Eitan","Lior@gmail.com","12345678");
        representativeService= (RepresentativeService) system.getUserServices().get(user).get(1);
        owner= new Owner(user1,"moshe");
        user1.addRole(owner);
        system.addServicesToUser(user1);


    }

    @Before
    public void getTheRole(){
        for(IUserService iUserService : system.getUserServices().get(user1)){
            if(iUserService instanceof  OwnerService){
                ownerService  = (OwnerService) iUserService ;
            }
        }
        if(ownerService == null) Assert.assertTrue(false);
        for(Role role: user1.getRoles()){
            if(role instanceof Owner){
                owner=(Owner) role;
            }
        }
    }

    @Test
    public void test(){
        displayLog();
        displayComplaints();
        respondComplaint();
        removeUser();
        closeTeam();


    }

    /**
     * A@13
     * UC:8.4
     */
//    @Test
    public void displayLog(){
        administratorService.displayLog();
    }


    /**
     * A@14
     * UC: 8.3.1
     */
//    @Test
    public void displayComplaints(){
        User temp=guestService.register("lili","lolo","L@gmail.com","123488678");
        assertTrue(administratorService.showComplaints().size()==0); // prints that there isn't a complaint in the system
        Complaint complaint1 = new Complaint(user1, "bad", "2012-12-12");
        Complaint complaint2 = new Complaint(temp, "good", "2018-12-13");
        DataComp.getInstance().addComplaint(complaint1);
        DataComp.getInstance().addComplaint(complaint2);
        assertTrue(administratorService.showComplaints().size()==2);
    }


    /**
     * A@15
     * UC: 8.3.2
     */
//    @Test
    public void respondComplaint(){
        Complaint complaint1 = new Complaint(user1, "bad", "2012-12-12");
        Complaint complaint2 = new Complaint(user2, "good", "2018-12-13");
        DataComp.getInstance().addComplaint(complaint1);
        DataComp.getInstance().addComplaint(complaint2);
        assertFalse(complaint1.isAnswered());
        assertFalse(complaint2.isAnswered());

        administratorService.commentComplaint(complaint1,"aaa");
        administratorService.commentComplaint(complaint1,"Aa"); // prints that the admin already responded

        assertTrue(complaint1.isAnswered());
    }


    /**
     * A@16
     * UC: 8.2
     */
//    @Test
    public void removeUser(){
        Team team = new Team("Hapoel", "Blumfield",null);
        user3=guestService.register("David","Eitan","c@gmail.com","12345678");
        user4=guestService.register("aa","bb","c1@gmail.com","12345678");
        owner.addTeam(team);
        try {
            player=ownerService.insertNewPlayer(owner,"Hapoel","David@Eitan","GoalKeeper",1,2,1995,"David@Eitan","c@gmail.com");
            manager=ownerService.insertNewManager(owner,"Hapoel","aaa","aa@bb","c1@gmail.com",null);
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!" + DataComp.getInstance().getUserList().size() +"!!!!!!!!!");
            assertTrue(DataComp.getInstance().getUserList().size()==4);
            administratorService.deleteUser(player.getUser());
//            system.removeUser(player.getUser());
            assertTrue(DataComp.getInstance().getUserList().size()==3);


            administratorService.deleteUser(manager.getUser());
//            system.removeUser(manager.getUser());

            assertTrue(DataComp.getInstance().getUserList().size()==2);

            administratorService.deleteUser(owner.getUser());
            system.removeUser(owner.getUser());
            assertTrue(DataComp.getInstance().getUserList().size()==2);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * A@17
     * UC: 8.1
     */
//    @Test
    public void closeTeam(){
        Team team = new Team("Hapoel", "Blumfield",null);
        owner.addTeam(team);
        DataComp.getInstance().addTeam(team);
        administratorService.closeTeam(team);
        assertTrue(team.isFinalClose()==true);


    }







}
