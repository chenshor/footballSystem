package com.football_system.football_system.FMserver.ServiceLayer;

import com.football_system.football_system.FMserver.LogicLayer.*;

import java.io.IOException;
import java.util.List;

public class RepresentativeService extends AUserService {

    public RepresentativeService(Controller control) {
        super(control);
    }

    /**
     * id: RepresentativeService@1
     * add new League
     *
     * @param leagueType
     * @return boolean of success/unsuccessful operation
     * @throws Exception if league type illigel
     */
    public boolean addLeague(League.LeagueType leagueType ,String name) throws IOException{
        return League.addLeague(leagueType , name);
    }

    /**
     * id: RepresentativeService@2
     *
     * @return show all Leagues that existing in the system
     */
    public List<League> showAllLeagus() throws IOException {
        return League.ShowAllLeagues();
    }

    /**
     * id: RepresentativeService@3
     * can add new season -or- if season existing addes a league to it
     *
     * @param start  date of season
     * @param end    date of season
     * @param league to link the season to League
     * @throws IOException if season already exists
     */
    public void addSeason(String start, String end, League league) throws IOException {
        Season.addSeason(start, end, league);
    }

    /**
     * id: RepresentativeService@4
     * show all existing Seasons
     *
     * @return all system Seasons
     */
    public List<Season> showAllSeasons() throws IOException {
        return Season.ShowAllSeasons();
    }

    /**
     * id: RepresentativeService@5
     * add new referee from exist users
     * @return true if added successfully, else if already exists
     */
    public boolean addNewRefereeFromUsers(User user , String qualification , String name) throws IOException{
        if(Referee.MakeUserReferee( user,  qualification,  name)) {
            control.updateServicesToUser(user);
            return true;
        }
        return  false;
    }

    /**
     * id: RepresentativeService@6
     * add new referee from exist users
     * @return true if added successfully, else if already exists
     */
    public boolean removeRefereeFromUsers(User user) throws IOException{
        control.updateServicesToUser(user);
        Referee referee = user.ifUserRoleIncludeReferee();
        if(referee == null){
            return false;
        }
        if(Referee.RemoveUserReferee(referee)){
            control.updateServicesToUser(user);
            return true;
        }
        return false ;
    }


    /**
     * id: RepresentativeService@7
     * show all system referees
     * @return system referees
     */
    public List<Referee> showAllReferees() throws IOException{
        return Referee.getReferees() ;
    }

    /**
     * id: RepresentativeService@8
     * show all system referees
     * @return true if added successfully
     */
    public boolean addJudgmentApproval(Referee referee , League league, Season season ) throws IOException{
        if(league == null || season == null || referee == null) return false;
        return referee.addJudgmentApproval(new JudgmentApproval(league,season,referee));
    }

    /**
     * id: RepresentativeService@9
     * show all system referees
     *
     * @return true if added successfully
     */
    public boolean removeJudgmentApproval(Referee referee , League league, Season season ) throws IOException{
        if(league == null || season == null || referee == null) return false;
        return referee.removeJudgmentApproval(new JudgmentApproval(league,season));
    }

    /**
     * id: RepresentativeService@10
     * schedule all games
     *
     * @param league
     * @param numberOfGamesPerTeam number Of Games Per Team in season
     * @param season
     * @param allPossiableTimes of games
     * @return number of Scheduled Games
     */
    public List<Game> scheduleGame(League league , int numberOfGamesPerTeam , Season season , List<String[]> allPossiableTimes) throws IOException{
        int number_of_games =League.numberOfNeededDates( numberOfGamesPerTeam ,Team.getAllTeamsInLeague(league).size());
        if( allPossiableTimes.size() < number_of_games ){
            throw new IOException("number of needed dates:"+number_of_games +", You're provide only:"+allPossiableTimes.size()+" dates.");
        }
        return league.gamescheduling(numberOfGamesPerTeam , season , allPossiableTimes);
    }

    /**
     * id: RepresentativeService@11
     * create new Team
     *
     * @param name    - team name
     * @param stadium - stadium name
     */
    public void createTeam(String name, String stadium) {
        control.getRepresentative().createTeam(name, stadium, null);
    }

    /**
     * Set Rank Policy to specific league in specific season
     * NOT DEFAULT
     *
     * @param season - season
     * @param league - league
     * @param win - points fo win
     * @param draw - points for draw
     * @param lose - points for  draw
     * @return boolean indicates action success
     */
    public boolean setRankPolicy(Season season, League league, int win, int draw, int lose) {
        RankPolicy newRankPolicy = createRank(season, league, win, draw, lose);
        if (newRankPolicy == null) return false;
     //   try {
        //    league.getRankPolicyList().remove(season);
            if( league.getRankPolicyList() != null) {
                league.getRankPolicyList().put(season, newRankPolicy);
                DataComp.getInstance().addLeague(league);
            }
        DataComp.getInstance().addRankedPolicy(newRankPolicy);

//        }catch (Exception exception){
//
//        }
        return true;
    }

    /**
     * Set Rank Policy to specific league in specific season
     * DEFAULT
     *
     * @param season - season
     * @param league - league
     * @return boolean indicates action success
     */
    public boolean setRankPolicy(Season season, League league) {
        RankPolicy newRankPolicy = createDefaultRank(season, league);
        if (newRankPolicy == null) return false;
        league.getRankPolicyList().remove(season);
        league.getRankPolicyList().put(season, newRankPolicy);
        return true;
    }

    private RankPolicy createDefaultRank(Season season, League league) {
        if (season == null) return null;
        if (league == null) return null;
        return new RankPolicy(league, season);
    }

    private RankPolicy createRank(Season season, League league, int win, int draw, int lose) {
        if (season == null) return null;
        if (league == null) return null;
        return new RankPolicy(league, season, win, draw, lose);
    }
}
