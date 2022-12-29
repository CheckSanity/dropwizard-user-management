package com.usermanagement.database.dao.users

import com.usermanagement.database.entity.UserEntity
import java.sql.Timestamp
import java.time.Instant

interface IUsersDao {
    fun getList(
        sort: String,
        order: String,
        deleted: Boolean,
        limit: Int,
        offset: Int
    ): List<UserEntity>

    fun insert(
        firstName: String,
        lastName: String,
        email: String,
        createdAt: Timestamp = Timestamp.from(Instant.now())
    ): Int?

    fun getById(id: Int): UserEntity?

    fun getByEmail(email: String): UserEntity?

    fun update(id: Int, firstName: String, lastName: String, email: String)

    fun delete(id: Int, deletedAt: Timestamp = Timestamp.from(Instant.now()))
}
