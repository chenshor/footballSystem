package com.football_system.football_system;

import com.football_system.football_system.FMserver.DataLayer.DataManager;
import com.football_system.football_system.FMserver.LogicLayer.*;
import com.football_system.football_system.FMserver.ServiceLayer.*;
import com.football_system.football_system.logicTest.SecurityObject;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FootballSystemApplication {

	public static IController system;
	private static User user ;
	public static Representative representative ;
	private static Logger errorsLogger = Logger.getLogger("errors");
	private static Logger eventsLogger = Logger.getLogger("events");

	public static void main(String[] args) {
		SpringApplication.run(FootballSystemApplication.class, args);
		run();
	}


	public static void run(){
		DataComp.setDataManager(new DataManager());
		Administrator administrator = new Administrator("A", "B", "C");
		user = new User("rep@gmail.com", "12345678", "Lior");
		DataComp.getInstance().addNewUser(user);
		representative = new Representative(user, "lama name"); // rep user
		user.addRole(representative);
		system = new Controller(representative, administrator);
		SecurityObject.addUserToSystem(user) ;



		//---- add Seasons by respresentative
		RepresentativeService representativeService = null;
		for (IUserService iUserService : FootballSystemApplication.system.getUserServices().get(user)) {
			if (iUserService instanceof RepresentativeService) {
				representativeService = (RepresentativeService) iUserService;
			}
		}
		try {
			representativeService.addLeague(League.LeagueType.MAJOR_LEAGUE, "championsLeague");
			representativeService.addLeague(League.LeagueType.LEAGUE_A, "cool League");

			representativeService.addSeason("2020", "2021", League.getLeagueByType("MAJOR_LEAGUE"));
			representativeService.addSeason("2021", "2022", League.getLeagueByType("MAJOR_LEAGUE"));
			representativeService.addSeason("2022", "2023", League.getLeagueByType("LEAGUE_A"));
		}catch (Exception e){}
// ------ add user
		try {
			Guest guest = new Guest();
			FootballSystemApplication.system.addGuest(guest);
			GuestService guestService = (GuestService) system.getGuestServices().get(guest);
			User reg = guestService.register("da", "s", "chen@walla.com", "12345678");
			SecurityObject.addUserToSystem(reg);
			//setTeamsDB();


			//add refereeUser
			Guest guest2 = new Guest();
			FootballSystemApplication.system.addGuest(guest2);
			GuestService guestService2 = (GuestService) system.getGuestServices().get(guest2);
			User referee = guestService2.register("refer", "s", "referee@walla.com", "12345678");
			SecurityObject.addUserToSystem(referee);

			representativeService.addNewRefereeFromUsers(referee, "very nice referee", "alon");
			representativeService.addJudgmentApproval(representativeService.showAllReferees().get(0), League.getLeagueByType("MAJOR_LEAGUE") , Season.getSeason("2020","2021")) ;
			representativeService.addJudgmentApproval(representativeService.showAllReferees().get(0), League.getLeagueByType("MAJOR_LEAGUE") , Season.getSeason("2021","2023")) ;


			Team barcelona = new Team("FC Barcelona", "Camp Nou", null);
			Team realMadrid = new Team("Real Madrid CF", "Bernabeu", null);
			League championsLeague = League.getLeagueByType("MAJOR_LEAGUE") ;
			championsLeague.setName("Champions League");
			barcelona.setLeague(championsLeague);
			realMadrid.setLeague(championsLeague);
			DataComp.getInstance().addTeam(barcelona);
			DataComp.getInstance().addTeam(realMadrid);
			barcelona.setStatus(Team.TeamStatus.activityOpened);
			realMadrid.setStatus(Team.TeamStatus.activityOpened);
			new Game( Season.getSeason("2020","2021") , barcelona,realMadrid, Referee.getReferees().get(0) ,Referee.getReferees().get(0) ,"2021-09-08","8:00","9:30");
		}catch (Exception e){}

	}

}
