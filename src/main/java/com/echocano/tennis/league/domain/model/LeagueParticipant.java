package com.echocano.tennis.league.domain.model;

import java.util.List;

import org.jboss.logging.Logger;

public class LeagueParticipant {

    private static final Logger LOG = Logger.getLogger(LeagueParticipant.class);

    private Long id;
    private League league;
    private Team team;
    private Integer matchesPlayed = 0;
    private Integer matchesWon = 0;
    private Integer matchesLost = 0;
    private Integer lossesByWalkover = 0;
    private Integer setsWon = 0;
    private Integer setsLost = 0;
    private Integer points = 0;

    public LeagueParticipant() {
    }

    public Integer getSetDifference() {
        return setsWon - setsLost;
    }

    public void recalculateStats(List<Match> matches, League league) {
        this.matchesWon = 0;
        this.matchesLost = 0;
        this.setsWon = 0;
        this.setsLost = 0;
        this.points = 0;

        for (Match m : matches) {
            if (m.getWinner() == null)
                continue;
            boolean isWinner = m.getWinner().getId().equals(this.team.getId());
            if (isWinner) {
                this.matchesWon++;
                this.points += league.getPointsPerWin();
            } else {
                this.matchesLost++;
                this.points += m.isWalkover() ? league.getPointsPerWalkover() : league.getPointsPerLoss();
            }
            calculateSets(m);
        }
    }

    private void calculateSets(Match match) {
        MatchResultsSummary resultsSummary = match.calculateResults();
        if (match.getTeam1().getId().equals(this.team.getId())) {
            this.setsWon += resultsSummary.team1SetsWon();
            this.setsLost += resultsSummary.team2SetsWon();
        } else {
            this.setsWon += resultsSummary.team2SetsWon();
            this.setsLost += resultsSummary.team1SetsWon();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Integer getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(Integer matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public Integer getMatchesWon() {
        return matchesWon;
    }

    public void setMatchesWon(Integer matchesWon) {
        this.matchesWon = matchesWon;
    }

    public Integer getSetsWon() {
        return setsWon;
    }

    public void setSetsWon(Integer setsWon) {
        this.setsWon = setsWon;
    }

    public Integer getSetsLost() {
        return setsLost;
    }

    public void setSetsLost(Integer setsLost) {
        this.setsLost = setsLost;
    }

    public Integer getMatchesLost() {
        return matchesLost;
    }

    public void setMatchesLost(Integer matchesLost) {
        this.matchesLost = matchesLost;
    }

    public Integer getLossesByWalkover() {
        return lossesByWalkover;
    }

    public void setLossesByWalkover(Integer lossesByWalkover) {
        this.lossesByWalkover = lossesByWalkover;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

}