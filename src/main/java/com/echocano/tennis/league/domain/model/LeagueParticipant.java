package com.echocano.tennis.league.domain.model;

public class LeagueParticipant {
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

    public void updateStatsAsWinner(int setsWon, int setsLost, int pointsToSum) {
        this.matchesPlayed += 1;
        this.matchesWon += 1;
        this.setsWon += setsWon;
        this.setsLost += setsLost;
        this.points += pointsToSum;
    }

    public void updateStatsAsLoser(int setsWon, int setsLost, boolean byWalkover, int pointsToSum) {
        this.matchesPlayed += 1;
        this.matchesLost += 1;
        this.setsWon += setsWon;
        this.setsLost += setsLost;
        this.points += pointsToSum;

        if (byWalkover) {
            this.lossesByWalkover += 1;
        }
    }

    public void revertStatsAsWinner(int setsWon, int setsLost, int points) {
        this.matchesPlayed--;
        this.matchesWon--;
        this.setsWon -= setsWon;
        this.setsLost -= setsLost;
        this.points -= points;
    }

    public void revertStatsAsLoser(int setsWon, int setsLost, boolean wasWalkover, int points) {
        this.matchesPlayed--;
        this.matchesLost--;
        this.setsWon -= setsWon;
        this.setsLost -= setsLost;
        if (wasWalkover) {
            this.lossesByWalkover--;
        }
        this.points -= points;
    }
}