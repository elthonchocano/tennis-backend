package com.echocano.tennis.league.infrastructure.adapters.out.db.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

import com.echocano.tennis.league.domain.model.League;
import com.echocano.tennis.league.domain.model.LeagueParticipant;
import com.echocano.tennis.league.domain.model.Player;
import com.echocano.tennis.league.domain.model.Team;
import com.echocano.tennis.league.infrastructure.adapters.out.db.entity.LeagueEntity;
import com.echocano.tennis.league.infrastructure.adapters.out.db.entity.LeagueParticipantEntity;
import com.echocano.tennis.league.infrastructure.adapters.out.db.entity.PlayerEntity;
import com.echocano.tennis.league.infrastructure.adapters.out.db.entity.TeamEntity;

@Mapper(componentModel = "jakarta-cdi", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, uses = {
        DbPlayerMapper.class })
public interface DbLeagueMapper {

    @Mapping(target = "matches", ignore = true)
    League toDomain(LeagueEntity entity);

    @Mapping(target = "matches", ignore = true)
    LeagueEntity toEntity(League domain);

    Player toDomain(PlayerEntity entity);

    PlayerEntity toEntity(Player domain);

    @Mapping(target = "player1", qualifiedByName = "playerEntityToDomain")
    @Mapping(target = "player2", qualifiedByName = "playerEntityToDomain")
    Team toDomain(TeamEntity entity);

    @Mapping(target = "player1", qualifiedByName = "playerDomainToEntity")
    @Mapping(target = "player2", qualifiedByName = "playerDomainToEntity")
    TeamEntity toEntity(Team domain);

    LeagueParticipant toDomain(LeagueParticipantEntity entity);

    LeagueParticipantEntity toEntity(LeagueParticipant domain);
}