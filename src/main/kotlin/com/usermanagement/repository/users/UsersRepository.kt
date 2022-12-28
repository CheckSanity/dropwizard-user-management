package com.usermanagement.repository.users

import com.github.kittinunf.result.Result
import com.usermanagement.database.dao.IUsersDao
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
        usersDao.getAll(
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

    override fun updateUser(userId: Int): Result<User, RepositoryError> {
        TODO("Not yet implemented")
    }

    override fun deleteUser(userId: Int): Result<Unit, RepositoryError> {
        TODO("Not yet implemented")
    }
}
