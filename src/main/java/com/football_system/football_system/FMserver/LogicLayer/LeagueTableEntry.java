package com.football_system.football_system.FMserver.LogicLayer;

public class LeagueTableEntry {
    private Team team;
    private int totalPlayed;
    private int won;
    private int drawn;
    private int lost;
    private int GF; // goals for
    private int GA; // goals against
    private int GD; // goal difference
    private int points;

    public LeagueTableEntry(Team team) {
        this.team = team;
    }

    /**
     * id: LeagueTableEntry@1
     * @param points - points
     * @param GA - against
     * @param GF - for
     */
    public void update(int points, int GA, int GF){
        totalPlayed++;
        this.GA += GA;
        this.GF += GF;
        updateGD();
        this.points += points;
    }

    public void updateWin(){
        won++;
    }
    public void updateDraw(){
        drawn++;
    }
    public void updateLose(){
        lost++;
    }

    private void updateGD() {
        this.GD = this.GF - this.GA;
    }

    public Team getTeam() {
        return team;
    }

    public int getTotalPlayed() {
        return totalPlayed;
    }

    public int getWon() {
        return won;
    }

    public int getDrawn() {
        return drawn;
    }

    public int getLost() {
        return lost;
    }

    public int getGF() {
        return GF;
    }

    public int getGA() {
        return GA;
    }

    public int getGD() {
        return GD;
    }

    public int getPoints() {
        return points;
    }
}
