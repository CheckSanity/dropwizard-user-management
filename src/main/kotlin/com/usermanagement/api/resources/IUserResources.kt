package com.usermanagement.api.resources

import com.usermanagement.api.models.NewUserModel
import com.usermanagement.api.models.UpdateUserModel
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
interface IUserResources {
    @GET
    fun getUsers(
        @QueryParam("limit")
        @Min(1)
        @Max(100)
        @DefaultValue("25")
        limit: Int,

        @QueryParam("offset")
        @Min(0)
        @DefaultValue("0")
        offset: Int,

        @QueryParam("sort")
        @DefaultValue("id")
        @Pattern(regexp = "id|firstName|lastName|createdAt|deletedAt|email")
        sort: String,

        @QueryParam("order")
        @DefaultValue("ASC")
        @Pattern(regexp = "ASC|DESC")
        order: String,

        @QueryParam("show_deleted")
        @DefaultValue("false")
        deleted: Boolean
    ): Response

    @GET
    @Path("{userId}")
    fun getUser(@PathParam("userId") userId: Int): Response

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun createUser(@NotNull @Valid newUserModel: NewUserModel): Response

    @PUT
    @Path("{userId}")
    fun updateUser(
        @PathParam("userId") userId: Int,
        @NotNull @Valid updateUserModel: UpdateUserModel
    ): Response

    @DELETE
    @Path("{userId}")
    fun deleteUser(@PathParam("userId") userId: Int): Response
}
