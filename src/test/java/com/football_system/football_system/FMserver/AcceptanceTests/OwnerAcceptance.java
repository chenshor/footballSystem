package com.football_system.football_system.FMserver.AcceptanceTests;

import com.football_system.football_system.FMserver.LogicLayer.*;
import com.football_system.football_system.FMserver.ServiceLayer.*;
import com.football_system.football_system.FMserver.DataLayer.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class OwnerAcceptance {

    private static IController system;

    private static User representitiveUser ;
    private static Representative representative ;
    private static Guest guest;
    private static IGuestService guestService;
    private static RepresentativeService representativeService;
    public static AdministratorService administratorService;


    private static Team team;
    private static User ownerUser;

    private static Owner owner;
    private static OwnerService ownerService;

    private static User userPlayer;
    private static User userCoach;
    private static User userManager;
    private static User userOwner2;



    @BeforeClass
    public static void beforeClass() {

        DataComp.setDataManager(new DataManager());
        Administrator administrator = new Administrator("A", "B", "C");
        representitiveUser = new User("alonaLas@post.bgu.ac.il", "12345678", "alonalas");
        representative = new Representative(representitiveUser, "lama name");

        representitiveUser.addRole(representative);

        system = new Controller(representative, administrator);

        guest = new Guest();
        system.addGuest(guest);
        guestService = system.getGuestServices().get(guest);
        //administratorService=new AdministratorService(administrator);

        representativeService= (RepresentativeService) system.getUserServices().get(representitiveUser).get(1);

        ownerUser = guestService.register("Lior","Eitan","Lior@gmail.com","12345678");
        owner= new Owner(ownerUser,"Alona");

        team = new Team("Hapoel","Blumfield",new Page(owner));
        owner.addTeam(team);
        team.addOwner(owner);

        userCoach = new User("coach@gmail.com","12345678","harry","potter");
        userPlayer = new User("player@gmail.com","12345678","Hermione","Greinger");
        userManager = new User("manager@gmail.com","12345678","ronald","wisley");
        userOwner2 = new User("owner@gmail.com","12345678","severmouse","prof.");



        userManager = guestService.register("ronald","wisley",userManager.getEmail(),userManager.getPassword());
        userPlayer = guestService.register("Hermione","Granger",userPlayer.getEmail(),userPlayer.getPassword());
        userCoach = guestService.register("harry","potter",userCoach.getEmail(),userCoach.getPassword());
        userOwner2 = guestService.register("Severus","Snape",userOwner2.getEmail(),userOwner2.getPassword());


        ownerUser.addRole(owner);
        system.addServicesToUser(ownerUser);


    }

    @Before
    public void getTheRole(){
        for(IUserService iUserService : system.getUserServices().get(ownerUser)){
            if(iUserService instanceof  OwnerService){
                ownerService  = (OwnerService) iUserService ;
            }
        }
        if(ownerService == null)
            assertTrue(false);
        for(Role role: ownerUser.getRoles()){
            if(role instanceof Owner){
                owner=(Owner) role;
            }
        }
    }

    @Test
    public void test(){
        ////////////successfull uc6.1.1

        try {
            Player p = insertPlayer("hermi","genious",1,2,1980,userPlayer.getUserName(),userPlayer.getEmail());
            assertNotNull(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(team.getPlayer(userPlayer));
        assertEquals(userPlayer.getRoles().size(),2);
        try {
            Coach c = insertCoach("harry","seeker","the boy who lived",userCoach.getUserName(),userCoach.getEmail());
            assertNotNull(c);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(team.getCoach(userCoach));
        assertEquals(userCoach.getRoles().size(),2);
        try {
            Manager m = insertManager("Ron",userManager.getUserName(),userManager.getEmail(),
                    false,false,false,false);
            assertNotNull(m);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(team.getManager(userManager));
        assertEquals(userManager.getRoles().size(),2);

        ///////////// un successfull uc6.1.1

        try {
            insertPlayer("aaa","bbb",1,2,1955,userPlayer.getUserName(),"playergmailcom");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            assertEquals("The chosen user does not exist, please insert valid inputs",e.getMessage());
        }

        ////////////////uc 6.1.3 successfull

        try {
            updateRoleHolder(team.getCoach(userCoach),"job","best coach ever");
            updateRoleHolder(team.getPlayer(userPlayer),"position","best player ever");
            updateRoleHolder(team.getManager(userManager),"aaa","bbb");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            assertEquals(e.getMessage(),"Owner can not update a manager details");
        }
        assertEquals(team.getCoach(userCoach).getJob(),"best coach ever");
        assertEquals(team.getPlayer(userPlayer).getPosition(),"best player ever");

        //////////////////uc 6.1.3 un successfull

        try {
            updateRoleHolder(team.getPlayer(userPlayer),"Ballerina","best ballerina");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            assertEquals("Invalid attribute selected: Ballerina", e.getMessage());
        }

        ////////////////////////uc 6.1.2 successfull

        try {
            deleteAsset(team.getPlayer(userPlayer));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        assertNull(team.getPlayer(userPlayer));

        //////////////////////uc 6.1.2 un successfull

        try {
            User test = new User("david@levi","12345678","david","levi");
            Player p = new Player(test,"aaa",team,"asd","1-2-1934",new Page(owner));
            deleteAsset(p);
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            assertEquals("The chosen user does not exist, please insert valid inputs",e.getMessage());
        }

        //////////////////////uc 6.2 successfull

        try {
            nominateOwner(userOwner2);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(team.getOwnerList().size(),2);
        assertNotNull(team.getOwner(userOwner2));
        assertEquals(userOwner2.getRoles().size(),2);
        assertEquals(team.getOwner(userOwner2).getTeam(team.getName()).getName(), "Hapoel");
        assertEquals(team.getOwner(userOwner2).getNominatedBy(),owner);

        ////////////////////////uc 6.2 un successfull

        try {
            nominateOwner(userOwner2);
        } catch (IOException e) {
            e.getMessage();
            assertEquals("User is allready nominated as owner in this team",e.getMessage());
        }

        /////////////////////// uc 6.3 successfull

        try {
            cancelOwnership(team.getOwner(userOwner2));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(team.getOwnerList().size(),1);
        assertEquals(userOwner2.getRoles().size(),1);
        assertNull(team.getOwner(userOwner2));

        /////////////////// uc 6.3 un successfull

        User test = null;
        try {
            test = new User("dumble@gmail.com","12345678","Albus","dumbledore");
            test = guestService.register("albus","dumbledore",test.getEmail(),test.getPassword());
            Owner ownerTest = new Owner(test,"dumbledore");
            test.addRole(ownerTest);
            ownerTest.addTeam(team);
            team.addOwner(ownerTest);
            cancelOwnership(ownerTest);
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            assertEquals("The selected owner can't be removed since he did not nominated by you", e.getMessage());
        }

        ////////////////// uc 6.4 successfull

        Manager m = null;
        try {
            m = insertManager("Draco",userPlayer.getUserName(),userPlayer.getEmail(),
                    true,false,true,false);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(team.getManagerList().size(),2);
        assertEquals(userPlayer.getRoles().size(), 2); // fan & manager (player was deleted)
        assertTrue(m.getPermissionBooleanMap().get(Manager.Permission.playerDeletion));
        assertTrue(m.getPermissionBooleanMap().get(Manager.Permission.playerAddition));
        assertFalse(m.getPermissionBooleanMap().get(Manager.Permission.coachAddition));
        assertFalse(m.getPermissionBooleanMap().get(Manager.Permission.coachDeletion));

        ////////////////// uc 6.4 un successfull

        Manager m1 = null;
        try {
            m1 = insertManager("albus",test.getUserName(),test.getEmail(),true,true,true,true);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            assertEquals("The selected user is allready nominated as the owner in the team", e.getMessage());
        }
        assertNull(m1);

        /////////////////// uc 6.5 successfull

        try {
            deleteAsset(m);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(m.getUser().getRoles().size(),1);
        assertEquals(team.getManagerList().size(), 1);
        assertNull(team.getManager(m.getUser()));

        ////////////////// uc 6.5 un successfull

        Manager m2 = new Manager(userPlayer,"choe",team);
        userPlayer.addRole(m2);
        team.addManager(m2);
        team.getRoleHolders().add(m2);
        try {
            deleteAsset(m2);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            assertEquals(e.getMessage(),"The selected manager was not nominated by you");
        }

        ///////////////////// uc 6.6.1 successfull

        try {
            closeTeam(team);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(team.getStatus(), Team.TeamStatus.activityClosed);

        ///////////////////// uc 6.6.2 successfull

        try {
            openTeam(team);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        assertEquals(team.getStatus(), Team.TeamStatus.activityOpened);
    }

    /**
     * A@18
     * @param team1
     * @throws IOException
     */
    private void openTeam(Team team1) throws IOException {
        ownerService.renewTeamActivity(owner,team1);
    }

    /**
     * A@19
     * @param team1
     * @throws IOException
     */
    private void closeTeam(Team team1) throws IOException {
        ownerService.closeTeamActivity(owner,team1);
    }

    /**
     * A@20
     * @param ownerNominate
     * @throws IOException
     */
    private void cancelOwnership(Owner ownerNominate) throws IOException {
        ownerService.removeOwnership(owner,ownerNominate,team.getName());
    }

    /**
     * A@21
     * @param user
     * @throws IOException
     */
    private void nominateOwner(User user) throws IOException {
        ownerService.nominateNewOwner(owner,team.getName(),user,"Snape");
    }

    /**
     * A@22
     * @param roleHolder
     * @throws IOException
     */
    private void deleteAsset(RoleHolder roleHolder) throws IOException {
        ownerService.deleteRoleHolder(owner,team.getName(),roleHolder.getUser().getUserName(),roleHolder.getUser().getEmail(),roleHolder);
    }

    /**
     * A@23
     * @param roleHolder
     * @param attribute
     * @param desciption
     * @throws IOException
     */
    private void updateRoleHolder(RoleHolder roleHolder, String attribute, String desciption) throws IOException {
        Map<String, String> attributes = new HashMap<>();
        attributes.put(attribute,desciption);
        ownerService.updateRoleHolder(owner,team.getName(),roleHolder,attributes);
    }

    /**
     * A24
     * @param managerName
     * @param userName
     * @param email
     * @param insertPlayer
     * @param insertCoach
     * @param deletePlayer
     * @param deleteCoach
     * @return
     * @throws IOException
     */
    private Manager insertManager(String managerName,String userName, String email
        ,boolean insertPlayer, boolean insertCoach, boolean deletePlayer, boolean deleteCoach) throws IOException {
        Map<Manager.Permission,Boolean> permissionBooleanMap = new HashMap<>();
        permissionBooleanMap.put(Manager.Permission.playerAddition,insertPlayer);
        permissionBooleanMap.put(Manager.Permission.playerDeletion,deletePlayer);
        permissionBooleanMap.put(Manager.Permission.coachAddition,insertCoach);
        permissionBooleanMap.put(Manager.Permission.coachDeletion,deleteCoach);
        Manager m = ownerService.insertNewManager(owner,team.getName(),managerName,userName,email,permissionBooleanMap);
        return m;
    }

    /**
     * A@25
     * @param coachName
     * @param qualification
     * @param job
     * @param userName
     * @param email
     * @return
     * @throws IOException
     */
    private Coach insertCoach(String coachName,String qualification, String job, String userName, String email) throws IOException {
        Coach c = ownerService.insertNewCoach(owner,team.getName(),coachName,qualification,job,userName,email);
        return c;
    }

    /**
     * A@26
     * @param name
     * @param position
     * @param day
     * @param month
     * @param year
     * @param userName
     * @param email
     * @return
     * @throws IOException
     */
    private Player insertPlayer(String name, String position, int day, int month, int year, String userName, String email) throws IOException {
        Player p = ownerService.insertNewPlayer(owner,team.getName(),name,position,day,month,year,userName,email);
        return p;
    }
}
