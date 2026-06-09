package com.echocano.tennis.league.infrastructure.adapters.in.rest.mapper;

import com.echocano.tennis.league.domain.model.League;
import com.echocano.tennis.league.domain.model.Match;
import com.echocano.tennis.league.domain.model.Team;
import com.echocano.tennis.league.domain.model.SetResult;
import com.echocano.tennis.league.infrastructure.adapters.in.rest.dto.MatchRequest;
import com.echocano.tennis.league.infrastructure.adapters.in.rest.dto.MatchResponse;
import com.echocano.tennis.league.infrastructure.adapters.in.rest.dto.SetResultDto;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "jakarta-cdi", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface RestMatchMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "winner", ignore = true)
    @Mapping(target = "league", source = "leagueId", qualifiedByName = "mapIdToLeague")
    @Mapping(target = "team1", source = "team1Id", qualifiedByName = "mapIdToTeam")
    @Mapping(target = "team2", source = "team2Id", qualifiedByName = "mapIdToTeam")
    @Mapping(target = "matchDate", source = "matchDate")
    Match toDomain(MatchRequest request);
    
    @Mapping(source = "league.id", target = "leagueId")
    MatchResponse toResponse(Match domain);

    List<MatchResponse> toResponseList(List<Match> domainList);

    @Mapping(target = "id", ignore = true)
    SetResult toDomain(SetResultDto dto);

    SetResultDto toResponse(SetResult domain);

    List<SetResult> toDomainSetsList(List<SetResultDto> dtos);

    @Named("mapIdToLeague")
    default League mapIdToLeague(Long leagueId) {
        if (leagueId == null)
            return null;
        League league = new League();
        league.setId(leagueId);
        return league;
    }

    @Named("mapIdToTeam")
    default Team mapIdToTeam(Long teamId) {
        if (teamId == null)
            return null;
        Team team = new Team();
        team.setId(teamId);
        return team;
    }
}