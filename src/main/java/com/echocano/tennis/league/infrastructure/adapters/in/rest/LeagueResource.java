package com.echocano.tennis.league.infrastructure.adapters.in.rest;

import java.net.URI;
import java.util.List;

import com.echocano.tennis.league.application.port.in.AddParticipantUseCase;
import com.echocano.tennis.league.application.port.in.CreateLeagueUseCase;
import com.echocano.tennis.league.application.port.in.ViewLeaderboardUseCase;
import com.echocano.tennis.league.application.port.in.ViewLeagueUseCase;
import com.echocano.tennis.league.application.port.in.ViewMatchUseCase;
import com.echocano.tennis.league.infrastructure.adapters.in.rest.dto.LeagueParticipantResponse;
import com.echocano.tennis.league.infrastructure.adapters.in.rest.dto.LeagueParticipantRowRequest;
import com.echocano.tennis.league.infrastructure.adapters.in.rest.dto.LeagueRequest;
import com.echocano.tennis.league.infrastructure.adapters.in.rest.dto.LeagueResponse;
import com.echocano.tennis.league.infrastructure.adapters.in.rest.dto.MatchResponse;
import com.echocano.tennis.league.infrastructure.adapters.in.rest.mapper.RestLeaderboardMapper;
import com.echocano.tennis.league.infrastructure.adapters.in.rest.mapper.RestLeagueMapper;
import com.echocano.tennis.league.infrastructure.adapters.in.rest.mapper.RestMatchMapper;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/v1/leagues")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LeagueResource {

	private final CreateLeagueUseCase createLeagueUseCase;
	private final AddParticipantUseCase addParticipantUseCase;
	private final ViewLeaderboardUseCase viewLeaderboardUseCase;
	private final ViewLeagueUseCase viewLeagueUseCase;
	private final ViewMatchUseCase viewMatchUseCase;
	private final RestLeaderboardMapper leaderboardMapper;
	private final RestLeagueMapper leagueMapper;
	private final RestMatchMapper matchMapper;

	public LeagueResource(CreateLeagueUseCase createLeagueUseCase,
			AddParticipantUseCase addParticipantUseCase,
			ViewLeaderboardUseCase viewLeaderboardUseCase,
			ViewLeagueUseCase viewLeagueUseCase,
			ViewMatchUseCase viewMatchUseCase,
			RestLeaderboardMapper leaderboardMapper,
			RestLeagueMapper leagueMapper,
			RestMatchMapper matchMapper) {
		this.createLeagueUseCase = createLeagueUseCase;
		this.addParticipantUseCase = addParticipantUseCase;
		this.viewLeaderboardUseCase = viewLeaderboardUseCase;
		this.viewLeagueUseCase = viewLeagueUseCase;
		this.viewMatchUseCase = viewMatchUseCase;
		this.leaderboardMapper = leaderboardMapper;
		this.leagueMapper = leagueMapper;
		this.matchMapper = matchMapper;
	}

	@POST
	@RolesAllowed("admin")
	@WithTransaction
	public Uni<Response> createLeague(@Valid LeagueRequest league) {
		var domainLeague = leagueMapper.toDomain(league);
		return createLeagueUseCase.execute(domainLeague)
				.map(createdDomain -> leagueMapper.toResponse(createdDomain))
				.map(leagueResponse -> Response
						.created(URI.create("/v1/leagues/" + leagueResponse.id()))
						.entity(leagueResponse)
						.build());
	}

	@POST
	@Path("/{leagueId}/participants")
	@RolesAllowed("admin")
	@WithTransaction
	public Uni<Response> addParticipantToLeague(@PathParam("leagueId") Long leagueId,
			LeagueParticipantRowRequest request) {
		return addParticipantUseCase.execute(leagueId, request.teamId())
				.map(participant -> Response
						.created(URI.create("/v1/leagues/" + leagueId + "/participants/"
								+ participant.getId()))
						.entity(participant)
						.build())
				.onFailure().recoverWithItem(failure -> Response.status(Response.Status.CONFLICT)
						.entity(failure.getMessage())
						.build());
	}

	@GET
	@Path("/{leagueId}/leaderboard")
	@WithSession
	public Uni<List<LeagueParticipantResponse>> getLeaderboardWithPagination(
			@PathParam("leagueId") Long leagueId,
			@QueryParam("page") @DefaultValue("0") int page,
			@QueryParam("size") @DefaultValue("25") int size) {
		return viewLeaderboardUseCase.getLeaderboardWithPagination(leagueId, page, size)
				.map(leaderboardMapper::toResponseList);
	}

	@GET
	@Path("")
	@WithSession
	public Uni<List<LeagueResponse>> getLeaguesWithPagination(
			@QueryParam("page") @DefaultValue("0") int page,
			@QueryParam("size") @DefaultValue("25") int size) {
		return viewLeagueUseCase.getLeagues(page, size)
				.map(leagueMapper::toResponseList);
	}

	@GET
	@Path("/{leagueId}/teams/{teamId}/matches")
	@WithSession
	public Uni<List<MatchResponse>> getTeamMatches(
			@PathParam("leagueId") Long leagueId,
			@PathParam("teamId") Long teamId) {
		return viewMatchUseCase.getAllMatch(leagueId, teamId)
				.map(matchMapper::toResponseList);
	}
}
