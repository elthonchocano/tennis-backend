package com.echocano.tennis.league.application.service;

import com.echocano.tennis.league.application.port.in.ManagePlayerUseCase;
import com.echocano.tennis.league.application.port.in.ManageTeamUseCase;
import com.echocano.tennis.league.application.port.out.PlayerRepositoryPort;
import com.echocano.tennis.league.domain.model.Player;
import com.echocano.tennis.league.domain.model.Team;

import io.smallrye.mutiny.Uni;
import java.util.List;

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
}