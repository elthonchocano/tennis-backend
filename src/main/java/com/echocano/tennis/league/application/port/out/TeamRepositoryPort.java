package com.echocano.tennis.league.application.port.out;

import java.util.List;

import com.echocano.tennis.league.domain.model.Team;
import io.smallrye.mutiny.Uni;

public interface TeamRepositoryPort {
    Uni<Team> findById(Long id);
    Uni<List<Team>> findAllWithPlayers();
    Uni<Team> save(Team team);
    Uni<Boolean> deleteById(Long id);
}