package com.usermanagement.repository.users

import com.github.kittinunf.result.Result
import com.usermanagement.utils.RepositoryError

interface IUsersRepository {
    fun getUsers(
        limit: Int,
        offset: Int,
        sort: String,
        order: String,
        deleted: Boolean
    ): Result<List<User>, RepositoryError>

    fun getUser(id: Int): Result<User, RepositoryError>

    fun createUser(newUser: NewUser): Result<User, RepositoryError>

    fun updateUser(userId: Int): Result<User, RepositoryError>

    fun deleteUser(userId: Int): Result<Unit, RepositoryError>
}
