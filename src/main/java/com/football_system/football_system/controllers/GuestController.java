package com.football_system.football_system.controllers;

import com.football_system.football_system.FMserver.LogicLayer.Guest;
import com.football_system.football_system.FMserver.LogicLayer.League;
import com.football_system.football_system.FMserver.LogicLayer.Season;
import com.football_system.football_system.FMserver.LogicLayer.Team;
import com.football_system.football_system.FMserver.ServiceLayer.GuestService;
import com.football_system.football_system.FootballSystemApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GuestController {

    private static GuestService guestService;
    public  GuestController(){
        Guest guest = new Guest() ;
        guestService = new GuestService(guest , FootballSystemApplication.system) ;
    }
    @RequestMapping("/Teams")
    public List<Team> getTeam() {
        return guestService.getTeams();
    }

    @RequestMapping("/Teams/{name}")
    public Team getTeam(@PathVariable String name) {
        return guestService.getTeams().stream().filter(team -> team.getName().equals(name))
                .findFirst().get();
    }

    @RequestMapping("/Leagues")
    public List<League> getLeagues() {
        return guestService.getLeagues();
    }

    @RequestMapping("/Leagues/{name}")
    public League getLeagues(@PathVariable String name) {
        return guestService.getLeagues().stream().filter(league -> league.getName().equals(name))
                .findFirst().get();
    }

    @RequestMapping("/Season")
    public List<Season> getSeason() {
        return guestService.getSesons();
    }

//    @RequestMapping("/Season/{name}")
//    public Season getSeason(@PathVariable String name) {
//        return guestService.getSesons().stream().filter(season -> season.getName().equals(name))
//                .findFirst().get();
//    }
}
