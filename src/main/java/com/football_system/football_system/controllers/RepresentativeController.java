package com.football_system.football_system.controllers;


import com.football_system.football_system.FMserver.LogicLayer.*;
import com.football_system.football_system.FMserver.ServiceLayer.IUserService;
import com.football_system.football_system.FMserver.ServiceLayer.RepresentativeService;
import com.football_system.football_system.FootballSystemApplication;
import com.football_system.football_system.serverObjects.SecurityObject;
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/Representative")
public class RepresentativeController {

    private static Logger errorsLogger = Logger.getLogger("errors");
    private static Logger eventsLogger = Logger.getLogger("events");


    @RequestMapping(
            value = "/addTeam",
            method = RequestMethod.POST)
    public boolean addTeam(@RequestBody SecurityObject securityObject)
            throws Exception {
        User user = SecurityObject.Authorization(securityObject);
        if (user == null) {
            errorsLogger.error("Authorization Error - Team Creation Failed");
            return false;
        }
        RepresentativeService representativeService = null;
        for (IUserService iUserService : FootballSystemApplication.system.getUserServices().get(user)) {
            if (iUserService instanceof RepresentativeService) {
                representativeService = (RepresentativeService) iUserService;
            }
        }
        if (representativeService != null) {
            LinkedHashMap<String, Object> objects = (LinkedHashMap<String, Object>) securityObject.getObject().get(0);
            LinkedHashMap<String, Object> TeamDetails = (LinkedHashMap<String, Object>) objects.get("Team");
            LinkedHashMap<String, Object> leagueDetails = (LinkedHashMap<String, Object>) TeamDetails.get("league");

            League.LeagueType leagueType = League.LeagueType.MAJOR_LEAGUE;
            try {
                leagueType = League.LeagueType.valueOf(leagueDetails.get("type").toString());
            } catch (Exception e) {
            }

            League league  = League.checkIfLeagueExist(leagueType) ;
            if(league == null){
                errorsLogger.error("General Error - Team Creation Failed: League type not valid");
                return false ;
            }

            Team newTeam = new Team(TeamDetails.get("name").toString(), TeamDetails.get("stadium").toString(), null);
           // league.setName(leagueDetails.get("name").toString());
            newTeam.setLeague(league);
            DataComp.getInstance().addTeam(newTeam);
          //  if (TeamDetails.get("status").toString().equals("activityOpened")) {
            newTeam.setStatus(Team.TeamStatus.activityOpened);
//            } else {
//                newTeam.setStatus(Team.TeamStatus.activityClosed);
//            }
            eventsLogger.info(" - The User : " + user.getUserName() + " - created new team");
            return true;
        }
        errorsLogger.error("General Error - Team Creation Failed");
        return false;
    }

    @RequestMapping(
            value = "/scheduleGame",
            method = RequestMethod.POST)
    public List<Game> scheduleGame(@RequestBody SecurityObject securityObject)
            throws Exception {

        //SecurityObject securityObject1 = new SecurityObject();
        List<Game> games = null ;

        User user = SecurityObject.Authorization(securityObject);
        if (user == null){
            errorsLogger.error("Authorization Error - Game Scheduling Failed");
            return games;
        }
        RepresentativeService representativeService = null;
        for (IUserService iUserService : FootballSystemApplication.system.getUserServices().get(user)) {
            if (iUserService instanceof RepresentativeService) {
                representativeService = (RepresentativeService) iUserService;
            }
        }
        if (representativeService != null) {
            LinkedHashMap<String, Object> objects = (LinkedHashMap<String, Object>) securityObject.getObject().get(0);
            String leagueType = objects.get("league").toString();
            String SeasonStartDate = objects.get("start").toString();
            String SeasonEndDate = objects.get("end").toString();
            int GamesPerTeam = Integer.parseInt(objects.get("numberOfGamesPerTeam").toString()) ;
            ArrayList<Object> dates = (ArrayList<Object>) objects.get("date") ;
          //  ArrayList<Object> dates =(  ArrayList<Object>)(((HashMap<String,Object>) ((ArrayList<Object>) objects.get("date")).get(0)).get("date"));

            List<String[]> possiableTimes = new LinkedList<>();
            for(Object date : dates){
                LinkedHashMap<String,String> tempDate = (LinkedHashMap<String,String> )(date) ;
                String [] gameDate = {tempDate.get("date"),tempDate.get("start") ,tempDate.get("end")} ;
                possiableTimes.add(gameDate) ;
            }
            League league = League.getLeagueByType(leagueType);
            Season season = Season.getSeason(SeasonStartDate,SeasonEndDate);

            games = representativeService.scheduleGame(league, GamesPerTeam , season ,possiableTimes);
           // securityObject.setObject(games);

        }
        if(games==null){
            errorsLogger.error("General Error - Game Scheduling Failed");
        }else{
            eventsLogger.info(" - The User : " + user.getUserName() + " - Game Scheduling done successfully");
        }
        return games;
        //return "hi there!" ;
    //    return securityObject;
    }


    @RequestMapping(
            value = "/rankPolicy",
            method = RequestMethod.POST)
    public ResponseEntity<Void> rankPolicy(@RequestBody SecurityObject securityObject)
            throws Exception {

        List<Game> games = null ;

        User user = SecurityObject.Authorization(securityObject);
        if (user == null)  {
            errorsLogger.error("Authorization Error - Rank Policy set Failed");
            return ResponseEntity.badRequest().build();
        }
        RepresentativeService representativeService = null;
        for (IUserService iUserService : FootballSystemApplication.system.getUserServices().get(user)) {
            if (iUserService instanceof RepresentativeService) {
                representativeService = (RepresentativeService) iUserService;
            }
        }
        if (representativeService != null) {
            LinkedHashMap<String, Object> objects = (LinkedHashMap<String, Object>) securityObject.getObject().get(0);
            String leagueType = objects.get("league").toString();
            String SeasonStartDate = objects.get("start").toString();
            String SeasonEndDate = objects.get("end").toString();
            int win = Integer.parseInt(objects.get("win").toString());
            int draw = Integer.parseInt(objects.get("draw").toString());
            int lose = Integer.parseInt(objects.get("lose").toString());

            League league = League.getLeagueByType(leagueType);
            Season season = Season.getSeason(SeasonStartDate,SeasonEndDate);

            if(representativeService.setRankPolicy(season , league,win  ,draw  ,lose )) {
                System.out.println("rank policy been setted to win:"+win+", draw:"+draw+" , lose:"+lose);
                eventsLogger.info(" - The User : " + user.getUserName() + " - Rank Policy set successfully");
                return ResponseEntity.accepted().build();
            }
        }
        errorsLogger.error("General Error - Rank Policy set Failed");
        return ResponseEntity.badRequest().build();

    }


}
