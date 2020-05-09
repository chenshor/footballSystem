package com.football_system.football_system.controllers;


import com.football_system.football_system.FMserver.LogicLayer.*;
import com.football_system.football_system.FMserver.ServiceLayer.GuestService;
import com.football_system.football_system.FMserver.ServiceLayer.IUserService;
import com.football_system.football_system.FMserver.ServiceLayer.Interest;
import com.football_system.football_system.FMserver.ServiceLayer.RepresentativeService;
import com.football_system.football_system.FootballSystemApplication;
import com.football_system.football_system.logicTest.SecurityObject;
import com.football_system.football_system.logicTest.UserTest;
import jdk.nashorn.internal.runtime.UserAccessorProperty;
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/Representative")
public class RepresentativeController {

    private static Logger errorsLogger = Logger.getLogger("errors");
    private static Logger eventsLogger = Logger.getLogger("events");

    /**
     * @param securityObject
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addTeam", method = RequestMethod.POST)
    public boolean addTeam(@RequestBody SecurityObject securityObject) throws Exception {
        User user = SecurityObject.Authorization(securityObject);
        if (user == null) {
            errorsLogger.error(" Security Error - unauthorized Security Object received");
            return false;
        }
        RepresentativeService representativeService = null;
        for (IUserService iUserService : FootballSystemApplication.system.getUserServices().get(user)) {
            if (iUserService instanceof RepresentativeService) {
                representativeService = (RepresentativeService) iUserService;
            }
        }
        if (representativeService == null) {
            LinkedHashMap<String, Object> objects = (LinkedHashMap<String, Object>) securityObject.getObject().get(0);
            LinkedHashMap<String, Object> TeamDetails = (LinkedHashMap<String, Object>) objects.get("Team");
            Team newTeam = new Team(TeamDetails.get("name").toString(), TeamDetails.get("stadium").toString(), null);
            LinkedHashMap<String, Object> leagueDetails = (LinkedHashMap<String, Object>) TeamDetails.get("league");

            League.LeagueType leagueType = League.LeagueType.MAJOR_LEAGUE;
            try {
                leagueType = League.LeagueType.valueOf(leagueDetails.get("type").toString());
            } catch (Exception e) {
            }

            League championsLeague = new League(leagueType);
            championsLeague.setName(leagueDetails.get("name").toString());
            newTeam.setLeague(championsLeague);
            DataComp.getInstance().addTeam(newTeam);
            if (TeamDetails.get("status").toString().equals("activityOpened")) {
                newTeam.setStatus(Team.TeamStatus.activityOpened);
            } else {
                newTeam.setStatus(Team.TeamStatus.activityClosed);
            }
            return true;
        }
        return false;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public boolean scheduleGame(@RequestBody SecurityObject securityObject)
            throws Exception {
//scheduleGame(League league , int numberOfGamesPerTeam , Season season , List<String[]> allPossiableTimes)
        if (SecurityObject.Authorization(securityObject) == null) return false;


        return true;
    }

    /**
     * Set Rank Policy
     *
     * @param securityObject
     * @return true if function completed with no errors
     * @throws Exception
     */
    @RequestMapping(value = "/setRankPolicy", method = RequestMethod.POST)
    public boolean setRankPolicy(@RequestBody SecurityObject securityObject) throws Exception {

        return true;
    }
}
