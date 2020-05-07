package com.football_system.football_system.FMserver.LogicLayer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.football_system.football_system.FMserver.DataLayer.*;
import com.football_system.football_system.FMserver.ServiceLayer.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Season implements Serializable {

    @JsonIgnore
    private IController system;

    private String start;
    private String end;
    @JsonIgnore
    private List<Game> gameList;

    private List<League> leagueList;

    @JsonIgnore
    private Map<League, Table> leagueTables;

    public Table getTable(League league){
        return getLeagueTables().get(league);
    }

    public Map<League, Table> getLeagueTables() {
        return leagueTables;
    }

    private static IDataManager data(){
        return DataComp.getInstance();
    }

    public Season(IController system, String start, String end, List<Game> gameList, List<League> leagueList, Map<League, Policy> policies) {
        this.system = system;
        this.start = start;
        this.end = end;
        this.gameList = gameList;
        this.leagueList = leagueList;
    }

    public Season(String start, String end, League league){
        this.setStart(start);
        this.setEnd(end);
        leagueList = new LinkedList<League>();
        this.leagueList.add(league);
    }

    public static Season getSeason(String start , String end){
        Season seasonRes = null;
        if(start != null && end != null){
            for(Season season : Season.ShowAllSeasons()){
                if(season.getStart().equals(start)&&season.getEnd().equals(end)) return season;
            }
        }
        return seasonRes;
    }

    public boolean seasonContainsLeague(String Type){
      for (League league :getLeagueList()){
          if(league.getType()==League.LeagueType.valueOf(Type)) return true;
      }
      return false;
    }

    /**
     * id: Season@1
     * @param start date of the season
     * @param end date of the season
     * @param league linked to Season
     * @return new created Season
     * @throws IOException if season already exists
     */
    public static Season addSeason(String start ,String end,League league) throws IOException {
        Season season =  data().SearchSeason(start,end);
        if(season == null){
            season = new Season(start,end,league);
            data().addSeason(season);
        }
        else if(season.getLeagueList().contains(league)){
            throw new IOException("Season already exist");
        }else {
            season.leagueList.add(league);
        }
        return season;
    }

    /**
     * id: Season@2
     * show all existing Seasons
     * @return all system Seasons
     */
    public static List<Season> ShowAllSeasons(){
        return data().getSeasonList();
    }
    public IController getSystem() {
        return system;
    }

    public void setSystem(IController system) {
        this.system = system;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public List<Game> getGameList() {
        return gameList;
    }

    public void setGameList(List<Game> gameList) {
        this.gameList = gameList;
    }

    public List<League> getLeagueList() {
        return leagueList;
    }

    public void setLeagueList(List<League> leagueList) {
        this.leagueList = leagueList;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Season){
            return ((Season)obj ).getStart().equals(this.getStart()) &&
                    ((Season)obj ).getEnd().equals(this.getEnd());
        }
        return false;
    }
}
