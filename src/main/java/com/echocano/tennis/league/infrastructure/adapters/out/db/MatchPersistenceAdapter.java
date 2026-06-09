package com.echocano.tennis.league.infrastructure.adapters.out.db;

import java.util.List;

import com.echocano.tennis.league.application.port.out.MatchRepositoryPort;
import com.echocano.tennis.league.domain.model.Match;
import com.echocano.tennis.league.infrastructure.adapters.out.db.entity.MatchEntity;
import com.echocano.tennis.league.infrastructure.adapters.out.db.mapper.DbMatchMapper;
import com.echocano.tennis.league.infrastructure.adapters.out.db.repository.MatchPanacheRepository;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MatchPersistenceAdapter implements MatchRepositoryPort {

    private final MatchPanacheRepository repository;
    private final DbMatchMapper mapper;

    public MatchPersistenceAdapter(MatchPanacheRepository repository, DbMatchMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Uni<Match> save(Match match) {
        MatchEntity entity = mapper.toEntity(match);
        return repository.getSession()
                .flatMap(session -> session.merge(entity))
                .call(managedEntity -> repository.getSession().flatMap(session -> session.flush()))
                .flatMap((MatchEntity managedEntity) -> repository.findByIdWithPlayersAndSets(managedEntity.getId()))
                .map(fullEntity -> mapper.toDomain(fullEntity));
    }

    @Override
    public Uni<Match> findById(Long id) {
        return repository.findByIdWithPlayersAndSets(id)
                .map(entity -> entity != null ? mapper.toDomain(entity) : null);
    }

    @Override
    public Uni<List<Match>> findAllMatches(Long leagueId, Long teamId) {
        return repository.findByLeagueAndTeam(leagueId, teamId)
                .map(entities -> entities != null ? mapper.toDomainList(entities) : null);
    }
}