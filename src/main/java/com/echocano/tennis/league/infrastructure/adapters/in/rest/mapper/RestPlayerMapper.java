package com.echocano.tennis.league.infrastructure.adapters.in.rest.mapper;

import com.echocano.tennis.league.domain.model.Player;
import com.echocano.tennis.league.infrastructure.adapters.in.rest.dto.PlayerRequest;
import com.echocano.tennis.league.infrastructure.adapters.in.rest.dto.PlayerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "jakarta-cdi", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface RestPlayerMapper {
    
    @Mapping(target = "id", ignore = true)
    Player toDomain(PlayerRequest request);
    PlayerResponse toResponse(Player domain);
}