package com.football_system.football_system.FMserver.LogicLayer;

import java.util.*;

public class Table {

    private HashMap<Team, LeagueTableEntry> leagueTableEntries;
    private Comparator<LeagueTableEntry> policyComparator;

    /**
     * constructor
     * @param policy - Comparator || enter null for default policy
     */
    public Table(Comparator<LeagueTableEntry> policy) {
        leagueTableEntries = new HashMap();
        if (policy == null) {
            policyComparator =
                    Comparator.comparing(LeagueTableEntry::getPoints)
                            .thenComparing(LeagueTableEntry::getGD)
                            .thenComparing(LeagueTableEntry::getGF)
                            .thenComparing(LeagueTableEntry::getGA);
        } else {
            policyComparator = policy;
        }
    }

    /**
     * id: Table@1
     * init table
     * @param teams - list of teams
     */
    public void init(List<Team> teams) {
        for (Team team : teams) {
            leagueTableEntries.put(team, new LeagueTableEntry(team));
        }
    }

    /**
     * id: Table@2
     * update table according to game
     * @param game - game
     * @param win - points
     * @param draw - points
     * @param lose - points
     */
    public void updateTable(Game game, int win, int draw, int lose) {
        Team home = game.getHome();
        Team away = game.getAway();
        int homeGoals = game.getResult().getHome();
        int awayGoals = game.getResult().getAway();
        if (homeGoals == awayGoals) {
            leagueTableEntries.get(home).update(draw, awayGoals, homeGoals);
            leagueTableEntries.get(away).update(draw, homeGoals, awayGoals);
            leagueTableEntries.get(home).updateDraw();
            leagueTableEntries.get(away).updateDraw();
        } else if (homeGoals > awayGoals) {
            leagueTableEntries.get(home).update(win, awayGoals, homeGoals);
            leagueTableEntries.get(away).update(lose, homeGoals, awayGoals);
            leagueTableEntries.get(home).updateWin();
            leagueTableEntries.get(away).updateLose();
        } else {
            leagueTableEntries.get(home).update(lose, awayGoals, homeGoals);
            leagueTableEntries.get(away).update(win, homeGoals, awayGoals);
            leagueTableEntries.get(home).updateLose();
            leagueTableEntries.get(away).updateWin();
        }
    }

    /**
     * id: Table@3
     * Table Sorting
     * @return Table sorted by Policy Comparator
     */
    public List<LeagueTableEntry> getLeagueTableEntries() {
        List<LeagueTableEntry> table = new ArrayList<>(leagueTableEntries.values());
        table.sort(policyComparator.reversed());
        return table;
    }

    /**
     * id: Table@4
     * get team stats
     * @param teamName - team name
     * @return entry of team's stats
     */
    public LeagueTableEntry getTeamStat(String teamName) {
        return (LeagueTableEntry) leagueTableEntries.values().stream().filter(
                teamStat -> teamStat.getTeam().getName().equalsIgnoreCase(teamName)
        ).toArray()[0];
    }

    /**
     * id: Table@5
     * PRINT TABLE
     */
    public void printTable() {
        System.out.println(
                padding("Place") +
                        padding("Team") +
                        padding("Points") +
                        padding("Games") +
                        padding("Win") +
                        padding("Draw") +
                        padding("Lose") +
                        padding("GD") +
                        padding("GF") +
                        padding("GA")
        );
        int i = 1;
        for (LeagueTableEntry entry : getLeagueTableEntries()) {
            System.out.println(
                    padding(Integer.toString(i)) +
                            padding(entry.getTeam().getName()) +
                            padding(((Integer) entry.getPoints()).toString()) +
                            padding(((Integer) entry.getTotalPlayed()).toString()) +
                            padding(((Integer) entry.getWon()).toString()) +
                            padding(((Integer) entry.getDrawn()).toString()) +
                            padding(((Integer) entry.getLost()).toString()) +
                            padding(((Integer) entry.getGD()).toString()) +
                            padding(((Integer) entry.getGF()).toString()) +
                            padding(((Integer) entry.getGA()).toString())
            );
            i++;
        }
    }

    private String padding(String s) {
        return String.format("%-" + 10 + "s", s);
    }
}
