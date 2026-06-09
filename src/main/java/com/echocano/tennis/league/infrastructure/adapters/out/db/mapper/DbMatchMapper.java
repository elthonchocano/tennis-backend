package com.echocano.tennis.league.infrastructure.adapters.out.db.mapper;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

import com.echocano.tennis.league.domain.model.Match;
import com.echocano.tennis.league.domain.model.SetResult;
import com.echocano.tennis.league.infrastructure.adapters.out.db.entity.MatchEntity;
import com.echocano.tennis.league.infrastructure.adapters.out.db.entity.SetResultEntity;

@Mapper(componentModel = "jakarta-cdi", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, uses = {
        DbLeagueMapper.class })
public interface DbMatchMapper {

    Match toDomain(MatchEntity entity);

    MatchEntity toEntity(Match domain);

    List<Match> toDomainList(List<MatchEntity> entities);

    SetResult toDomain(SetResultEntity entity);

    @Mapping(target = "match", ignore = true)
    SetResultEntity toEntity(SetResult domain);

    @AfterMapping
    default void linkSets(@MappingTarget MatchEntity matchEntity) {
        if (matchEntity.getSets() != null) {
            matchEntity.getSets().forEach(set -> set.setMatch(matchEntity));
        }
    }
}