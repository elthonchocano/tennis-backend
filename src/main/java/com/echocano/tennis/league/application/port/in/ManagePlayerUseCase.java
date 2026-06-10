package com.echocano.tennis.league.application.port.in;

import java.util.List;

import com.echocano.tennis.league.domain.model.Player;

import io.smallrye.mutiny.Uni;

public interface ManagePlayerUseCase {
    Uni<List<Player>> getAllPlayers();

    Uni<Player> getPlayerById(Long id);

    Uni<Player> createPlayer(Player player);

    Uni<Player> updatePlayer(Long id, Player player);

    Uni<Boolean> deletePlayer(Long id);

    Uni<Player> processGoogleAuthentication(String email, String googleId, String firstName, String lastName,
            String avatarUrl);

    Uni<Player> claimLocalAccount(String invitationCode, String email, String googleId, String avatarUrl);

    Uni<String> getInvitationCodeByPhoneNumber(String phoneNumber);
}