package com.echocano.tennis.league.infrastructure.adapters.in.rest;

import java.util.List;

import org.eclipse.microprofile.jwt.JsonWebToken;

import com.echocano.tennis.league.application.port.in.ManagePlayerUseCase;
import com.echocano.tennis.league.infrastructure.adapters.in.rest.dto.ClaimAccountRequest;
import com.echocano.tennis.league.infrastructure.adapters.in.rest.dto.InvitationCodeResponse;
import com.echocano.tennis.league.infrastructure.adapters.in.rest.dto.PlayerRequest;
import com.echocano.tennis.league.infrastructure.adapters.in.rest.dto.PlayerResponse;
import com.echocano.tennis.league.infrastructure.adapters.in.rest.mapper.RestPlayerMapper;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/v1/players")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("user")
public class PlayerResource {

	private final ManagePlayerUseCase playerUseCase;
	private final RestPlayerMapper mapper;
	private final JsonWebToken jwt;

	public PlayerResource(ManagePlayerUseCase managePlayerUseCase, RestPlayerMapper mapper, JsonWebToken jwt) {
		this.playerUseCase = managePlayerUseCase;
		this.mapper = mapper;
		this.jwt = jwt;
	}

	@GET
	@WithSession
	public Uni<List<PlayerResponse>> getAll() {
		return playerUseCase.getAllPlayers()
				.map(list -> list.stream().map(mapper::toResponse).toList());
	}

	@GET
	@Path("/{id}")
	@WithSession
	public Uni<Response> getById(@PathParam("id") Long id) {
		return playerUseCase.getPlayerById(id)
				.map(player -> player != null ? Response.ok(mapper.toResponse(player)).build()
						: Response.status(Response.Status.NOT_FOUND).entity("Player not found.").build());
	}

	@POST
	@RolesAllowed("admin")
	@WithTransaction
	public Uni<Response> create(PlayerRequest request) {
		if (request == null) {
			throw new WebApplicationException("Invalid creation data.", 400);
		}
		return playerUseCase.createPlayer(mapper.toDomain(request))
				.map(inserted -> Response.status(Response.Status.CREATED).entity(mapper.toResponse(inserted)).build());
	}

	@PUT
	@Path("/{id}")
	@RolesAllowed("admin")
	@WithTransaction
	public Uni<Response> update(@PathParam("id") Long id, PlayerRequest request) {
		if (request == null) {
			throw new WebApplicationException("Invalid update data.", 400);
		}
		return playerUseCase.updatePlayer(id, mapper.toDomain(request))
				.map(updated -> updated != null ? Response.ok(mapper.toResponse(updated)).build()
						: Response.status(Response.Status.NOT_FOUND).entity("Player with ID " + id + " not found")
								.build());
	}

	@DELETE
	@Path("/{id}")
	@RolesAllowed("admin")
	@WithTransaction
	public Uni<Response> delete(@PathParam("id") Long id) {
		return playerUseCase.deletePlayer(id)
				.map(deleted -> deleted ? Response.noContent().build()
						: Response.status(Response.Status.NOT_FOUND).entity("Player not found").build())
				.onFailure().transform(t -> new WebApplicationException(
						Response.status(Response.Status.CONFLICT)
								.entity("The player cannot be deleted because they belong to an active team.")
								.build()));
	}

	@GET
	@Path("/auth")
	@Authenticated
	@WithTransaction
	public Uni<Response> authenticateAndSyncPlayer() {
		String email = jwt.getClaim("email");
		String googleId = jwt.getSubject();
		String firstName = jwt.getClaim("given_name");
		String lastName = jwt.getClaim("family_name");
		String avatarUrl = jwt.getClaim("picture");

		if (firstName == null)
			firstName = "Player";
		if (lastName == null)
			lastName = "";

		return playerUseCase.processGoogleAuthentication(email, googleId, firstName, lastName, avatarUrl)
				.map(mapper::toResponse)
				.map(playerResponse -> Response.ok(playerResponse).build());
	}

	@POST
	@Path("/claim")
	@Authenticated
	@WithTransaction
	public Uni<Response> claimAccount(ClaimAccountRequest request) {
		String email = jwt.getClaim("email");
		String googleId = jwt.getSubject();
		String avatarUrl = jwt.getClaim("picture");
		return playerUseCase.claimLocalAccount(request.invitationCode(), email, googleId, avatarUrl)
				.map(mapper::toResponse)
				.map(playerResponse -> Response.ok(playerResponse).build());
	}

	@GET
	@Path("/invitation-code")
	@io.quarkus.security.Authenticated // O @PermitAll si el servicio externo de SMS usa otra seguridad (API Key, etc.)
	@WithTransaction
	public Uni<Response> getInvitationCode(@QueryParam("phoneNumber") String phoneNumber) {
		if (phoneNumber == null || phoneNumber.isBlank()) {
			return Uni.createFrom().item(
					Response.status(Response.Status.BAD_REQUEST)
							.entity("'phoneNumber' parameter is required.")
							.build());
		}
		return playerUseCase.getInvitationCodeByPhoneNumber(phoneNumber)
				.map(code -> Response.ok(new InvitationCodeResponse(code)).build());
	}
}