package com.football_system.football_system.FMserver.UnitTests;

import com.football_system.football_system.FMserver.LogicLayer.*;
import com.football_system.football_system.FMserver.ServiceLayer.*;
import com.football_system.football_system.FMserver.DataLayer.*;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;


import java.io.IOException;

import java.util.List;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SeasonTest  {

    private IDataManager dm;
    private League league ;
    private League league2 ;
    private Season season ;

    @Before
    public void init(){
        dm =  new DataManager();
        league = new League(League.LeagueType.LEAGUE_B);
        league2 = new League(League.LeagueType.LEAGUE_A);
        season = null ;
    }

    /**
     * id: U@18
     */
    @Test
    public void addSeason() {

        String start = "2020-02-01" ;
        String end = "2020-02-03" ;
        boolean success = false ;

        try {
            season = Season.addSeason(start, end, league);
            assertNotNull(season);
            Season.addSeason(start, end, league);
        }catch (IOException e){
            success = true ;
        }
        assertTrue(success);
        try{
            assertEquals(season , Season.addSeason(start, end, league2));
        }catch (IOException e){

        }
    }

    /**
     * id: U@19
     */
    @Test
    public void showAllSeasons() {
        List<Season> seasons = Season.ShowAllSeasons();
        assertEquals(1 , seasons.size());
    }
}