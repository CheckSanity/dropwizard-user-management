package com.usermanagement.api.resources.users

import com.usermanagement.api.models.users.AssignUserModel
import com.usermanagement.api.models.users.NewUserModel
import com.usermanagement.api.models.users.UnassignUserModel
import com.usermanagement.api.models.users.UpdateUserModel
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
interface IUsersResources {
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
        @DefaultValue("userId")
        @Pattern(regexp = "userId|firstName|lastName|createdAt|deletedAt|email")
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

    @POST
    @Path("{userId}/assign")
    fun assignUser(
        @PathParam("userId") userId: Int,
        @NotNull @Valid assignUserModel: AssignUserModel
    ): Response

    @POST
    @Path("{userId}/unassign")
    fun unassignUser(
        @PathParam("userId") userId: Int,
        @NotNull @Valid unassignUserModel: UnassignUserModel
    ): Response

    @GET
    @Path("{userId}/responsibilities")
    fun getUserResponsibilities(
        @PathParam("userId")
        userId: Int,

        @QueryParam("group_id")
        groupId: Int
    ): Response
}
