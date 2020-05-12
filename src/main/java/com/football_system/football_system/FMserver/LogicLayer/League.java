package com.football_system.football_system.FMserver.LogicLayer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.football_system.football_system.FMserver.DataLayer.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class League implements Serializable {

    public enum LeagueType{
        MAJOR_LEAGUE, SECOND_LEAGUE, LEAGUE_A,LEAGUE_B, LEAGUE_C
    }

    private LeagueType type;
    @JsonIgnore private List<Referee> refereeList;
    @JsonIgnore private List<Season> seasonList;
    @JsonIgnore private Map<Season,Policy> policyList;
    @JsonIgnore private Map<Season,RankPolicy> rankPolicyList;
    private String name;

    public League( LeagueType type, List<Referee> refereeList, List<Season> seasonList, Map<Season, Policy> policyList) {
        this.type = type;
        this.refereeList = refereeList;
        this.seasonList = seasonList;
        this.policyList = policyList;
        this.rankPolicyList = new HashMap<>();
    }
    public League( String type, List<Referee> refereeList, List<Season> seasonList, Map<Season, Policy> policyList) {
        if(isAType(type)){
            this.refereeList = refereeList;
            this.seasonList = seasonList;
            this.policyList = policyList;
            this.type=LeagueType.valueOf(type);
        }

    }

    public Map<Season, RankPolicy> getRankPolicyList() {
        return rankPolicyList;
    }

    public static League getLeagueByType(String Type){
        LeagueType leagueType = League.LeagueType.valueOf(Type);
        League leagueRes = null;
        if(leagueType != null){
            leagueRes = League.ShowAllLeagues().stream().filter(league -> league.getType().equals(leagueType)).findFirst().get();
        }
        return leagueRes;
    }
    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof League){
            return this.getType() == ((League) obj).getType() ;
        }
        return false;
    }

    private static IDataManager data(){
        return DataComp.getInstance();
    }

    public League(LeagueType leagueType){
        this.type = leagueType;
        data().addLeague(this);
    }

    public League(LeagueType leagueType ,String name){
        this.type = leagueType;
        this.name = name ;
        data().addLeague(this);
    }
    /**
     * id: League@1
     * check if League already exist
     * @param leagueType
     * @return League if existing , null if not
     */
    public static League checkIfLeagueExist(LeagueType leagueType){
        return data().SearchLeagueByType(leagueType);
    }

    /**
     * id: League@2
     * show all existing Leagues
     * @return all system leagues
     */
    public static List<League> ShowAllLeagues(){
        return data().getLeagueList();
    }

    /**
     * id: League@3
     * add new league
     * @param leagueType
     * @return if success/unsuccessful operation
     * @throws Exception if league type illegal
     */
    public static boolean addLeague(League.LeagueType leagueType ,String name) throws IOException{
        if(leagueType==null){
            throw new IOException("illegal league type");
        }
        League league= League.checkIfLeagueExist(leagueType);
        if(league != null){
            return false ;
        }
        data().addLeague(new League(leagueType , name));
        return true;
    }

    /**
     * id: League@5
     *  number of Dates correct - for pre-algorithm check
     * @param numberOfGamesPerTeam
     * @param numberOfTeam
     * @return number of games
     */
    public static int numberOfNeededDates(int numberOfGamesPerTeam , int numberOfTeam){
        int numberOfGames = 0 ;
        for(int i=numberOfTeam-1 ; i > 0  ; i--){
            numberOfGames = numberOfGames +i;
        }
        return numberOfGamesPerTeam*numberOfGames ;
    }

    /**
     * id: League@4
     * schedule games in season
     * @param numberOfGamesPerTeam
     * @return number of schduled Games
     */
    public List<Game> gamescheduling(int numberOfGamesPerTeam , Season season , List<String[]> allPossiableTimes){
        if (numberOfGamesPerTeam == 0 || season == null) return null;
        List<Game> gamesList = new LinkedList<>();
        List<Team> teams = Team.getAllTeamsInLeague(this);
        Team[] teamsArray = new Team[teams.size()];
        if(allPossiableTimes.size()<numberOfNeededDates(numberOfGamesPerTeam , teams.size())) return null;
            teams.toArray(teamsArray);
        List<Referee> referees = Referee.legalRefereesForLeague(this, season);
        Referee[] refereesArray = new Referee[referees.size()];
        referees.toArray(refereesArray);
        int nextTime = 0;
        int a = 0, b = 0;
        int games_counter = 0;
        int refereeSchecule = 0;
        for (int interations = 0; interations < numberOfGamesPerTeam; interations++) {
            for (int i = teams.size() - 1; i > 0; i--) {
                for (int j = 0; j < i; j++) {
                    if (interations % 2 == 0) {
                        a = i;
                        b = j;
                    } else {
                        a = j;
                        b = i;
                    }
                    Game game = new Game(season, teamsArray[a], teamsArray[b], null, refereesArray[refereeSchecule%(refereesArray.length)], null,
                            allPossiableTimes.get(nextTime)[0], allPossiableTimes.get(nextTime)[1], allPossiableTimes.get(nextTime)[2]);
                    gamesList.add(game);
                    nextTime++;
                    refereeSchecule++;
                    teamsArray[a].addHomeGame(game);
                    teamsArray[b].addAwayGame(game);
                    System.out.println(teamsArray[a].getName() + "-" + teamsArray[b].getName());
                    games_counter++;
                }
            }
        }
        return gamesList;
    }
    public LeagueType getType() {
        return type;
    }

    @JsonIgnore public String getTypeString(){
        return type.name();
    }

    public void setType(LeagueType type) {
        this.type = type;
    }

    public List<Referee> getRefereeList() {
        return refereeList;
    }

    public void setRefereeList(List<Referee> refereeList) {
        this.refereeList = refereeList;
    }

    public List<Season> getSeasonList() {
        return seasonList;
    }

    public void setSeasonList(List<Season> seasonList) {
        this.seasonList = seasonList;
    }

    public Map<Season, Policy> getPolicyList() {
        return policyList;
    }

    public void setPolicyList(Map<Season, Policy> policyList) {
        this.policyList = policyList;
    }


    /**
     * ID: Leauge@6
     * return true if the string type is an actual league type
     * @param type the type
     * @return true if the type is an actual type
     */
    public boolean isAType(String type){
        try{
            LeagueType.valueOf(type);
            return true;
        }catch (Exception e){
            System.out.println(" The input is not a valid league type");
            return false;
        }

    }
}
