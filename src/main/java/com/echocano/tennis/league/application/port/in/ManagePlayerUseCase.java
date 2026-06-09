package com.echocano.tennis.league.application.port.in;

import com.echocano.tennis.league.domain.model.Player;
import io.smallrye.mutiny.Uni;
import java.util.List;

public interface ManagePlayerUseCase {
    Uni<List<Player>> getAllPlayers();
    Uni<Player> getPlayerById(Long id);
    Uni<Player> createPlayer(Player player);
    Uni<Player> updatePlayer(Long id, Player player);
    Uni<Boolean> deletePlayer(Long id);
}