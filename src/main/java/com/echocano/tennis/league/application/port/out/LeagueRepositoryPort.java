package com.echocano.tennis.league.application.port.out;

import java.util.List;

import com.echocano.tennis.league.domain.model.League;
import io.smallrye.mutiny.Uni;

public interface LeagueRepositoryPort {
    Uni<League> save(League league);
    Uni<League> findById(Long id);
    Uni<Boolean> existsByNameAndSeason(String name, String season);
    Uni<List<League>> findAll(int page, int pageSize);
}
