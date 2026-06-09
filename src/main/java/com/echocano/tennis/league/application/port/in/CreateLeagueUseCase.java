package com.echocano.tennis.league.application.port.in;

import com.echocano.tennis.league.domain.model.League;
import io.smallrye.mutiny.Uni;

public interface CreateLeagueUseCase {
    Uni<League> execute(League league);
}
