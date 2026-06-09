package com.echocano.tennis.league.infrastructure.adapters.out.db.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;

import com.echocano.tennis.league.domain.model.Player;
import com.echocano.tennis.league.infrastructure.adapters.out.db.entity.PlayerEntity;

@Mapper(componentModel = "jakarta-cdi", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface DbPlayerMapper {

    @Named("playerEntityToDomain")
    Player toDomain(PlayerEntity entity);

    @Named("playerDomainToEntity")
    PlayerEntity toEntity(Player domain);
}