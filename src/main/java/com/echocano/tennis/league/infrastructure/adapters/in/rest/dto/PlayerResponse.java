package com.echocano.tennis.league.infrastructure.adapters.in.rest.dto;

public record PlayerResponse(
    Long id,
    String firstName,
    String lastName,
    String phoneNumber,
    String hand
) {}