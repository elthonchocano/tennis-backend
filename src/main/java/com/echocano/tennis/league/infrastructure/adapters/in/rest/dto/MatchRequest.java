package com.echocano.tennis.league.infrastructure.adapters.in.rest.dto;

import java.time.LocalDate;
import java.util.List;

public record MatchRequest(
    Long leagueId,
    Long team1Id,
    Long team2Id,
    String matchType,
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate matchDate,
    List<SetResultDto> sets,
    boolean walkover
) {}