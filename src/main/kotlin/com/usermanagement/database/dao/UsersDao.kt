package com.usermanagement.database.dao

import com.usermanagement.database.entity.UserEntity
import org.jdbi.v3.core.Jdbi
import java.sql.Timestamp

class UsersDao(database: Jdbi) : IUsersDao {
    private val dao = database.onDemand(IUsersDao::class.java)

    init {
        createTable()
    }

    override fun createTable() = dao.createTable()

    override fun getAll(
        sort: String,
        order: String,
        limit: Int,
        offset: Int,
        deleted: Boolean
    ): List<UserEntity> =
        dao.getAll(sort = sort, order = order, limit = limit, offset = offset, deleted = deleted)

    override fun insert(
        firstName: String,
        lastName: String,
        email: String,
        createdAt: Timestamp
    ): Int? =
        dao.insert(firstName = firstName, lastName = lastName, email = email, createdAt = createdAt)

    override fun getById(id: Int): UserEntity? = dao.getById(id = id)

    override fun getByEmail(email: String): UserEntity? = dao.getByEmail(email = email)

    override fun updateUser(id: Int, firstName: String, lastName: String, email: String) =
        dao.updateUser(id = id, firstName = firstName, lastName = lastName, email = email)

    override fun deleteUser(id: Int, deletedAt: Timestamp) =
        dao.deleteUser(id = id, deletedAt = deletedAt)
}
