package com.football_system.football_system;

import com.football_system.football_system.FMserver.DataLayer.DataManager;
import com.football_system.football_system.FMserver.LogicLayer.*;
import com.football_system.football_system.FMserver.ServiceLayer.*;
import com.football_system.football_system.logicTest.SecurityObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FootballSystemApplication {

	public static IController system;
	private static User user ;
	public static Representitive representative ;


	public static void main(String[] args) {
		DataComp.setDataManager(new DataManager());
		Administrator administrator = new Administrator("A", "B", "C");
		user = new User("rep@gmail.com", "123456as", "Lior");
		representative = new Representitive(user, "lama name"); // rep user
		user.addRole(representative);
		system = new Controller(representative, administrator);
		SecurityObject.addUserToSystem(user) ;
// ------ add user
		Guest guest = new Guest();
		FootballSystemApplication.system.addGuest(guest);
		GuestService guestService = (GuestService) system.getGuestServices().get(guest);
		User chen =  guestService.register("da","s","chen@walla.com", "1234567q") ;
		SecurityObject.addUserToSystem(chen) ;

		SpringApplication.run(FootballSystemApplication.class, args);
	}

}
