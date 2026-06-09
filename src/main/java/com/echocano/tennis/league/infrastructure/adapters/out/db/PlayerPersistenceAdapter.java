package com.echocano.tennis.league.infrastructure.adapters.out.db;

import java.util.List;

import com.echocano.tennis.league.application.port.out.PlayerRepositoryPort;
import com.echocano.tennis.league.domain.model.Player;
import com.echocano.tennis.league.infrastructure.adapters.out.db.mapper.DbPlayerMapper;
import com.echocano.tennis.league.infrastructure.adapters.out.db.repository.PlayerPanacheRepository;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PlayerPersistenceAdapter implements PlayerRepositoryPort {

    private final PlayerPanacheRepository repository;
    private final DbPlayerMapper mapper;

    public PlayerPersistenceAdapter(PlayerPanacheRepository repository, DbPlayerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Uni<List<Player>> findAll() {
        return repository.listAll()
                .map(entities -> entities.stream().map(mapper::toDomain).toList());
    }

    @Override
    public Uni<Player> findById(Long id) {
        return repository.findById(id).map(entity -> entity != null ? mapper.toDomain(entity) : null);
    }

    @Override
    public Uni<Player> save(Player player) {
        var entity = mapper.toEntity(player);
        if (entity.getId() == null) {
            return repository.persistAndFlush(entity)
                    .map(mapper::toDomain);
        } else {
            return repository.getSession()
                    .flatMap(session -> session.merge(entity)
                            .flatMap(mergedEntity -> session.flush()
                                    .map(ignored -> mergedEntity)))
                    .map(mapper::toDomain);
        }
    }

    @Override
    public Uni<Boolean> deleteById(Long id) {
        return repository.deleteById(id);
    }
}