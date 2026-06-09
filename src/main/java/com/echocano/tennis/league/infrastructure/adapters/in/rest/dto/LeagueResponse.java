package com.echocano.tennis.league.infrastructure.adapters.in.rest.dto;

public record LeagueResponse(
    Long id,
    String name,
    String season,
    Integer pointsPerWin,
    Integer pointsPerLoss,
    Integer pointsPerWalkover
) {
}
