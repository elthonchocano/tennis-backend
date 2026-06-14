package com.echocano.tennis.league.infrastructure.adapters.in.rest;

import java.util.List;

import com.echocano.tennis.league.application.port.in.ManageTeamUseCase;
import com.echocano.tennis.league.infrastructure.adapters.in.rest.dto.TeamRequest;
import com.echocano.tennis.league.infrastructure.adapters.in.rest.dto.TeamResponse;
import com.echocano.tennis.league.infrastructure.adapters.in.rest.mapper.RestTeamMapper;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/v1/teams")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TeamResource {

        @Inject
        ManageTeamUseCase teamUseCase;

        @Inject
        RestTeamMapper mapper;

        @GET
        @WithSession
        public Uni<List<TeamResponse>> getAll() {
                return teamUseCase.getAllTeams()
                                .map(list -> list.stream().map(mapper::toResponse).toList());
        }

        @GET
        @Path("/{id}")
        @WithSession
        public Uni<Response> getById(@PathParam("id") Long id) {
                return teamUseCase.getTeamById(id)
                                .map(team -> team != null ? Response.ok(mapper.toResponse(team)).build()
                                                : Response.status(Response.Status.NOT_FOUND).entity("Team not found")
                                                                .build());
        }

        @POST
        @RolesAllowed("admin")
        @WithTransaction
        public Uni<Response> create(TeamRequest request) {
                if (request == null) {
                        throw new WebApplicationException("Invalid Team structure.", 422);
                }
                return teamUseCase.createTeam(mapper.toDomain(request))
                                .map(inserted -> Response.status(Response.Status.CREATED)
                                                .entity(mapper.toResponse(inserted)).build());
        }

        @PUT
        @Path("/{id}")
        @RolesAllowed("admin")
        @WithTransaction
        public Uni<Response> update(@PathParam("id") Long id, TeamRequest request) {
                if (request == null) {
                        throw new WebApplicationException("Invalid team data.", 400);
                }
                return teamUseCase.updateTeam(id, mapper.toDomain(request))
                                .map(updated -> updated != null ? Response.ok(mapper.toResponse(updated)).build()
                                                : Response.status(Response.Status.NOT_FOUND)
                                                                .entity("Team with ID " + id + " not found.")
                                                                .build());
        }

        @DELETE
        @Path("/{id}")
        @RolesAllowed("admin")
        @WithTransaction
        public Uni<Response> delete(@PathParam("id") Long id) {
                return teamUseCase.deleteTeam(id)
                                .map(deleted -> deleted ? Response.noContent().build()
                                                : Response.status(Response.Status.NOT_FOUND).entity("Team not found.")
                                                                .build())
                                .onFailure().transform(t -> new WebApplicationException(
                                                Response.status(Response.Status.CONFLICT)
                                                                .entity("The team cannot be deleted because it already has recorded matches.")
                                                                .build()));
        }
}