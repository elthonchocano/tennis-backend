package com.echocano.tennis.league.application.port.out;

import java.util.List;

import com.echocano.tennis.league.domain.model.Match;
import io.smallrye.mutiny.Uni;

public interface MatchRepositoryPort {
    
    Uni<Match> save(Match match);
    Uni<Match> findById(Long id);
    Uni<List<Match>> findAllMatches(Long leagueId, Long teamId);
}
