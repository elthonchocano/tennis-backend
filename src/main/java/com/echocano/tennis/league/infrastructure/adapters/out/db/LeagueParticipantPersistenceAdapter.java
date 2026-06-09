package com.echocano.tennis.league.infrastructure.adapters.out.db;

import java.util.List;

import com.echocano.tennis.league.application.port.out.LeagueParticipantRepositoryPort;
import com.echocano.tennis.league.domain.model.LeagueParticipant;
import com.echocano.tennis.league.infrastructure.adapters.out.db.entity.LeagueEntity;
import com.echocano.tennis.league.infrastructure.adapters.out.db.entity.LeagueParticipantEntity;
import com.echocano.tennis.league.infrastructure.adapters.out.db.entity.TeamEntity;
import com.echocano.tennis.league.infrastructure.adapters.out.db.mapper.DbLeagueMapper;
import com.echocano.tennis.league.infrastructure.adapters.out.db.repository.LeagueParticipantPanacheRepository;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LeagueParticipantPersistenceAdapter implements LeagueParticipantRepositoryPort {

    private final LeagueParticipantPanacheRepository repository;
    private final DbLeagueMapper mapper;

    public LeagueParticipantPersistenceAdapter(LeagueParticipantPanacheRepository repository, DbLeagueMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Uni<LeagueParticipant> save(LeagueParticipant participant) {
        LeagueParticipantEntity entity = mapper.toEntity(participant);
        return repository.getSession()
                .flatMap(session -> {
                    LeagueEntity leagueProxy = session.getReference(LeagueEntity.class,
                            participant.getLeague().getId());
                    TeamEntity teamProxy = session.getReference(TeamEntity.class, participant.getTeam().getId());
                    entity.setLeague(leagueProxy);
                    entity.setTeam(teamProxy);
                    if (entity.getId() == null) {
                        return session.persist(entity)
                                .replaceWith(entity);
                    } else {
                        return session.merge(entity);
                    }
                })
                .map(persistedEntity -> mapper.toDomain(persistedEntity));
    }

    @Override
    public Uni<LeagueParticipant> findByLeagueAndTeam(Long leagueId, Long teamId) {
        return repository.find("league.id = ?1 and team.id = ?2", leagueId, teamId)
                .firstResult()
                .map(entity -> entity != null ? mapper.toDomain(entity) : null);
    }

    @Override
    public Uni<List<LeagueParticipant>> findLeaderboardByLeague(Long leagueId) {
        return repository.findLeaderboardByLeagueId(leagueId)
                .map(entities -> entities.stream()
                        .map(mapper::toDomain)
                        .toList());
    }

    @Override
    public Uni<List<LeagueParticipant>> findLeaderboardByLeagueWithPagination(Long leagueId, int page, int pageSize) {
        return repository.findLeaderboardByLeagueIdWithPagination(leagueId, page, pageSize)
                .map(entities -> entities.stream()
                        .map(mapper::toDomain)
                        .toList());
    }
}
