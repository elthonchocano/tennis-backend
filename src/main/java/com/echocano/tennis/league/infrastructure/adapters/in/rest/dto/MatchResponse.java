package com.echocano.tennis.league.infrastructure.adapters.in.rest.dto;

import java.time.LocalDate;
import java.util.List;

import com.echocano.tennis.league.domain.model.MatchType;

public record MatchResponse(
        Long id,
        Long leagueId,
        TeamResponse team1,
        TeamResponse team2,
        TeamResponse winner,
        MatchType matchType,
        @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd") LocalDate matchDate,
        boolean walkover,
        List<SetResultDto> sets) {
}