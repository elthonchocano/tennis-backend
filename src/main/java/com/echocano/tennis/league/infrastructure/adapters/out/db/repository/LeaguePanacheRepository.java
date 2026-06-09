package com.echocano.tennis.league.infrastructure.adapters.out.db.repository;

import java.util.List;

import com.echocano.tennis.league.infrastructure.adapters.out.db.entity.LeagueEntity;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LeaguePanacheRepository implements PanacheRepositoryBase<LeagueEntity, Long> {

    public Uni<Boolean> existsByNameAndSeason(String name, String season) {
        return count("name = ?1 AND season = ?2", name, season)
                .map(count -> count > 0);
    }

    public Uni<List<LeagueEntity>> findAllWithPagination(int page, int pageSize) {
        return find("order by name, season desc").page(page, pageSize).list();
    }
}
