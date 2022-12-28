package com.usermanagement.repository.groups

import com.github.kittinunf.result.Result
import com.usermanagement.database.dao.IGroupsDao
import com.usermanagement.repository.groups.Group.Companion.toData
import com.usermanagement.utils.RepositoryError

class GroupsRepository(private val groupsDao: IGroupsDao) : IGroupsRepository {
    override fun getGroups(
        limit: Int,
        offset: Int,
        deleted: Boolean
    ): Result<List<Group>, RepositoryError> = Result.of {
        groupsDao.getAll(
            limit = limit,
            offset = offset,
            deleted = deleted
        ).toData()
    }

    override fun getGroup(id: Int): Result<Group, RepositoryError> {
        return Result.of {
            val group = groupsDao.getById(id = id)
                ?: return Result.failure(RepositoryError.UserNotFound)

            return Result.success(group.toData())
        }
    }

    override fun createGroup(newGroup: NewGroup): Result<Group, RepositoryError> {
        return Result.of {
            val id = groupsDao.insert(
                name = newGroup.name,
                description = newGroup.description,
            ) ?: return Result.failure(RepositoryError.GroupNotCreated)

            return getGroup(id = id)
        }
    }

    override fun updateGroup(groupId: Int): Result<Group, RepositoryError> {
        TODO("Not yet implemented")
    }

    override fun deleteGroup(groupId: Int): Result<Unit, RepositoryError> {
        TODO("Not yet implemented")
    }
}
