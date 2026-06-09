package com.echocano.tennis.league.application.port.out;

import com.echocano.tennis.league.domain.model.LeagueParticipant;
import io.smallrye.mutiny.Uni;
import java.util.List;

public interface LeagueParticipantRepositoryPort {
    Uni<LeagueParticipant> save(LeagueParticipant participant);
    Uni<LeagueParticipant> findByLeagueAndTeam(Long leagueId, Long teamId);
    Uni<List<LeagueParticipant>> findLeaderboardByLeague(Long leagueId);
    Uni<List<LeagueParticipant>> findLeaderboardByLeagueWithPagination(Long leagueId, int page, int pageSize);
}