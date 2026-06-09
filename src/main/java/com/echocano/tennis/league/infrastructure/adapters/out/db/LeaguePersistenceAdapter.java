package com.echocano.tennis.league.infrastructure.adapters.out.db;

import java.util.List;

import com.echocano.tennis.league.application.port.out.LeagueRepositoryPort;
import com.echocano.tennis.league.domain.model.League;
import com.echocano.tennis.league.infrastructure.adapters.out.db.entity.LeagueEntity;
import com.echocano.tennis.league.infrastructure.adapters.out.db.mapper.DbLeagueMapper;
import com.echocano.tennis.league.infrastructure.adapters.out.db.repository.LeaguePanacheRepository;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LeaguePersistenceAdapter implements LeagueRepositoryPort {

    private final LeaguePanacheRepository repository;
    private final DbLeagueMapper mapper;

    public LeaguePersistenceAdapter(LeaguePanacheRepository repository, DbLeagueMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Uni<League> save(League league) {
        LeagueEntity entity = mapper.toEntity(league);
        return repository.getSession()
                .flatMap(session -> session.merge(entity))
                .call(managedEntity -> repository.getSession().flatMap(session -> session.flush()))
                .flatMap((LeagueEntity managedEntity) -> repository.findById(managedEntity.getId()))
                .map(mapper::toDomain);
    }

    @Override
    public Uni<League> findById(Long id) {
        return repository.findById(id)
                .map(entity -> entity != null ? mapper.toDomain(entity) : null);
    }

    @Override
    public Uni<Boolean> existsByNameAndSeason(String name, String season) {
        return repository.existsByNameAndSeason(name, season);
    }

    @Override
    public Uni<List<League>> findAll(int page, int pageSize) {
        return repository.findAllWithPagination(page, pageSize)
                .map(entities -> entities.stream()
                        .map(mapper::toDomain)
                        .toList());
    }
}