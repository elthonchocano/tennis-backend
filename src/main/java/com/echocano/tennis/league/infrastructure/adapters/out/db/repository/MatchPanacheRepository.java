package com.echocano.tennis.league.infrastructure.adapters.out.db.repository;

import java.util.List;

import com.echocano.tennis.league.infrastructure.adapters.out.db.entity.MatchEntity;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MatchPanacheRepository implements PanacheRepositoryBase<MatchEntity, Long> {

    public Uni<MatchEntity> findByIdWithPlayersAndSets(Long id) {
        return find(""
                + "SELECT m FROM MatchEntity m "
                + "LEFT JOIN FETCH m.league l "
                + "LEFT JOIN FETCH m.sets s "
                + "LEFT JOIN FETCH m.team1 t1 "
                + "LEFT JOIN FETCH t1.player1 p11 "
                + "LEFT JOIN FETCH t1.player2 p12 "
                + "LEFT JOIN FETCH m.team2 t2 "
                + "LEFT JOIN FETCH t2.player1 p21 "
                + "LEFT JOIN FETCH t2.player2 p22 "
                + "WHERE m.id = ?1", id)
                .firstResult();
    }

    public Uni<List<MatchEntity>> findByLeagueAndTeam(Long leagueId, Long teamId) {
        return list(
                "SELECT m FROM MatchEntity m "
                        + "LEFT JOIN FETCH m.league l "
                        + "LEFT JOIN FETCH m.sets s "
                        + "LEFT JOIN FETCH m.team1 t1 "
                        + "LEFT JOIN FETCH t1.player1 p11 "
                        + "LEFT JOIN FETCH t1.player2 p12 "
                        + "LEFT JOIN FETCH m.team2 t2 "
                        + "LEFT JOIN FETCH t2.player1 p21 "
                        + "LEFT JOIN FETCH t2.player2 p22 "
                        + "WHERE (m.team1.id = ?1 OR m.team2.id = ?1) AND m.league.id = ?2 "
                        + "ORDER BY m.matchDate ASC",
                teamId, leagueId);
    }
}
