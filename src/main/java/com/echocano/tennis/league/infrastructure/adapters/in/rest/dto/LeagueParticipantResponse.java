package com.echocano.tennis.league.infrastructure.adapters.in.rest.dto;

public record LeagueParticipantResponse(
    Long id,
    TeamResponse team,
    Integer matchesPlayed,
    Integer matchesWon,
    Integer matchesLost,
    Integer lossesByWalkover,
    Integer setsWon,
    Integer setsLost,
    Integer points
) {}