package com.football_system.football_system;

import java.io.IOException;

import com.football_system.football_system.FMserver.DataLayer.DataManager;
import com.football_system.football_system.FMserver.LogicLayer.*;
import com.football_system.football_system.FMserver.ServiceLayer.*;
import com.football_system.football_system.logicTest.SecurityObject;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

//@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FootballSystemApplication.class)
@AutoConfigureMockMvc
public abstract class AbstractTest {

    @Autowired
    protected MockMvc mvc;

//    @Autowired
//    WebApplicationContext webApplicationContext;
    public static IController system;
    private static User user ;
    public static Representative representative ;

//    protected void setUp() {
//        DataComp.setDataManager(new DataManager());
//        //mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//        Administrator administrator = new Administrator("A", "B", "C");
//        user = new User("rep@gmail.com", "12345678", "Lior");
//        representative = new Representative(user, "lama name"); // rep user
//        user.addRole(representative);
//        system = new Controller(representative, administrator);
//        SecurityObject.addUserToSystem(user);
//
//
//        //---- add Seasons by respresentative
//        RepresentativeService representativeService = null;
//        for (IUserService iUserService : FootballSystemApplication.system.getUserServices().get(user)) {
//            if (iUserService instanceof RepresentativeService) {
//                representativeService = (RepresentativeService) iUserService;
//            }
//        }
//        try {
//            representativeService.addLeague(League.LeagueType.MAJOR_LEAGUE, "championsLeague");
//            representativeService.addLeague(League.LeagueType.LEAGUE_A, "cool League");
//
//            representativeService.addSeason("2020-01-03", "2021-01-05", League.getLeagueByType("MAJOR_LEAGUE"));
//            representativeService.addSeason("2021-05-06", "2022-03-20", League.getLeagueByType("MAJOR_LEAGUE"));
//            representativeService.addSeason("2021-05-06", "2022-03-20", League.getLeagueByType("LEAGUE_A"));
//        } catch (Exception e) {
//        }
//// ------ add user
//        try {
//            Guest guest = new Guest();
//            FootballSystemApplication.system.addGuest(guest);
//            GuestService guestService = (GuestService) system.getGuestServices().get(guest);
//            User reg = guestService.register("da", "s", "chen@walla.com", "12345678");
//            SecurityObject.addUserToSystem(reg);
//            //setTeamsDB();
//
//
//            //add refereeUser
//            Guest guest2 = new Guest();
//            FootballSystemApplication.system.addGuest(guest2);
//            GuestService guestService2 = (GuestService) system.getGuestServices().get(guest2);
//            User referee = guestService2.register("refer", "s", "referee@walla.com", "12345678");
//            SecurityObject.addUserToSystem(referee);
//
//            representativeService.addNewRefereeFromUsers(referee, "very nice referee", "alon");
//            representativeService.addJudgmentApproval(representativeService.showAllReferees().get(0), League.getLeagueByType("MAJOR_LEAGUE"), Season.getSeason("2020-01-03", "2021-01-05"));
//            representativeService.addJudgmentApproval(representativeService.showAllReferees().get(0), League.getLeagueByType("MAJOR_LEAGUE"), Season.getSeason("2021-05-06", "2022-03-20"));
//
//
//            Team barcelona = new Team("FC Barcelona", "Camp Nou", null);
//            Team realMadrid = new Team("Real Madrid CF", "Bernabeu", null);
//            League championsLeague = League.getLeagueByType("MAJOR_LEAGUE");
//            championsLeague.setName("Champions League");
//            barcelona.setLeague(championsLeague);
//            realMadrid.setLeague(championsLeague);
//            DataComp.getInstance().addTeam(barcelona);
//            DataComp.getInstance().addTeam(realMadrid);
//            barcelona.setStatus(Team.TeamStatus.activityOpened);
//            realMadrid.setStatus(Team.TeamStatus.activityOpened);
//            DataComp.getInstance().addGame(new Game(Season.getSeason("2020-01-03", "2021-01-05"), barcelona, realMadrid, Referee.getReferees().get(0), Referee.getReferees().get(0), "2021-09-08", "8:00", "9:30"));
//        } catch (Exception e) {
//        }
//    }
    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }

    protected String getContent(String uri){
        return  getContent( uri , true);
    }
    protected String getContent(String uri , boolean needToBePassed){
        try {
            MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                    .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

            int status = mvcResult.getResponse().getStatus();
            assertTrue(needToBePassed == (200==status));
            String content = mvcResult.getResponse().getContentAsString();
            System.out.println("myContent"+content);
            return content ;
        }catch (Exception exception){
            System.out.println(exception);
            Assert.fail();
        }
        return null ;
    }
}