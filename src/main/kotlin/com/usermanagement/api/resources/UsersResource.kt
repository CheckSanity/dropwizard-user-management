package com.usermanagement.api.resources

import com.usermanagement.api.models.NewUserModel
import com.usermanagement.api.models.UpdateUserModel
import com.usermanagement.api.models.UserModel.Companion.toModel
import com.usermanagement.repository.users.IUsersRepository
import com.usermanagement.repository.users.NewUser
import com.usermanagement.repository.users.UpdateUser
import com.usermanagement.utils.RepositoryError
import org.kodein.di.DI
import org.kodein.di.instance
import javax.ws.rs.core.Response


class UsersResource(di: DI) : IUserResources {
    private val usersRepository: IUsersRepository by di.instance()

    override fun getUsers(
        limit: Int,
        offset: Int,
        sort: String,
        order: String,
        deleted: Boolean
    ): Response {
        return usersRepository.getUsers(
            limit = limit,
            offset = offset,
            sort = sort,
            order = order,
            deleted = deleted
        ).fold(
            success = { users ->
                Response
                    .status(Response.Status.OK)
                    .entity(users.toModel())
                    .build()
            },
            failure = {
                Response
                    .status(Response.Status.BAD_REQUEST)
                    .build()
            }
        )
    }

    override fun getUser(userId: Int): Response {
        return usersRepository.getUser(id = userId).fold(
            success = { user ->
                Response
                    .status(Response.Status.OK)
                    .entity(user.toModel())
                    .build()
            },
            failure = { error ->
                when (error) {
                    is RepositoryError.UserNotFound -> {
                        Response
                            .status(Response.Status.NOT_FOUND)
                            .entity(error.message)
                            .build()
                    }

                    else -> {
                        Response
                            .status(Response.Status.BAD_REQUEST)
                            .entity(error.message)
                            .build()
                    }
                }
            }
        )
    }

    override fun createUser(newUserModel: NewUserModel): Response {
        return usersRepository.createUser(
            newUser = NewUser(
                email = newUserModel.email,
                firstName = newUserModel.firstName,
                lastName = newUserModel.lastName
            )
        ).fold(
            success = { user ->
                Response
                    .status(Response.Status.OK)
                    .entity(user.toModel())
                    .build()
            },
            failure = { error ->
                when (error) {
                    RepositoryError.UserEmailInUse -> {
                        Response
                            .status(Response.Status.CONFLICT)
                            .entity(error.message)
                            .build()
                    }

                    else -> {
                        Response
                            .status(Response.Status.BAD_REQUEST)
                            .entity(error.message)
                            .build()
                    }
                }
            }
        )
    }

    override fun deleteUser(userId: Int): Response {
        return usersRepository.deleteUser(userId = userId).fold(
            success = { user ->
                Response
                    .status(Response.Status.OK)
                    .entity(user.toModel())
                    .build()
            },
            failure = { error ->
                when (error) {
                    RepositoryError.UserNotFound,
                    RepositoryError.UserDeleted -> {
                        Response
                            .status(Response.Status.NOT_FOUND)
                            .entity(error.message)
                            .build()
                    }

                    else -> {
                        Response
                            .status(Response.Status.BAD_REQUEST)
                            .entity(error.message)
                            .build()
                    }
                }
            }
        )
    }

    override fun updateUser(userId: Int, updateUserModel: UpdateUserModel): Response {
        return usersRepository.updateUser(
            userId = userId,
            updateUser = UpdateUser(
                email = updateUserModel.email,
                firstName = updateUserModel.firstName,
                lastName = updateUserModel.lastName
            )
        ).fold(
            success = { user ->
                Response
                    .status(Response.Status.OK)
                    .entity(user.toModel())
                    .build()
            },
            failure = { error ->
                when (error) {
                    RepositoryError.UserNotFound,
                    RepositoryError.UserDeleted -> {
                        Response
                            .status(Response.Status.NOT_FOUND)
                            .entity(error.message)
                            .build()
                    }

                    else -> {
                        Response
                            .status(Response.Status.BAD_REQUEST)
                            .entity(error.message)
                            .build()
                    }
                }
            }
        )
    }
}
