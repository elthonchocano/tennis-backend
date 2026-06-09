package com.echocano.tennis.league.application.port.in;

import com.echocano.tennis.league.domain.model.Team;
import io.smallrye.mutiny.Uni;
import java.util.List;

public interface ManageTeamUseCase {
    Uni<List<Team>> getAllTeams();
    Uni<Team> getTeamById(Long id);
    Uni<Team> createTeam(Team team);
    Uni<Team> updateTeam(Long id, Team team);
    Uni<Boolean> deleteTeam(Long id);
}