package com.echocano.tennis.league.infrastructure.adapters.in.rest.mapper;

import com.echocano.tennis.league.domain.model.Team;
import com.echocano.tennis.league.infrastructure.adapters.in.rest.dto.TeamRequest;
import com.echocano.tennis.league.infrastructure.adapters.in.rest.dto.TeamResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "jakarta-cdi", uses = {RestPlayerMapper.class}, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface RestTeamMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "player1Id", target = "player1.id")
    @Mapping(source = "player2Id", target = "player2.id")
    Team toDomain(TeamRequest request);

    TeamResponse toResponse(Team domain);
}