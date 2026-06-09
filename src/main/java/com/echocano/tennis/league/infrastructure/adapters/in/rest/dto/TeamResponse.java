package com.echocano.tennis.league.infrastructure.adapters.in.rest.dto;

public record TeamResponse(
    Long id,
    String teamName,
    PlayerResponse player1,
    PlayerResponse player2
) {}