package com.echocano.tennis.league.infrastructure.adapters.in.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record LeagueRequest(

        @NotBlank(message = "League name is required.") String name,

        @NotBlank(message = "The league season (season) is required.") String season,

        Integer pointsPerWin,
        Integer pointsPerLoss,
        Integer pointsPerWalkover) {

}
