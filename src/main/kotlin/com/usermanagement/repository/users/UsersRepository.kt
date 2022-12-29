package com.usermanagement.repository.users

import com.github.kittinunf.result.Result
import com.github.kittinunf.result.isFailure
import com.usermanagement.database.dao.users.IUsersDao
import com.usermanagement.database.entity.UserEntity
import com.usermanagement.repository.users.User.Companion.toData
import com.usermanagement.utils.RepositoryError

class UsersRepository(private val usersDao: IUsersDao) : IUsersRepository {
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
