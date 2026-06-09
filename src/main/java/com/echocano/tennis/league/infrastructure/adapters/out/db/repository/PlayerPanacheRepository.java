package com.echocano.tennis.league.infrastructure.adapters.out.db.repository;

import com.echocano.tennis.league.infrastructure.adapters.out.db.entity.PlayerEntity;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PlayerPanacheRepository implements PanacheRepository<PlayerEntity> {
}