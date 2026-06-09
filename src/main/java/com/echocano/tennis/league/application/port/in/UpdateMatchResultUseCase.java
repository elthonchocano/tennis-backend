package com.echocano.tennis.league.application.port.in;

import com.echocano.tennis.league.domain.model.Match;
import com.echocano.tennis.league.domain.model.SetResult;
import java.util.List;
import io.smallrye.mutiny.Uni;

public interface UpdateMatchResultUseCase {
    Uni<Match> execute(Long matchId, List<SetResult> sets, boolean isWalkover, Long walkoverWinnerId);
}