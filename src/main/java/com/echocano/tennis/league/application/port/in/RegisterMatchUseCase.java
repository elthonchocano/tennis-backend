package com.echocano.tennis.league.application.port.in;

import com.echocano.tennis.league.domain.model.Match;
import io.smallrye.mutiny.Uni;

public interface RegisterMatchUseCase {
    Uni<Match> execute(Match match);
}
