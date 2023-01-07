package com.usermanagement.api.resources.groups

import com.usermanagement.api.models.groups.NewGroupModel
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/groups")
@Produces(MediaType.APPLICATION_JSON)
interface IGroupsResources {
    @GET
    fun getGroups(
        @QueryParam("limit")
        @Min(1)
        @Max(100)
        @DefaultValue("25")
        limit: Int,

        @QueryParam("offset")
        @Min(0)
        @DefaultValue("0")
        offset: Int,

        @QueryParam("show_deleted")
        @DefaultValue("false")
        deleted: Boolean
    ): Response

    @GET
    @Path("{groupId}")
    fun getGroup(@PathParam("groupId") groupId: Int): Response

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun createGroup(@NotNull @Valid newGroupModel: NewGroupModel): Response

    @DELETE
    @Path("{groupId}")
    fun deleteGroup(@PathParam("groupId") groupId: Int): Response

    @GET
    @Path("{groupId}/users")
    fun getUsers(
        @PathParam("groupId")
        groupId: Int,

        @QueryParam("limit")
        @Min(1)
        @Max(100)
        @DefaultValue("25")
        limit: Int,

        @QueryParam("offset")
        @Min(0)
        @DefaultValue("0")
        offset: Int,
    ): Response
}
