package com.echocano.tennis.league.application.port.out;

import com.echocano.tennis.league.domain.model.Player;
import io.smallrye.mutiny.Uni;
import java.util.List;

public interface PlayerRepositoryPort {
    Uni<List<Player>> findAll();
    Uni<Player> findById(Long id);
    Uni<Player> save(Player player);
    Uni<Boolean> deleteById(Long id);
}