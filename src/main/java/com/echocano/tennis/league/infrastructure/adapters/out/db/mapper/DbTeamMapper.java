package com.echocano.tennis.league.infrastructure.adapters.out.db.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

import com.echocano.tennis.league.domain.model.Team;
import com.echocano.tennis.league.infrastructure.adapters.out.db.entity.TeamEntity;

@Mapper(componentModel = "jakarta-cdi", uses = {
        DbPlayerMapper.class }, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface DbTeamMapper {

    Team toDomain(TeamEntity entity);

    TeamEntity toEntity(Team domain);
}