package com.echocano.tennis.league.infrastructure.config;

import com.echocano.tennis.league.application.port.in.ManagePlayerUseCase;
import com.echocano.tennis.league.application.port.in.ManageTeamUseCase;
import com.echocano.tennis.league.application.port.out.LeagueParticipantRepositoryPort;
import com.echocano.tennis.league.application.port.out.LeagueRepositoryPort;
import com.echocano.tennis.league.application.port.out.MatchRepositoryPort;
import com.echocano.tennis.league.application.port.out.PlayerRepositoryPort;
import com.echocano.tennis.league.application.port.out.TeamRepositoryPort;
import com.echocano.tennis.league.application.service.LeagueApplicationService;
import com.echocano.tennis.league.application.service.MatchApplicationService;
import com.echocano.tennis.league.application.service.PlayerApplicationService;
import com.echocano.tennis.league.application.service.TeamApplicationService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class ServiceProducer {

    @Produces
    @ApplicationScoped
    public LeagueApplicationService leagueApplicationService(
            LeagueRepositoryPort leagueRepository,
            LeagueParticipantRepositoryPort participantRepository,
            TeamRepositoryPort teamRepository) {
        return new LeagueApplicationService(leagueRepository, participantRepository, teamRepository);
    }

    @Produces
    @ApplicationScoped
    public MatchApplicationService matchApplicationService(
            MatchRepositoryPort matchRepository,
            LeagueParticipantRepositoryPort participantRepository,
            TeamRepositoryPort teamRepository) {
        return new MatchApplicationService(matchRepository, participantRepository);
    }

    @Produces
    @ApplicationScoped
    public ManagePlayerUseCase playerApplicationService(PlayerRepositoryPort playerRepositoryPort,
            ManageTeamUseCase manageTeamUseCase) {
        return new PlayerApplicationService(playerRepositoryPort, manageTeamUseCase);
    }

    @Produces
    @ApplicationScoped
    public ManageTeamUseCase teamApplicationService(TeamRepositoryPort teamRepositoryPort) {
        return new TeamApplicationService(teamRepositoryPort);
    }
}