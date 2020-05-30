package com.football_system.football_system.controllers;

import com.football_system.football_system.FMserver.LogicLayer.*;
import com.football_system.football_system.FMserver.ServiceLayer.GuestService;
import com.football_system.football_system.FootballSystemApplication;
import org.apache.log4j.Logger;
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
    private static Logger errorsLogger = Logger.getLogger("errors");
    private static Logger eventsLogger = Logger.getLogger("events");

    public  GuestController(){
        Guest guest = new Guest() ;
        guestService = new GuestService(guest , FootballSystemApplication.system) ;
    }
    @RequestMapping("/Teams")
    public List<Team> getTeam() {
        try {
            eventsLogger.info(" - The Guest : get Teams");
            return guestService.getTeams();
        }catch (Exception e){
            errorsLogger.error("General Error - can not get teams");
            return null;
        }
    }

    @RequestMapping("/Teams/{name}")
    public Team getTeam(@PathVariable String name) {
        try {
            Team res_team= guestService.getTeams().stream().filter(team -> team.getName().equals(name))
                    .findFirst().get();
            eventsLogger.info(" - The Guest : get Team");
            return res_team;
        }catch (Exception exception){
            errorsLogger.error("General Error - can not get team");
            return null;
        }
    }

    @RequestMapping("/Leagues")
    public List<League> getLeagues() {
        try {
            List<League> getLeagues = guestService.getLeagues();
            eventsLogger.info(" - The Guest : get Leagues");
            return getLeagues;
        }catch (Exception e){
            errorsLogger.error("General Error - can not get Leagues");
            return null;
        }
    }

    @RequestMapping("/Leagues/{type}")
    public League getLeague(@PathVariable String type) {
        try {
             League res_league = guestService.getLeagues().stream().filter(league -> league.getType().equals(type))
                    .findFirst().get();
            eventsLogger.info(" - The Guest : get League");
            return res_league;
        }catch (Exception exception){
            errorsLogger.error("General Error - can not get League");
            return null;
        }
    }

    @RequestMapping("/Seasons")
    public List<Season> getSeason() {
        try {
            List<Season> seasons =  guestService.getSesons();
            eventsLogger.info(" - The Guest : get Seasons");
            return seasons;
        }catch (Exception exception){
            errorsLogger.error("General Error - can not get Seasons");
            return null;
        }
    }


    @RequestMapping("/Seasons/ByLeague/{leagueType}")
    public List<Season> getSeason(@PathVariable String leagueType) {
        try {
            List<Season> seasons = new LinkedList<>();
            List<Season>seasonList = guestService.getSesons();
            for (Season season : guestService.getSesons()) {
                if (season.seasonContainsLeague(leagueType)) {
                    seasons.add(season);
                }
            }
            eventsLogger.info(" - The Guest : get Seasons by league type");
            return seasons;
        }catch (Exception exception){
            errorsLogger.error("General Error - can not get Seasons by league type");
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
            eventsLogger.info(" - The Guest : get number Of teams in season");
            return hashMap;
        }catch (Exception e){
            errorsLogger.error("General Error - get number Of teams in season");
            return null;
        }
    }

    @RequestMapping("/Games")
    public List<Game> getGames() {
        try {
            List<Game> games =DataComp.getInstance().getGameList();
            eventsLogger.info(" - The Guest : get Games");
            return games;
        }catch (Exception exception){
            errorsLogger.error("General Error - can not get Games");
            return null;
        }
    }

    @RequestMapping("/Games/gameUpdates/{game_id}")
    public Object getGameUpdates(@PathVariable String game_id) {
        try {
            Integer gameID = Integer.parseInt(game_id);
            Object obj = GuestService.getEvents(gameID);
            eventsLogger.info(" - The Guest : get updates");
            return obj;
        }catch (Exception exception){
            errorsLogger.error("General Error - can not get updates");
            return null;
        }
    }

}
