package com.echocano.tennis.league.domain.model;

public class SetResult {
    private Long id;
    private Integer setNumber;
    private Integer team1Games;
    private Integer team2Games;
    private Integer team1TieBreakPoints;
    private Integer team2TieBreakPoints;

    public SetResult() {}

    public boolean isTieBreak() {
        return (team1Games == 7 && team2Games == 6) || (team1Games == 6 && team2Games == 7);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getSetNumber() { return setNumber; }
    public void setSetNumber(Integer setNumber) { this.setNumber = setNumber; }

    public Integer getTeam1Games() { return team1Games; }
    public void setTeam1Games(Integer team1Games) { this.team1Games = team1Games; }

    public Integer getTeam2Games() { return team2Games; }
    public void setTeam2Games(Integer team2Games) { this.team2Games = team2Games; }

    public Integer getTeam1TieBreakPoints() { return team1TieBreakPoints; }
    public void setTeam1TieBreakPoints(Integer points) { this.team1TieBreakPoints = points; }

    public Integer getTeam2TieBreakPoints() { return team2TieBreakPoints; }
    public void setTeam2TieBreakPoints(Integer points) { this.team2TieBreakPoints = points; }
}