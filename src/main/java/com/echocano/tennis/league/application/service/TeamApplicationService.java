package com.echocano.tennis.league.application.service;

import com.echocano.tennis.league.application.port.in.ManageTeamUseCase;
import com.echocano.tennis.league.application.port.out.TeamRepositoryPort;
import com.echocano.tennis.league.domain.model.Team;
import io.smallrye.mutiny.Uni;
import java.util.List;

public class TeamApplicationService implements ManageTeamUseCase {

    private final TeamRepositoryPort teamRepositoryPort;

    public TeamApplicationService(TeamRepositoryPort teamRepositoryPort) {
        this.teamRepositoryPort = teamRepositoryPort;
    }

    @Override
    public Uni<List<Team>> getAllTeams() {
        return teamRepositoryPort.findAllWithPlayers();
    }

    @Override
    public Uni<Team> getTeamById(Long id) {
        return teamRepositoryPort.findById(id);
    }

    @Override
    public Uni<Team> createTeam(Team team) {
        return teamRepositoryPort.save(team);
    }

    @Override
    public Uni<Team> updateTeam(Long id, Team teamDetails) {
        return teamRepositoryPort.findById(id)
                .onItem().ifNotNull().transformToUni(existingTeam -> {
                    existingTeam.setTeamName(teamDetails.getTeamName());
                    existingTeam.setPlayer1(teamDetails.getPlayer1());
                    existingTeam.setPlayer2(teamDetails.getPlayer2());
                    return teamRepositoryPort.save(existingTeam);
                });
    }

    @Override
    public Uni<Boolean> deleteTeam(Long id) {
        return teamRepositoryPort.deleteById(id);
    }
}