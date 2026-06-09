package com.echocano.tennis.league.infrastructure.adapters.in.rest.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record MatchResultRequest(
        @NotNull(message = "The set list cannot be null.") @Valid List<SetResultDto> sets,

        boolean walkover,

        Long walkoverWinnerId) {
    public MatchResultRequest {
        if (sets == null) {
            sets = List.of();
        }
        if (walkover && walkoverWinnerId == null) {
            throw new IllegalArgumentException(
                    "You must specify the walkoverWinnerId if the match is marked as a walkover.");
        }
        if (walkover && !sets.isEmpty()) {
            throw new IllegalArgumentException("A match won by walkover cannot have any recorded sets.");
        }
    }
}