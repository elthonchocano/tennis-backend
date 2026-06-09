package com.echocano.tennis.league.application.port.in;

import java.util.List;

import com.echocano.tennis.league.domain.model.League;

import io.smallrye.mutiny.Uni;

public interface ViewLeagueUseCase {

    Uni<List<League>> getLeagues(int page, int pageSize);
}
