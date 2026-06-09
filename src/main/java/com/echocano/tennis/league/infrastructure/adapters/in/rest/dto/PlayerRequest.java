package com.echocano.tennis.league.infrastructure.adapters.in.rest.dto;

public record PlayerRequest(
    String firstName,
    String lastName,
    String phoneNumber,
    String hand
) {}