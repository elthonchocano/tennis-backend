package com.echocano.tennis.league.infrastructure.adapters.out.db.repository;

import java.util.List;

import com.echocano.tennis.league.infrastructure.adapters.out.db.entity.LeagueParticipantEntity;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LeagueParticipantPanacheRepository implements PanacheRepositoryBase<LeagueParticipantEntity, Long> {

	public Uni<List<LeagueParticipantEntity>> findLeaderboardByLeagueId(Long leagueId) {
		return list(
				"FROM LeagueParticipantEntity lp " +
						"LEFT JOIN FETCH lp.team t " +
						"LEFT JOIN FETCH t.player1 p1 " +
						"LEFT JOIN FETCH t.player2 p2 " +
						"WHERE lp.league.id = ?1 " +
						"ORDER BY lp.points DESC, lp.setsWon DESC",
				leagueId);
	}

	public Uni<List<LeagueParticipantEntity>> findLeaderboardByLeagueIdWithPagination(Long leagueId, int page,
			int pageSize) {
		return find(
				"FROM LeagueParticipantEntity lp " +
						"LEFT JOIN FETCH lp.team t " +
						"LEFT JOIN FETCH t.player1 p1 " +
						"LEFT JOIN FETCH t.player2 p2 " +
						"WHERE lp.league.id = ?1 " +
						"ORDER BY lp.points DESC, (lp.setsWon - lp.setsLost) DESC",
				leagueId)
				.page(page, pageSize)
				.list();
	}
}