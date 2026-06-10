package com.echocano.tennis.league.application.port.out;

import java.util.List;
import java.util.Optional;

import com.echocano.tennis.league.domain.model.Player;

import io.smallrye.mutiny.Uni;

public interface PlayerRepositoryPort {
    Uni<List<Player>> findAll();

    Uni<Player> findById(Long id);

    Uni<Player> save(Player player);

    Uni<Boolean> deleteById(Long id);

    Uni<Optional<Player>> findByEmail(String email);

    Uni<Optional<Player>> findByInvitationCode(String code);

    Uni<Optional<Player>> findByPhoneNumber(String phoneNumber);
}