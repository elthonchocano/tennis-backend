package com.echocano.tennis.league.domain.model;

public record MatchResultsSummary(
    Team winner,
    Team loser,
    int team1SetsWon,
    int team2SetsWon,
    boolean isWalkover
) {}