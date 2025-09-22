package com.example.resource;

import java.net.URI;
import java.util.List;

import com.example.dto.UserCreateDTO;
import com.example.dto.UserDTO;
import com.example.dto.UserUpdateDTO;
import com.example.service.UserService;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

@ApplicationScoped
@Path("/user")
public class UserResource {
    @Inject
    UserService service;

    @GET
    public Uni<List<UserDTO>> list() {
        return service.list();
    }

    @GET
    @Path("/{id}")
    public Uni<UserDTO> get(@PathParam("id") Long id) {
        return service.get(id);
    }

    @GET
    @Path("/search")
    public Uni<List<UserDTO>> search(
            @QueryParam("name") String name,
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("20") @QueryParam("size") int size) {
        return service.search(name, page, size);
    }

    @POST
    public Uni<Response> create(@Valid UserCreateDTO in, @Context UriInfo uriInfo) {
        return service.create(in) // Uni<UserDTO>
                .map(dto -> {
                    // usa dto.id() se è un record, dto.getId() se è un classico DTO
                    UriBuilder b = uriInfo.getAbsolutePathBuilder().path(String.valueOf(dto.id));
                    return Response.created(b.build()).entity(dto).build();
                });
    }

    @PUT
    @Path("/{id}")
    public Uni<UserDTO> update(@PathParam("id") Long id, @Valid UserUpdateDTO in) {
        return service.update(id, in);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
