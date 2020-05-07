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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    @RequestMapping("/Leagues/{type}")
    public League getLeagues(@PathVariable String type) {
        return guestService.getLeagues().stream().filter(league -> league.getType().equals(type))
                .findFirst().get();
    }

    @RequestMapping("/Seasons")
    public List<Season> getSeason() {
        return guestService.getSesons();
    }


    @RequestMapping("/Seasons/ByLeague/{leagueType}")
    public List<Season> getSeason(@PathVariable String leagueType) {
        List<Season> seasons = new LinkedList<>();
        for (Season season:guestService.getSesons()) {
            if(season.seasonContainsLeague(leagueType)){
                seasons.add(season);
            }
        }
        return seasons;
    }


    @RequestMapping("/Seasons/League/{leagueType}/numberGamesPerTeam/{numberOfGamesPerTeam}")
    public Map<String,Integer> getNumberOfTeams(/*@PathVariable String season ,*/@PathVariable("leagueType") String leagueType  , @PathVariable("numberOfGamesPerTeam") String numberOfGamesPerTeam) {
        HashMap <String,Integer> hashMap = new HashMap<>();
        int numberOfTeams = Team.getAllTeamsInLeague(guestService.getLeagues().stream().filter(league -> league.getType()==League.LeagueType.valueOf(leagueType))
                .findFirst().get()).size();
        hashMap.put("number_of_Teams",numberOfTeams);
        hashMap.put("number_of_dates_needed",League.numberOfNeededDates(Integer.parseInt(numberOfGamesPerTeam),numberOfTeams ));
        return hashMap;
    }
//    @RequestMapping("/Season/{name}")
//    public Season getSeason(@PathVariable String name) {
//        return guestService.getSesons().stream().filter(season -> season.getName().equals(name))
//                .findFirst().get();
//    }
}
