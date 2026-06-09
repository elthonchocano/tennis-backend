package com.echocano.tennis.league.infrastructure.adapters.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

public record SetResultDto(
        Integer setNumber,
        Integer team1Games,
        Integer team2Games,
        @JsonSetter(nulls = Nulls.AS_EMPTY) Integer team1TieBreakPoints,
        @JsonSetter(nulls = Nulls.AS_EMPTY) Integer team2TieBreakPoints) {
    public SetResultDto {
        if (setNumber == null || setNumber < 1) {
            throw new IllegalArgumentException("The set number must be greater than 0.");
        }
        if (team1Games == null || team1Games < 0 || team2Games == null || team2Games < 0) {
            throw new IllegalArgumentException("Team games cannot be negative or null.");
        }
        // Si hay puntos de tie-break, no deberían ser negativos
        if ((team1TieBreakPoints != null && team1TieBreakPoints < 0) ||
                (team2TieBreakPoints != null && team2TieBreakPoints < 0)) {
            throw new IllegalArgumentException("Tie-break points cannot be negative.");
        }
    }
}