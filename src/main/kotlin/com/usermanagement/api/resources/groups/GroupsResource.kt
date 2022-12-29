package com.usermanagement.api.resources.groups

import com.usermanagement.api.models.groups.GroupModel.Companion.toModel
import com.usermanagement.api.models.groups.NewGroupModel
import com.usermanagement.repository.groups.IGroupsRepository
import com.usermanagement.repository.groups.NewGroup
import com.usermanagement.utils.RepositoryError
import org.kodein.di.DI
import org.kodein.di.instance
import javax.ws.rs.core.Response


class GroupsResource(di: DI) : IGroupsResources {
    private val groupsRepository: IGroupsRepository by di.instance()

    override fun getGroups(
        limit: Int,
        offset: Int,
        deleted: Boolean
    ): Response {
        return groupsRepository.getGroups(
            limit = limit,
            offset = offset,
            deleted = deleted
        ).fold(
            success = { groups ->
                Response
                    .status(Response.Status.OK)
                    .entity(groups.toModel())
                    .build()
            },
            failure = {
                Response
                    .status(Response.Status.BAD_REQUEST)
                    .build()
            }
        )
    }

    override fun getGroup(groupId: Int): Response {
        return groupsRepository.getGroup(id = groupId).fold(
            success = { group ->
                Response
                    .status(Response.Status.OK)
                    .entity(group.toModel())
                    .build()
            },
            failure = { error ->
                when (error) {
                    is RepositoryError.GroupNotFound -> {
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

    override fun createGroup(newGroupModel: NewGroupModel): Response {
        return groupsRepository.createGroup(
            newGroup = NewGroup(
                name = newGroupModel.name,
                description = newGroupModel.description,
            )
        ).fold(
            success = { group ->
                Response
                    .status(Response.Status.OK)
                    .entity(group.toModel())
                    .build()
            },
            failure = { error ->
                when (error) {
                    RepositoryError.GroupNameInUse -> {
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

    override fun deleteGroup(groupId: Int): Response {
        return groupsRepository.deleteGroup(groupId = groupId).fold(
            success = { group ->
                Response
                    .status(Response.Status.OK)
                    .entity(group.toModel())
                    .build()
            },
            failure = { error ->
                when (error) {
                    RepositoryError.GroupNotFound,
                    RepositoryError.GroupDeleted -> {
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
