package com.echocano.tennis.league.infrastructure.adapters.in.rest.dto;

import com.echocano.tennis.league.domain.model.MatchType;
import java.time.LocalDate;
import java.util.List;

public record MatchResponse(
    Long id,
    Long leagueId,
    TeamResponse team1,
    TeamResponse team2,
    TeamResponse winner,
    MatchType matchType,
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate matchDate,
    List<SetResultDto> sets
) {}