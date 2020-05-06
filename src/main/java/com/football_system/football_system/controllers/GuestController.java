package com.football_system.football_system.controllers;

import com.football_system.football_system.FMserver.LogicLayer.Guest;
import com.football_system.football_system.FMserver.LogicLayer.Team;
import com.football_system.football_system.FMserver.ServiceLayer.GuestService;
import com.football_system.football_system.FootballSystemApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GuestController {

    @RequestMapping("/Teams")
    public List<Team> get() {
        Guest guest = new Guest() ;
        GuestService guestService = new GuestService(guest , FootballSystemApplication.system) ;
        return guestService.getTeams();
    }
}
