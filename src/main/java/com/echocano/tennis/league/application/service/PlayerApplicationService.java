package com.echocano.tennis.league.application.service;

import java.util.List;

import com.echocano.tennis.league.application.port.in.ManagePlayerUseCase;
import com.echocano.tennis.league.application.port.in.ManageTeamUseCase;
import com.echocano.tennis.league.application.port.out.PlayerRepositoryPort;
import com.echocano.tennis.league.domain.model.Player;
import com.echocano.tennis.league.domain.model.Team;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

public class PlayerApplicationService implements ManagePlayerUseCase {

    private final PlayerRepositoryPort playerRepositoryPort;
    private final ManageTeamUseCase manageTeamUseCase;

    public PlayerApplicationService(PlayerRepositoryPort playerRepositoryPort, ManageTeamUseCase manageTeamUseCase) {
        this.playerRepositoryPort = playerRepositoryPort;
        this.manageTeamUseCase = manageTeamUseCase;
    }

    @Override
    public Uni<List<Player>> getAllPlayers() {
        return playerRepositoryPort.findAll();
    }

    @Override
    public Uni<Player> getPlayerById(Long id) {
        return playerRepositoryPort.findById(id);
    }

    @Override
    public Uni<Player> createPlayer(Player player) {
        player.initializeLocalPlayer();
        return playerRepositoryPort.save(player)
                .onItem().transformToUni(savedPlayer -> {
                    Team singlesTeam = new Team();
                    singlesTeam.setTeamName(savedPlayer.getFirstName() + " " + savedPlayer.getLastName());
                    singlesTeam.setPlayer1(savedPlayer);
                    singlesTeam.setPlayer2(null);
                    return manageTeamUseCase.createTeam(singlesTeam)
                            .onItem().transform(ignored -> savedPlayer);
                });
    }

    @Override
    public Uni<Player> updatePlayer(Long id, Player playerDetails) {
        return playerRepositoryPort.findById(id)
                .onItem().ifNotNull().transformToUni(existingPlayer -> {
                    existingPlayer.setFirstName(playerDetails.getFirstName());
                    existingPlayer.setLastName(playerDetails.getLastName());
                    existingPlayer.setPhoneNumber(playerDetails.getPhoneNumber());
                    existingPlayer.setHand(playerDetails.getHand());
                    return playerRepositoryPort.save(existingPlayer);
                });
    }

    @Override
    public Uni<Boolean> deletePlayer(Long id) {
        return playerRepositoryPort.deleteById(id);
    }

    @Override
    public Uni<Player> processGoogleAuthentication(String email, String googleId, String firstName, String lastName,
            String avatarUrl) {
        return playerRepositoryPort.findByEmail(email)
                .flatMap(optionalPlayer -> {
                    if (optionalPlayer.isPresent()) {
                        Player existingPlayer = optionalPlayer.get();
                        if ("LOCAL".equals(existingPlayer.getOauth2Provider())) {
                            existingPlayer.linkGoogleAccount(googleId, avatarUrl);
                            return playerRepositoryPort.save(existingPlayer);
                        }
                        return Uni.createFrom().item(existingPlayer);
                    } else {
                        Player newPlayer = new Player();
                        newPlayer.setEmail(email);
                        newPlayer.setFirstName(firstName);
                        newPlayer.setLastName(lastName);
                        newPlayer.setOauth2Provider("GOOGLE");
                        newPlayer.setOauth2Id(googleId);
                        newPlayer.setAvatarUrl(avatarUrl);
                        return playerRepositoryPort.save(newPlayer);
                    }
                });
    }

    @Override
    public Uni<Player> claimLocalAccount(String invitationCode, String email, String googleId, String avatarUrl) {
        return playerRepositoryPort.findByInvitationCode(invitationCode)
                .flatMap(optionalPlayer -> {
                    if (optionalPlayer.isEmpty()) {
                        return Uni.createFrom().failure(
                                new IllegalArgumentException("Invalid or already used invitation code."));
                    }
                    Player player = optionalPlayer.get();
                    if (!"LOCAL".equals(player.getOauth2Provider())) {
                        return Uni.createFrom().failure(new IllegalStateException(
                                "ccount already claimed and linked to a different identity provider."));
                    }
                    player.setEmail(email);
                    player.setOauth2Provider("GOOGLE");
                    player.setOauth2Id(googleId);
                    player.setAvatarUrl(avatarUrl);
                    player.setInvitationCode(null);
                    return playerRepositoryPort.save(player);
                });
    }

    @Override
    public Uni<String> getInvitationCodeByPhoneNumber(String phoneNumber) {
        return playerRepositoryPort.findByPhoneNumber(phoneNumber)
                .flatMap(optionalPlayer -> {
                    if (optionalPlayer.isEmpty()) {
                        return Uni.createFrom().failure(
                                new NotFoundException("Phone number not registered."));
                    }
                    Player player = optionalPlayer.get();
                    if (player.getInvitationCode() == null || player.getInvitationCode().isBlank()) {
                        return Uni.createFrom().failure(
                                new BadRequestException("Invitation code already used."));
                    }
                    return Uni.createFrom().item(player.getInvitationCode());
                });
    }
}