package com.echocano.tennis.league.application.port.in;

import com.echocano.tennis.league.domain.model.LeagueParticipant;
import io.smallrye.mutiny.Uni;
import java.util.List;

public interface ViewLeaderboardUseCase {
    Uni<List<LeagueParticipant>> getLeaderboard(Long leagueId);
    Uni<List<LeagueParticipant>> getLeaderboardWithPagination(Long leagueId, int page, int pageSize);
}
