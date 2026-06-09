package com.echocano.tennis.league.application.port.in;

import java.util.List;

import com.echocano.tennis.league.domain.model.Match;

import io.smallrye.mutiny.Uni;

public interface ViewMatchUseCase {

    Uni<List<Match>> getAllMatch(Long leagueId, Long teamId);
}
