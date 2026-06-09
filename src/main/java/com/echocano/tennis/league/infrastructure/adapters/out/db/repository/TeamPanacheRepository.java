package com.echocano.tennis.league.infrastructure.adapters.out.db.repository;

import java.util.List;

import com.echocano.tennis.league.infrastructure.adapters.out.db.entity.TeamEntity;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TeamPanacheRepository implements PanacheRepositoryBase<TeamEntity, Long> {

    public Uni<TeamEntity> findByIdWithPlayers(Long id) {
        return find("from TeamEntity t left join fetch t.player1 left join fetch t.player2 where t.id = ?1", id)
                .firstResult();
    }

    public Uni<List<TeamEntity>> listAllWithPlayers() {
        return list("from TeamEntity t left join fetch t.player1 left join fetch t.player2");
    }
}