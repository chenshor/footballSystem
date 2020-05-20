package com.football_system.football_system.FMserver.UnitTests;

import com.football_system.football_system.FMserver.LogicLayer.*;
import com.football_system.football_system.FMserver.ServiceLayer.*;
import com.football_system.football_system.FMserver.DataLayer.*;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LeagueTest  {


    private League league ;


    @Before
    public void init(){
        DataComp.setDataManager(new DataManager());
        league = new League(League.LeagueType.LEAGUE_B);
    }

    /**
     * id : U@11
     */
    @Test
    public void checkIfLeagueExist() {
        assertNull( League.checkIfLeagueExist(League.LeagueType.LEAGUE_A)  );
        assertTrue( league.equals(League.checkIfLeagueExist(League.LeagueType.LEAGUE_B) ) );
        assertTrue(null == League.checkIfLeagueExist(League.LeagueType.LEAGUE_A) );
    }

    /**
     * id : U@12
     */
   @Test
    public void showAllLeagues() {
        List<League> leagues = League.ShowAllLeagues();
        assertEquals(1 , leagues.size());
        assertEquals(League.LeagueType.LEAGUE_B , leagues.get(0).getType());
    }
    /**
     * id : U@26
     */
    @Test
    public void addLeagues() {
        try {
            List<League> leagues = League.ShowAllLeagues();
            assertEquals(1, leagues.size());
            assertTrue(League.addLeague(League.LeagueType.MAJOR_LEAGUE , "Champions League"));
            assertEquals(2, leagues.size());
        }catch (Exception e){}
    }

    /**
     *  id : U@39
     */
    @Test
    public void numberOfNeededDates(){
        assertTrue(League.numberOfNeededDates(2,4)==12);
    }
}