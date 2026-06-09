package com.echocano.tennis.league.infrastructure.adapters.out.db;

import java.util.List;

import com.echocano.tennis.league.application.port.out.TeamRepositoryPort;
import com.echocano.tennis.league.domain.model.Team;
import com.echocano.tennis.league.infrastructure.adapters.out.db.entity.PlayerEntity;
import com.echocano.tennis.league.infrastructure.adapters.out.db.entity.TeamEntity;
import com.echocano.tennis.league.infrastructure.adapters.out.db.mapper.DbLeagueMapper;
import com.echocano.tennis.league.infrastructure.adapters.out.db.repository.TeamPanacheRepository;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TeamPersistenceAdapter implements TeamRepositoryPort {

    private final TeamPanacheRepository repository;
    private final DbLeagueMapper mapper;

    public TeamPersistenceAdapter(TeamPanacheRepository repository, DbLeagueMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Uni<Team> findById(Long id) {
        return repository.findByIdWithPlayers(id)
                .map(entity -> entity != null ? mapper.toDomain(entity) : null);
    }

    @Override
    public Uni<List<Team>> findAllWithPlayers() {
        return repository.listAllWithPlayers()
                .map(entities -> entities.stream().map(mapper::toDomain).toList());
    }

    @Override
    public Uni<Team> save(Team team) {
        TeamEntity entity = mapper.toEntity(team);
        return repository.getSession().flatMap(session -> {
            if (entity.getPlayer1() != null && entity.getPlayer1().getId() != null) {
                entity.setPlayer1(session.getReference(PlayerEntity.class, entity.getPlayer1().getId()));
            }
            if (entity.getPlayer2() != null && entity.getPlayer2().getId() != null) {
                entity.setPlayer2(session.getReference(PlayerEntity.class, entity.getPlayer2().getId()));
            } else {
                entity.setPlayer2(null);
            }
            Uni<TeamEntity> saveUni;
            if (entity.getId() == null) {
                saveUni = repository.persist(entity);
            } else {
                saveUni = session.merge(entity).map(merged -> (TeamEntity) merged);
            }
            return saveUni
                    .flatMap(savedEntity -> session.flush().replaceWith(savedEntity))
                    .map(savedEntity -> {
                        session.clear();
                        return savedEntity;
                    })
                    .flatMap(savedEntity -> repository.findByIdWithPlayers(savedEntity.getId()))
                    .map(mapper::toDomain);
        });
    }

    @Override
    public Uni<Boolean> deleteById(Long id) {
        return repository.deleteById(id);
    }
}