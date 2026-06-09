package com.echocano.tennis.league.domain.model;

import java.time.LocalDate;
import java.util.List;

public class Match {
    private Long id;
    private Team team1;
    private Team team2;
    private Team winner;
    private boolean walkover = false;
    private LocalDate matchDate;
    private MatchType matchType;
    private List<SetResult> sets;
    private League league;

    public void validate() {
        if (team1 == null || team2 == null) {
            throw new IllegalArgumentException("Both teams are required.");
        }
        if (team1.getId().equals(team2.getId())) {
            throw new IllegalArgumentException("A team cannot play against itself.");
        }
        if (league == null) {
            throw new IllegalArgumentException("The match must be linked to a league.");
        }
        if (matchType == null) {
            throw new IllegalArgumentException("The match type (matchType) is required.");
        }
        if (matchDate == null) {
            throw new IllegalArgumentException("The match date (matchDate) is required.");
        }

        if (sets != null && !sets.isEmpty()) {
            int maximosSetsPermitidos = matchType.getMaxSets();
            if (sets.size() > maximosSetsPermitidos) {
                throw new IllegalArgumentException("Number of sets exceeded for this match format " + matchType);
            }

            int setsGanadosTeam1 = 0;
            int setsGanadosTeam2 = 0;
            int setsNecesariosParaGanar = (maximosSetsPermitidos == 3) ? 2 : 3;

            for (SetResult set : sets) {
                if (set.getTeam1Games() > set.getTeam2Games()) {
                    setsGanadosTeam1++;
                } else if (set.getTeam2Games() > set.getTeam1Games()) {
                    setsGanadosTeam2++;
                } else {
                    throw new IllegalArgumentException("A set cannot end in a tie..");
                }
            }

            if (setsGanadosTeam1 > setsNecesariosParaGanar || setsGanadosTeam2 > setsNecesariosParaGanar) {
                throw new IllegalArgumentException("The submitted set is redundant. The match had already ended.");
            }
        }
    }

    public void validateSetsAmount() {
        if (this.sets == null) {
            throw new IllegalArgumentException("The set list cannot be null.");
        }

        if (this.matchType == null) {
            throw new IllegalStateException("The match type (matchType) is undefined.");
        }

        int totalSets = this.sets.size();
        int minAllowed = this.matchType.getMinSets();
        int maxAllowed = this.matchType.getMaxSets();

        if (totalSets < minAllowed || totalSets > maxAllowed) {
            throw new IllegalArgumentException(
                    String.format(
                            "For a %s match, the number of sets must be between %d and %d. Received: %d",
                            this.matchType.name(), minAllowed, maxAllowed, totalSets));
        }
    }

    public Long getId() {
        return id;
    }

    public Team getTeam1() {
        return team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public Team getWinner() {
        return winner;
    }

    public LocalDate getMatchDate() {
        return matchDate;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public List<SetResult> getSets() {
        return sets;
    }

    public League getLeague() {
        return league;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setWinner(Team winner) {
        this.winner = winner;
    }

    public void setSets(List<SetResult> sets) {
        this.sets = sets;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }

    public void setMatchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
    }

    public boolean isWalkover() {
        return walkover;
    }

    public void setWalkover(boolean walkover) {
        this.walkover = walkover;
    }

    public MatchResultsSummary calculateResults() {
        Team calculatedWinner;
        Team calculatedLoser;
        int t1SetsWon = 0;
        int t2SetsWon = 0;
        if (this.walkover) {
            calculatedWinner = this.winner;
            calculatedLoser = this.winner.equals(team1) ? team2 : team1;

            if (calculatedWinner.equals(team1)) {
                t1SetsWon = this.matchType.getMinSets();
            } else {
                t2SetsWon = this.matchType.getMinSets();
            }
            return new MatchResultsSummary(calculatedWinner, calculatedLoser, t1SetsWon, t2SetsWon, true);
        }
        for (SetResult set : this.sets) {
            if (set.getTeam1Games() > set.getTeam2Games()) {
                t1SetsWon++;
            } else if (set.getTeam2Games() > set.getTeam1Games()) {
                t2SetsWon++;
            } else if (set.getTeam1Games() == 6 && set.getTeam2Games() == 6) {
                int p1 = set.getTeam1TieBreakPoints() != null ? set.getTeam1TieBreakPoints() : 0;
                int p2 = set.getTeam2TieBreakPoints() != null ? set.getTeam2TieBreakPoints() : 0;
                if (p1 > p2) {
                    t1SetsWon++;
                } else if (p2 > p1) {
                    t2SetsWon++;
                } else {
                    throw new IllegalStateException("Set " + set.getSetNumber()
                            + " tied at 6 requires a winner in the tie-break points.");
                }
            }
        }
        if (t1SetsWon > t2SetsWon) {
            calculatedWinner = team1;
            calculatedLoser = team2;
        } else {
            calculatedWinner = team2;
            calculatedLoser = team1;
        }
        this.winner = calculatedWinner;
        return new MatchResultsSummary(calculatedWinner, calculatedLoser, t1SetsWon, t2SetsWon, false);
    }
}