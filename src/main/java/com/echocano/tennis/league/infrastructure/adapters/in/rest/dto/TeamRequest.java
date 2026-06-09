package com.echocano.tennis.league.infrastructure.adapters.in.rest.dto;

public record TeamRequest(
    String teamName,
    Long player1Id,
    Long player2Id
) {}