package com.echocano.tennis.league.infrastructure.adapters.in.rest.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

import com.echocano.tennis.league.domain.model.LeagueParticipant;
import com.echocano.tennis.league.infrastructure.adapters.in.rest.dto.LeagueParticipantResponse;

@Mapper(componentModel = "jakarta-cdi", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface RestLeaderboardMapper {

    LeagueParticipantResponse toResponse(LeagueParticipant domain);

    List<LeagueParticipantResponse> toResponseList(List<LeagueParticipant> domainList);
}