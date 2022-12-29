package com.usermanagement.api.resources.users

import com.usermanagement.api.models.users.*
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response


@Api(
    description = "Users routes",
    protocols = "HTTP",
    produces = "application/json",
    consumes = "application/json",
    tags = ["Users"]
)
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
interface IUsersResources {
    @ApiOperation(
        value = "Get users list",
        notes = "Returns users list",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                code = 200,
                message = "OK",
                response = UserModel::class,
                responseContainer = "List"
            ),
            ApiResponse(
                code = 400,
                message = "Something bad happen"
            )
        ]
    )
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

    @ApiOperation(
        value = "Get user by specified id",
        notes = "Returns user"
    )
    @ApiResponses(
        ApiResponse(code = 200, message = "OK", response = UserModel::class),
        ApiResponse(code = 404, message = "User not found")
    )
    @GET
    @Path("{userId}")
    fun getUser(@PathParam("userId") userId: Int): Response

    @ApiOperation(
        value = "Create new user",
        notes = "Returns created user"
    )
    @ApiResponses(
        ApiResponse(code = 200, message = "OK", response = UserModel::class),
        ApiResponse(code = 409, message = "Email already in use"),
        ApiResponse(code = 400, message = "Something bad happen")
    )
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun createUser(@NotNull @Valid newUserModel: NewUserModel): Response

    @ApiOperation(
        value = "Update user with specified id",
        notes = "Returns updated user"
    )
    @ApiResponses(
        ApiResponse(code = 200, message = "OK", response = UserModel::class),
        ApiResponse(code = 404, message = "User not found or deleted"),
        ApiResponse(code = 400, message = "Something bad happen")
    )
    @PUT
    @Path("{userId}")
    fun updateUser(
        @PathParam("userId") userId: Int,
        @NotNull @Valid updateUserModel: UpdateUserModel
    ): Response

    @ApiOperation(
        value = "Deletes user with specified id",
        notes = "Returns deleted user"
    )
    @ApiResponses(
        ApiResponse(code = 200, message = "OK", response = UserModel::class),
        ApiResponse(code = 404, message = "User not found or deleted"),
        ApiResponse(code = 400, message = "Something bad happen")
    )
    @DELETE
    @Path("{userId}")
    fun deleteUser(@PathParam("userId") userId: Int): Response

    @ApiOperation(
        value = "Assign user with specified id to group",
    )
    @ApiResponses(
        ApiResponse(code = 204, message = "OK"),
        ApiResponse(code = 404, message = "User/group not found"),
    )
    @POST
    @Path("{userId}/assign")
    fun assignUser(
        @PathParam("userId") userId: Int,
        @NotNull @Valid assignUserModel: AssignUserModel
    ): Response

    @ApiOperation(
        value = "Unassing user with specified id from group",
        code = 204
    )
    @ApiResponses(
        ApiResponse(code = 204, message = "OK"),
        ApiResponse(code = 404, message = "User/group not found"),
    )
    @POST
    @Path("{userId}/unassign")
    fun unassignUser(
        @PathParam("userId") userId: Int,
        @NotNull @Valid unassignUserModel: UnassignUserModel
    ): Response

    @ApiOperation(
        value = "Get user responsibilities in the specified group",
        notes = "Returns user responsibility string"
    )
    @ApiResponses(
        ApiResponse(code = 200, message = "OK", response = String::class),
        ApiResponse(code = 404, message = "User not assigned to group"),
    )
    @GET
    @Path("{userId}/responsibilities")
    fun getUserResponsibilities(
        @PathParam("userId")
        userId: Int,

        @QueryParam("group_id")
        groupId: Int
    ): Response
}
