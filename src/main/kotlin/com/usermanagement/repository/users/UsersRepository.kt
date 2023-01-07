package com.usermanagement.repository.users

import com.github.kittinunf.result.Result
import com.github.kittinunf.result.isFailure
import com.github.kittinunf.result.isSuccess
import com.usermanagement.database.dao.groupUsers.IGroupUsersDao
import com.usermanagement.database.dao.groups.IGroupsDao
import com.usermanagement.database.dao.users.IUsersDao
import com.usermanagement.database.entity.UserEntity
import com.usermanagement.repository.users.User.Companion.toData
import com.usermanagement.repository.users.UserResponsibility.Companion.toData
import com.usermanagement.utils.RepositoryError
import org.kodein.di.DI
import org.kodein.di.instance

class UsersRepository(di: DI) : IUsersRepository {
    private val usersDao: IUsersDao by di.instance()
    private val groupsDao: IGroupsDao by di.instance()
    private val groupUsersDao: IGroupUsersDao by di.instance()

    override fun getUsers(
        limit: Int,
        offset: Int,
        sort: String,
        order: String,
        deleted: Boolean
    ): Result<List<User>, RepositoryError> = Result.of {
        usersDao.getList(
            limit = limit,
            offset = offset,
            sort = sort,
            order = order,
            deleted = deleted
        ).toData()
    }

    override fun getUser(id: Int): Result<User, RepositoryError> {
        return Result.of {
            val user = usersDao.getById(id = id)
                ?: return Result.failure(RepositoryError.UserNotFound)

            return Result.success(user.toData())
        }
    }

    override fun createUser(newUser: NewUser): Result<User, RepositoryError> {
        return Result.of {
            if (usersDao.getByEmail(email = newUser.email) != null) {
                return Result.failure(RepositoryError.UserEmailInUse)
            }

            val id = usersDao.insert(
                email = newUser.email,
                firstName = newUser.firstName,
                lastName = newUser.lastName
            ) ?: return Result.failure(RepositoryError.UserNotCreated)

            return getUser(id = id)
        }
    }

    override fun updateUser(userId: Int, updateUser: UpdateUser): Result<User, RepositoryError> {
        return Result.of {
            val result = checkUser(userId = userId)
            if (result.isFailure()) {
                return result
            }

            if (updateUser.email != null && usersDao.getByEmail(email = updateUser.email) != null) {
                return Result.failure(RepositoryError.UserEmailInUse)
            }

            val user = result.get()

            usersDao.update(
                id = userId,
                firstName = updateUser.firstName ?: user.firstName,
                lastName = updateUser.lastName ?: user.lastName,
                email = updateUser.email ?: user.email
            )

            return getUser(id = userId)
        }
    }

    override fun deleteUser(userId: Int): Result<User, RepositoryError> {
        return Result.of {
            val result = checkUser(userId = userId)
            if (result.isFailure()) {
                return result
            }

            usersDao.delete(id = userId)

            return getUser(id = userId)
        }
    }

    override fun assignUser(
        userId: Int,
        groupId: Int,
        responsibility: String
    ): Result<Unit, RepositoryError> {
        groupsDao.getById(id = groupId) ?: return Result.failure(RepositoryError.GroupNotFound)
        usersDao.getById(id = userId) ?: return Result.failure(RepositoryError.UserNotFound)

        if (getResponsibility(userId = userId, groupId = groupId).isSuccess()) {
            return Result.success(Unit)
        }

        groupUsersDao.insert(userId = userId, groupId = groupId, responsibility = responsibility)

        return Result.success(Unit)
    }

    override fun unassignUser(userId: Int, groupId: Int): Result<Unit, RepositoryError> {
        groupsDao.getById(id = groupId) ?: return Result.failure(RepositoryError.GroupNotFound)
        usersDao.getById(id = userId) ?: return Result.failure(RepositoryError.UserNotFound)

        groupUsersDao.delete(userId = userId, groupId = groupId)

        return Result.success(Unit)
    }

    override fun getResponsibility(
        userId: Int,
        groupId: Int
    ): Result<UserResponsibility, RepositoryError> {
        return Result.of {
            val userResponsibility =
                groupUsersDao.getByGroupUserId(groupId = groupId, userId = userId)
                    ?: return Result.failure(RepositoryError.UserNotAssignedToGroup)

            return Result.success(userResponsibility.toData())
        }
    }

    private fun checkUser(userId: Int): Result<UserEntity, RepositoryError> {
        return Result.of {
            val user =
                usersDao.getById(id = userId) ?: return Result.failure(RepositoryError.UserNotFound)

            if (user.deletedAt != null) {
                return Result.failure(RepositoryError.UserDeleted)
            }

            return Result.success(user)
        }
    }
}
