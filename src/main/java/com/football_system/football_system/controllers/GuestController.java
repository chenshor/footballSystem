package com.football_system.football_system.controllers;

import com.football_system.football_system.FMserver.LogicLayer.*;
import com.football_system.football_system.FMserver.ServiceLayer.GuestService;
import com.football_system.football_system.FootballSystemApplication;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.rmi.server.ExportException;
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
        try {
            return guestService.getTeams();
        }catch (Exception e){
            return null;
        }
    }

    @RequestMapping("/Teams/{name}")
    public Team getTeam(@PathVariable String name) {
        try {
            return guestService.getTeams().stream().filter(team -> team.getName().equals(name))
                    .findFirst().get();
        }catch (Exception exception){
            return null;
        }
    }

    @RequestMapping("/Leagues")
    public List<League> getLeagues() {
        try {
            return guestService.getLeagues();
        }catch (Exception e){
            return null;
        }
    }

    @RequestMapping("/Leagues/{type}")
    public League getLeagues(@PathVariable String type) {
        try {
            return guestService.getLeagues().stream().filter(league -> league.getType().equals(type))
                    .findFirst().get();
        }catch (Exception exception){
            return null;
        }
    }

    @RequestMapping("/Seasons")
    public List<Season> getSeason() {
        try {
            return guestService.getSesons();
        }catch (Exception exception){
            return null;
        }
    }


    @RequestMapping("/Seasons/ByLeague/{leagueType}")
    public List<Season> getSeason(@PathVariable String leagueType) {
        try {
            List<Season> seasons = new LinkedList<>();
            for (Season season : guestService.getSesons()) {
                if (season.seasonContainsLeague(leagueType)) {
                    seasons.add(season);
                }
            }
            return seasons;
        }catch (Exception exception){
            return null;
        }
    }


    @RequestMapping("/Seasons/League/{leagueType}/numberGamesPerTeam/{numberOfGamesPerTeam}")
    public Map<String,Integer> getNumberOfTeams(/*@PathVariable String season ,*/@PathVariable("leagueType") String leagueType  , @PathVariable("numberOfGamesPerTeam") String numberOfGamesPerTeam) {
        try {
            HashMap <String,Integer> hashMap = new HashMap<>();
            int numberOfTeams = Team.getAllTeamsInLeague(guestService.getLeagues().stream().filter(league -> league.getType()==League.LeagueType.valueOf(leagueType))
                    .findFirst().get()).size();
            hashMap.put("number_of_Teams",numberOfTeams);
            hashMap.put("number_of_dates_needed",League.numberOfNeededDates(Integer.parseInt(numberOfGamesPerTeam),numberOfTeams ));
            return hashMap;
        }catch (Exception e){
            return null;
        }
    }

    @RequestMapping("/Games")
    public List<Game> getGames() {
        try {
            return DataComp.getInstance().getGameList();
        }catch (Exception exception){
            return null;
        }
    }

    @RequestMapping("/Games/gameUpdates/{game_id}")
    public Object getGameUpdates(@PathVariable String game_id) {
        try {
            Integer gameID = Integer.parseInt(game_id);
            Object obj = GuestService.getEvents(gameID);
            return obj;
        }catch (Exception exception){
            return null;
        }
    }

}
