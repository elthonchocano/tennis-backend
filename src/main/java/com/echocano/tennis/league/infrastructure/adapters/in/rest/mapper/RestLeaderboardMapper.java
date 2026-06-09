package com.echocano.tennis.league.infrastructure.adapters.in.rest.mapper;

import com.echocano.tennis.league.domain.model.LeagueParticipant;
import com.echocano.tennis.league.infrastructure.adapters.in.rest.dto.LeagueParticipantResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(componentModel = "jakarta-cdi", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface RestLeaderboardMapper {

    LeagueParticipantResponse toResponse(LeagueParticipant domain);
    List<LeagueParticipantResponse> toResponseList(List<LeagueParticipant> domainList);
}