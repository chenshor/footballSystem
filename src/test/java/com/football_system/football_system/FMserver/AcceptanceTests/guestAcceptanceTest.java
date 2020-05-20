package com.football_system.football_system.FMserver.AcceptanceTests;

import com.football_system.football_system.FMserver.LogicLayer.*;
import com.football_system.football_system.FMserver.ServiceLayer.*;
import com.football_system.football_system.FMserver.DataLayer.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
public class guestAcceptanceTest {
    private static IController system;
    private static User user ;
    private static Representative representative ;
    public static Guest testGuest ;
    private static IGuestService testGuestService;

    /**
     * for all use cases
     * @throws Exception
     */
    @Before
    public void before() throws Exception {
        DataComp.setDataManager(new DataManager());
        Administrator administrator = new Administrator("A","B","C");
        user = new User("AA","BB","CC");
        representative = new Representative(user, "lama name");
        user.addRole(representative);
        system = new Controller(representative, administrator);
        testGuest = new Guest();
        system.addGuest(testGuest);
        testGuestService = system.getGuestServices().get(testGuest);
    }

    /**
     * for use case 2.2
     */
    public void beforeUseCas22(){
        User user = testGuestService.register("Lior","Eitan","Lior@gmail.com","12345678");
        try{
            system.getUserServices().get(user).get(0).logOut();
        }catch (Exception E){
            E.printStackTrace();
        }
    }

    /**
     * for use case 2.3 and 2.4
     */
    public void beforeUseCase23(){
        Team testTeam1 = new Team("Hapoel Beer-Sheva","Terner",null);
        Team testTeam2 = new Team("Maccabi Beer-Sheva","Mekif Z",null);
        DataComp.getInstance().getTeamList().add(testTeam1);
        DataComp.getInstance().getTeamList().add(testTeam2);
    }

    /**
     * id: A@27
    * acceptance test 2.1.1 - use case 2.1
     * checks valid valid register to system
    */
    @Test
    public void registerTest(){
        String password = "12345678";
        String firstName = "Lior";
        String lastName = "Eitan";
        String email = "Lior@gmail.com";
        //checks if registered successfully with legal arguments
        assertNotNull(testGuestService.register(firstName,lastName,email,password));
        assertTrue(system.getGuestsList().size() == 0);
        assertTrue(system.getUserList().size() == 3);
        assertTrue(system.getUserList().get(2).getFirstName().equals("Lior"));
    }

    /**
     * id: A@28
     * acceptance test 2.1.3 - use case 2.1
     * checks valid input of arguments but already user with same email exists
     */
    @Test
    public void emailExistTest(){
        Guest guest = new Guest();
        system.addGuest(guest);
        IGuestService iGuestService = system.getGuestServices().get(guest);
        iGuestService.register("David","Lior","Eitan@gmail.com","12345678");
        String password = "12345678";
        String firstName = "David";
        String lastName = "Eitan";
        String email = "Eitan@gmail.com";
        assertNull(testGuestService.register(firstName,lastName,email,password));
    }

    /**
     * id: A@29
     * acceptance test 2.1.4 - use case 2.1
     * checks invalid input of arguments
     */
    @Test
   public void invalidInputTest(){
        //Wrong email form
        assertNull(testGuestService.register("Eitan","Lior","Testgmail.com","11212222"));
        //wrong password form
        assertNull(testGuestService.register("Eitan","Lior","Test@gmail.com","1121"));
        assertTrue(system.getUserList().size() == 2);
    }

    /**
     * id: A@30
     * acceptance test 2.2.1 - use case 2.2
     */
    @Test
    public void successfulSignInTest(){
        beforeUseCas22();
        assertNotNull(testGuestService.signIn("Lior@gmail.com","12345678"));
        assertTrue(system.getUserList().size() == 3);
        assertTrue(system.getUserServices().size() == 3);
    }

    /**
     * id: A@31
     * acceptance test 2.2.2 - use case 2.2
     * checks sign in with wrong arguments as input
     */
    @Test
    public void unsuccessfulSignInTest(){
        beforeUseCas22();
        //wrong email
        assertNull(testGuestService.signIn("Lio@gmail.com","12345678"));
        assertTrue(system.getUserList().size() == 2);
        //wrong password
        assertNull(testGuestService.signIn("Lior@gmail.com","12345679"));
        assertTrue(system.getUserList().size() == 2);
    }

    /**
     * id: A@32
     * acceptance test 2.3.1 - use case 2.3
     * checks successful show information
     */
    @Test
    public void successfulShowInformationTest(){
        beforeUseCase23();
        testGuestService.showInformationByCategory(Interest.Teams);
    }

    /**
     * id: A@33
     * acceptance test 2.4.1 - use case 2.4
     * checks successful show information
     */
    @Test
    public void successfulSearchInformation(){
        beforeUseCase23();
        testGuestService.searchInformation(Criteria.Category, "teams");
    }







}
