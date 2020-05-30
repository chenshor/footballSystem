package com.football_system.football_system.FMserver.LogicLayer;

import com.football_system.football_system.FMserver.DataLayer.IDataManager;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.Entity;

@Entity
@EnableAutoConfiguration
@javax.persistence.Table(name = "rank_policies")
public class RankPolicy extends Policy{
    private final int WIN;
    private final int DRAW;
    private final int LOSE;

    public RankPolicy(League league, Season season){
        super(league, season);
        WIN = 3;
        DRAW = 1;
        LOSE = 0;
    }

    public RankPolicy(League league, Season season, int win, int draw, int lose) {
        super(league, season);
        WIN = win;
        DRAW = draw;
        LOSE = lose;
    }

    public RankPolicy(){
        WIN = 3;
        DRAW = 1;
        LOSE = 0;
    }

    private IDataManager data(){
        return DataComp.getInstance();
    }


    @Override
    public League getLeague() {
        return super.getLeague();
    }

    @Override
    public void setLeague(League league) {
        super.setLeague(league);
        data().addRankedPolicy(this);
    }

    @Override
    public Season getSeason() {
        return super.getSeason();
    }

    @Override
    public void setSeason(Season season) {
        super.setSeason(season);
        data().addRankedPolicy(this);
    }

    /**
     * id: RankPolicy@1
     * update league table
     * @param game - game
     */
    public void updatePoints(Game game){
        Table leagueTable = getSeason().getTable(getLeague());
        leagueTable.updateTable(game,WIN,DRAW,LOSE);
    }
}
