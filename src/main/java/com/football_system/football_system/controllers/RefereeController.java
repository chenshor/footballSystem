package com.football_system.football_system.controllers;


import com.football_system.football_system.FMserver.LogicLayer.Game;
import com.football_system.football_system.FMserver.LogicLayer.GameEventCalender;
import com.football_system.football_system.FMserver.LogicLayer.User;
import com.football_system.football_system.FMserver.ServiceLayer.GuestService;
import com.football_system.football_system.FMserver.ServiceLayer.IUserService;
import com.football_system.football_system.FMserver.ServiceLayer.RefereeService;
import com.football_system.football_system.FMserver.ServiceLayer.RepresentativeService;
import com.football_system.football_system.FootballSystemApplication;
import com.football_system.football_system.logicTest.SecurityObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/Referee")
public class RefereeController {

    @RequestMapping(
            value = "/getEventProperties",
            method = RequestMethod.POST)
    public Object getEventProperties(@RequestBody SecurityObject securityObject)
            throws Exception {
        User user = SecurityObject.Authorization(securityObject);
        List<Object> returnList = new LinkedList<>() ;
        if (user == null) return null;
        RefereeService refereeService = null;
        for (IUserService iUserService : FootballSystemApplication.system.getUserServices().get(user)) {
            if (iUserService instanceof RefereeService) {
                refereeService = (RefereeService) iUserService;
            }
        }
        if (refereeService != null) {
            LinkedHashMap<String, Object> objects = (LinkedHashMap<String, Object>) securityObject.getObject().get(0);
            Integer game_id = Integer.parseInt(objects.get("game_id").toString());
            returnList.add(refereeService.getEventTypes())  ;
            returnList.add(refereeService.getPlayingTeamsInTheGame(game_id))  ;
            return returnList;
        }
        return null;
    }


    @RequestMapping(
            value = "/getGamesByReferee",
            method = RequestMethod.POST)
    public List<Game> getGamesByReferee(@RequestBody SecurityObject securityObject)
            throws Exception {
        User user = SecurityObject.Authorization(securityObject);
        if (user == null) return null;
        RefereeService refereeService = null;
        for (IUserService iUserService : FootballSystemApplication.system.getUserServices().get(user)) {
            if (iUserService instanceof RefereeService) {
                refereeService = (RefereeService) iUserService;
            }
        }
        if (refereeService != null) {
            LinkedHashMap<String, Object> objects = (LinkedHashMap<String, Object>) securityObject.getObject().get(0);
            String referee_id = (String) objects.get("referee_id");
            return refereeService.getGamesByRefereeID(referee_id) ;
        }
        return null;
    }

    @RequestMapping(
            value = "/addEventToGame",
            method = RequestMethod.POST)
    public void addEventToGame(@RequestBody SecurityObject securityObject)
            throws Exception {
        User user = SecurityObject.Authorization(securityObject);
        if (user == null) return ;
        RefereeService refereeService = null;
        for (IUserService iUserService : FootballSystemApplication.system.getUserServices().get(user)) {
            if (iUserService instanceof RefereeService) {
                refereeService = (RefereeService) iUserService;
            }
        }
        if (refereeService != null) {
            LinkedHashMap<String, Object> objects = (LinkedHashMap<String, Object>) securityObject.getObject().get(0);
            Integer game_id = Integer.parseInt(objects.get("game_id").toString());
            String description = (String) objects.get("description");
            String eventType = (String) objects.get("eventType");
            Boolean home = Game.checkIfHomeTeam(objects.get("team").toString() , game_id) ;
            Integer minute = Integer.parseInt(objects.get("minute").toString());
            refereeService.addEventGame(home , game_id , description ,eventType,minute );
        }
        return ;
    }
}
