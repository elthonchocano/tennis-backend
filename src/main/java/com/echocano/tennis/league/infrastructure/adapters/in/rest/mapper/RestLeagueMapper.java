package com.echocano.tennis.league.infrastructure.adapters.in.rest.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

import com.echocano.tennis.league.domain.model.League;
import com.echocano.tennis.league.infrastructure.adapters.in.rest.dto.LeagueRequest;
import com.echocano.tennis.league.infrastructure.adapters.in.rest.dto.LeagueResponse;

@Mapper(componentModel = "jakarta-cdi", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface RestLeagueMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "matches", ignore = true)
    League toDomain(LeagueRequest request);

    LeagueResponse toResponse(League domain);

    List<LeagueResponse> toResponseList(List<League> domainList);
}
