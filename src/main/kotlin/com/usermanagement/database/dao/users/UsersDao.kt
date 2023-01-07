package com.usermanagement.database.dao.users

import com.usermanagement.database.entity.UserEntity
import org.jdbi.v3.core.Jdbi
import java.sql.Timestamp

class UsersDao(database: Jdbi) : IUsersDao {
    private val queries = database.onDemand(UsersQueries::class.java)

    init {
        queries.createTable()
    }

    override fun getList(
        sort: String,
        order: String,
        deleted: Boolean,
        limit: Int,
        offset: Int,
    ): List<UserEntity> {
        return if (deleted) {
            queries.getAll(sort = sort, order = order, limit = limit, offset = offset)
        } else {
            queries.getActive(sort = sort, order = order, limit = limit, offset = offset)
        }
    }

    override fun insert(
        firstName: String,
        lastName: String,
        email: String,
        createdAt: Timestamp
    ): Int? =
        queries.insert(
            firstName = firstName,
            lastName = lastName,
            email = email,
            createdAt = createdAt
        )

    override fun getById(id: Int): UserEntity? = queries.getById(id = id)

    override fun getByEmail(email: String): UserEntity? = queries.getByEmail(email = email)

    override fun update(id: Int, firstName: String, lastName: String, email: String) =
        queries.update(id = id, firstName = firstName, lastName = lastName, email = email)

    override fun delete(id: Int, deletedAt: Timestamp) =
        queries.delete(id = id, deletedAt = deletedAt)
}
