package com.echocano.tennis.league.infrastructure.adapters.in.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record LeagueParticipantRowRequest(
		@NotBlank(message = "Team is required.") Long teamId) {
}