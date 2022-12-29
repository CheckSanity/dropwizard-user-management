package com.usermanagement.database.dao.users

import com.usermanagement.database.entity.UserEntity
import java.sql.Timestamp
import java.time.Instant

interface IUsersDao {
    fun getList(
        sort: String = "userId",
        order: String = "ASC",
        deleted: Boolean = false,
        limit: Int = 25,
        offset: Int = 0
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
