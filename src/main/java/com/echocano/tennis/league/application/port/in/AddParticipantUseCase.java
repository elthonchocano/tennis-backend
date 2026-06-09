package com.echocano.tennis.league.application.port.in;

import com.echocano.tennis.league.domain.model.LeagueParticipant;
import io.smallrye.mutiny.Uni;

public interface AddParticipantUseCase {
    Uni<LeagueParticipant> execute(Long leagueId, Long teamId);
}