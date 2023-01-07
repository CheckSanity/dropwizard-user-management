package com.usermanagement.repository.groups

import com.github.kittinunf.result.Result
import com.github.kittinunf.result.isFailure
import com.usermanagement.database.dao.groupUsers.IGroupUsersDao
import com.usermanagement.database.dao.groups.IGroupsDao
import com.usermanagement.database.entity.GroupEntity
import com.usermanagement.repository.groups.Group.Companion.toData
import com.usermanagement.utils.RepositoryError
import org.kodein.di.DI
import org.kodein.di.instance

class GroupsRepository(di: DI) : IGroupsRepository {
    private val groupsDao: IGroupsDao by di.instance()
    private val groupUsersDao: IGroupUsersDao by di.instance()

    override fun getGroups(
        limit: Int,
        offset: Int,
        deleted: Boolean
    ): Result<List<Group>, RepositoryError> = Result.of {
        groupsDao.getList(
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
            if (groupsDao.getByName(name = newGroup.name) != null) {
                return Result.failure(RepositoryError.GroupNameInUse)
            }

            val id = groupsDao.insert(
                name = newGroup.name,
                description = newGroup.description,
            ) ?: return Result.failure(RepositoryError.GroupNotCreated)

            return getGroup(id = id)
        }
    }

    override fun deleteGroup(groupId: Int): Result<Group, RepositoryError> {
        return Result.of {
            val result = checkGroup(groupId = groupId)
            if (result.isFailure()) {
                return result
            }

            groupsDao.delete(id = groupId)

            return getGroup(id = groupId)
        }
    }

    override fun getUsers(
        groupId: Int,
        limit: Int,
        offset: Int
    ): Result<List<Int>, RepositoryError> {
        return Result.of {
            return Result.success(
                groupUsersDao.getUserIdsByGroupId(
                    groupId = groupId,
                    limit = limit,
                    offset = offset
                )
            )
        }
    }

    private fun checkGroup(groupId: Int): Result<GroupEntity, RepositoryError> {
        return Result.of {
            val group =
                groupsDao.getById(id = groupId)
                    ?: return Result.failure(RepositoryError.GroupNotFound)

            if (group.deletedAt != null) {
                return Result.failure(RepositoryError.GroupDeleted)
            }

            return Result.success(group)
        }
    }
}
