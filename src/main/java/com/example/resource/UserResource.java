package com.example.resource;

import java.net.URI;
import java.util.List;

import com.example.dto.UserCreateDTO;
import com.example.dto.UserDTO;
import com.example.dto.UserUpdateDTO;
import com.example.service.UserService;

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
import jakarta.ws.rs.core.UriInfo;

@ApplicationScoped
@Path("/user")
public class UserResource {
    @Inject
    UserService service;

    @GET
    public List<UserDTO> list() {
        return service.list();
    }

    @GET
    @Path("/{id}")
    public UserDTO get(@PathParam("id") Long id) {
        return service.get(id);
    }

    @GET
    @Path("/search")
    public List<UserDTO> search(
            @QueryParam("name") String name,
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("20") @QueryParam("size") int size) {
        return service.search(name, page, size);
    }

    @POST
    public Response create(@Valid UserCreateDTO in, @Context UriInfo uriInfo) {
        UserDTO created = service.create(in);
        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(created.id)).build();
        return Response.created(location).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    public UserDTO update(@PathParam("id") Long id, @Valid UserUpdateDTO in) {
        return service.update(id, in);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
