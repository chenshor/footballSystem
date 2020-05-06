package com.football_system.football_system.controllers;


import com.football_system.football_system.FMserver.LogicLayer.Guest;
import com.football_system.football_system.FMserver.LogicLayer.Team;
import com.football_system.football_system.FMserver.ServiceLayer.GuestService;
import com.football_system.football_system.FMserver.ServiceLayer.Interest;
import com.football_system.football_system.FootballSystemApplication;
import com.football_system.football_system.logicTest.SecurityObject;
import com.football_system.football_system.logicTest.UserTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;

@RestController
public class RepresentativeController {

//    @RequestMapping("/Teams")
//    public List<Team> get() {
////        Guest guest = new Guest() ;
////        GuestService guestService = new GuestService(guest , FootballSystemApplication.system) ;
////        return guestService.showInformationByCategory(Interest.Teams); ;
//    }

    @RequestMapping(
            value = "/add",
            method = RequestMethod.POST)
    public boolean scheduleGame(@RequestBody SecurityObject securityObject)
            throws Exception {
//scheduleGame(League league , int numberOfGamesPerTeam , Season season , List<String[]> allPossiableTimes)
        if(SecurityObject.Authorization(securityObject)==null) return false ;

        return true;
    }
}
