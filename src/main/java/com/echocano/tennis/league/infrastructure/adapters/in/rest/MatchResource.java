package com.echocano.tennis.league.infrastructure.adapters.in.rest;

import java.net.URI;
import java.util.List;

import com.echocano.tennis.league.application.port.in.RegisterMatchUseCase;
import com.echocano.tennis.league.application.port.in.UpdateMatchResultUseCase;
import com.echocano.tennis.league.domain.model.SetResult;
import com.echocano.tennis.league.infrastructure.adapters.in.rest.dto.MatchRequest;
import com.echocano.tennis.league.infrastructure.adapters.in.rest.dto.MatchResponse;
import com.echocano.tennis.league.infrastructure.adapters.in.rest.dto.MatchResultRequest;
import com.echocano.tennis.league.infrastructure.adapters.in.rest.mapper.RestMatchMapper;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/v1/matches")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MatchResource {

	private final RegisterMatchUseCase registerMatchUseCase;
	private final UpdateMatchResultUseCase updateMatchResultUseCase;
	private final RestMatchMapper matchMapper;

	public MatchResource(RegisterMatchUseCase registerMatchUseCase,
			UpdateMatchResultUseCase updateMatchResultUseCase, RestMatchMapper matchMapper) {
		this.registerMatchUseCase = registerMatchUseCase;
		this.updateMatchResultUseCase = updateMatchResultUseCase;
		this.matchMapper = matchMapper;
	}

	@POST
	@RolesAllowed("admin")
	@WithTransaction
	public Uni<Response> registerMatch(MatchRequest request) {
		return Uni.createFrom().item(matchMapper.toDomain(request))
				.flatMap(registerMatchUseCase::execute)
				.map(matchMapper::toResponse)
				.map(responseBody -> Response.created(URI.create("/v1/matches/" + responseBody.id()))
						.entity(responseBody)
						.build())
				.onFailure(IllegalArgumentException.class).recoverWithItem(error -> Response.status(422)
						.entity(error.getMessage())
						.build())
				.onFailure(IllegalStateException.class)
				.recoverWithItem(error -> Response.status(Response.Status.BAD_REQUEST)
						.entity(error.getMessage())
						.build());
	}

	@PATCH
	@RolesAllowed("admin")
	@Path("/{id}/result")
	@WithTransaction
	public Uni<MatchResponse> updateResult(
			@PathParam("id") Long id,
			@Valid MatchResultRequest request) {
		List<SetResult> domainSets = matchMapper.toDomainSetsList(request.sets());
		System.out.print(request);
		return updateMatchResultUseCase.execute(
				id,
				domainSets,
				request.walkover(),
				request.walkoverWinnerId())
				.map(match -> matchMapper.toResponse(match));
	}
}