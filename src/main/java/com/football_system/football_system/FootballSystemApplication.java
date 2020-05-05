package com.football_system.football_system;

import com.football_system.football_system.FMserver.DataLayer.DataManager;
import com.football_system.football_system.FMserver.LogicLayer.*;
import com.football_system.football_system.FMserver.ServiceLayer.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FootballSystemApplication {

	public static IController system;
	private static User user ;
	private static Representitive representative ;


	public static void main(String[] args) {
		DataComp.setDataManager(new DataManager());
		Administrator administrator = new Administrator("A", "B", "C");
		user = new User("AA", "BB", "CC");
		representative = new Representitive(user, "lama name");
		user.addRole(representative);
		system = new Controller(representative, administrator);

		SpringApplication.run(FootballSystemApplication.class, args);
	}

}
