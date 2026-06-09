package com.echocano.tennis.league.application.service;

import java.util.List;

import org.jboss.logging.Logger;

import com.echocano.tennis.league.application.port.in.AddParticipantUseCase;
import com.echocano.tennis.league.application.port.in.CreateLeagueUseCase;
import com.echocano.tennis.league.application.port.in.ViewLeaderboardUseCase;
import com.echocano.tennis.league.application.port.in.ViewLeagueUseCase;
import com.echocano.tennis.league.application.port.out.LeagueParticipantRepositoryPort;
import com.echocano.tennis.league.application.port.out.LeagueRepositoryPort;
import com.echocano.tennis.league.application.port.out.TeamRepositoryPort;
import com.echocano.tennis.league.domain.model.League;
import com.echocano.tennis.league.domain.model.LeagueParticipant;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.BadRequestException;

public class LeagueApplicationService
        implements CreateLeagueUseCase, AddParticipantUseCase, ViewLeaderboardUseCase, ViewLeagueUseCase {

    private final LeagueRepositoryPort leagueRepository;
    private final LeagueParticipantRepositoryPort participantRepository;
    private final TeamRepositoryPort teamRepository;

    private static final Logger LOG = Logger.getLogger(LeagueApplicationService.class);

    public LeagueApplicationService(LeagueRepositoryPort leagueRepository,
            LeagueParticipantRepositoryPort participantRepository,
            TeamRepositoryPort teamRepository) {
        this.leagueRepository = leagueRepository;
        this.participantRepository = participantRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public Uni<League> execute(League league) {
        LOG.infof("Saving league", league);
        return leagueRepository.existsByNameAndSeason(league.getName(), league.getSeason())
                .flatMap(exists -> {
                    if (exists) {
                        return Uni.createFrom().failure(new BadRequestException(
                                String.format(
                                        "A league named '%s' is already registered for the '%s' season.",
                                        league.getName(), league.getSeason())));
                    }
                    return leagueRepository.save(league);
                });
    }

    @Override
    public Uni<LeagueParticipant> execute(Long leagueId, Long teamId) {
        return leagueRepository.findById(leagueId)
                .onItem().ifNull().failWith(new IllegalArgumentException("The specified league was not found."))
                .onItem().transformToUni(league -> teamRepository.findById(teamId)
                        .onItem().ifNull().failWith(new IllegalArgumentException("The specified team was not found."))
                        .onItem().transformToUni(team -> {
                            LeagueParticipant participant = new LeagueParticipant();
                            participant.setLeague(league);
                            participant.setTeam(team);
                            participant.setMatchesPlayed(0);
                            participant.setMatchesWon(0);
                            participant.setSetsWon(0);
                            participant.setSetsLost(0);
                            return participantRepository.save(participant);
                        }));
    }

    @Override
    public Uni<List<LeagueParticipant>> getLeaderboard(Long leagueId) {
        return participantRepository.findLeaderboardByLeague(leagueId);
    }

    @Override
    public Uni<List<LeagueParticipant>> getLeaderboardWithPagination(Long leagueId, int page, int pageSize) {
        return participantRepository.findLeaderboardByLeagueWithPagination(leagueId, page, pageSize);
    }

    @Override
    public Uni<List<League>> getLeagues(int page, int pageSize) {
        return leagueRepository.findAll(page, pageSize);
    }
}