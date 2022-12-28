package com.usermanagement.repository.groups

import com.github.kittinunf.result.Result
import com.usermanagement.utils.RepositoryError

interface IGroupsRepository {
    fun getGroups(
        limit: Int,
        offset: Int,
        deleted: Boolean
    ): Result<List<Group>, RepositoryError>

    fun getGroup(id: Int): Result<Group, RepositoryError>

    fun createGroup(newGroup: NewGroup): Result<Group, RepositoryError>

    fun updateGroup(groupId: Int): Result<Group, RepositoryError>

    fun deleteGroup(groupId: Int): Result<Unit, RepositoryError>
}
