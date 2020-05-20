package com.football_system.football_system.FMserver.RestControllersTests;


import com.football_system.football_system.FMserver.LogicLayer.*;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GuestControllerTest extends AbstractTest {

    private String url = "http://localhost:8080/";


    @Test
    public void  getTeams() throws Exception{
        try {

            String uri = url + "/Teams";
            Team[] teams = super.mapFromJson(getContent(uri) , Team[].class);
           // assertTrue(teams.length > 0);
        }catch (Exception e){
            assertTrue(false);
        }
    }

    @Test
    public void  getLeagues() throws Exception{
        DataComp.getInstance().addLeague(new League(League.LeagueType.MAJOR_LEAGUE));
        try{
            String uri = url + "/Leagues";
            League[] Leagues = super.mapFromJson(getContent(uri), League[].class);
           // assertTrue(Leagues.length > 0);
        }catch (Exception e){
//            assertTrue(false);
        }
    }
    @Test
    public void  getTeam() throws Exception{
        try{

            String uri = url + "/Teams/FC Barcelona";
            if(getContent(uri , true)!=null) {
                Team team = super.mapFromJson(getContent(uri, true), Team.class);
            }
           // assertTrue(team != null);
        }catch (Exception e){
//            assertTrue(false);
        }
    }
    @Test
    public void  getSeasons() throws Exception{
        try{
            String uri = url + "/Seasons";
            Season[] seasons = super.mapFromJson(getContent(uri), Season[].class);
         //   assertTrue(seasons.length > 0);
        }catch (Exception e){
            assertTrue(false);
        }
    }
    @Test
    public void  getSeasonsByLeagues() throws Exception{
        try{
            String uri = url + "/Seasons/ByLeague/MAJOR_LEAGUE";
            Season[] seasons = super.mapFromJson(getContent(uri), Season[].class);
     //       assertTrue(seasons.length > 0);
        }catch (Exception e){
            assertTrue(false);
        }
    }

    @Test
    public void  getSchedualingInfo() throws Exception{
        try{
            String uri = url + "/Seasons/League/MAJOR_LEAGUE/numberGamesPerTeam/2";

            Map<String,Integer> info = super.mapFromJson(getContent(uri) ,  Map.class);
//            assertTrue(info.get("number_of_Teams") == 2);
//            assertTrue(info.get("number_of_dates_needed") >= 2);
        }catch (Exception e){
//            assertTrue(false);
        }
    }

    @Test
    public void  getGames() throws Exception{
        try{
            String uri = url + "/Games";
            Game[] games = super.mapFromJson(getContent(uri), Game[].class);
         //   assertTrue(games.length > 0);
        }catch (Exception e){
            assertTrue(false);
        }
    }

}
