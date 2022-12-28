package com.usermanagement.database.dao

import com.usermanagement.database.entity.UserEntity
import org.jdbi.v3.sqlobject.config.RegisterRowMapper
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.Define
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import java.sql.Timestamp
import java.time.Instant
import java.util.*

@RegisterRowMapper(UserEntity.Mapper::class)
interface IUsersDao {
    @SqlUpdate(
        "CREATE TABLE IF NOT EXISTS users " +
                "(id INTEGER PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY," +
                "firstName VARCHAR, " +
                "lastName VARCHAR, " +
                "email VARCHAR UNIQUE, " +
                "createdAt TIMESTAMP, " +
                "deletedAt TIMESTAMP DEFAULT(NULL))"
    )
    fun createTable()

    @SqlQuery("SELECT * FROM users ORDER BY <sort> <order> LIMIT :limit OFFSET :offset")
    fun getAll(
        @Define("sort") sort: String,
        @Define("order") order: String,
        @Bind("limit") limit: Int,
        @Bind("offset") offset: Int,
        @Bind("deleted") deleted: Boolean
    ): List<UserEntity>

    @SqlUpdate("INSERT INTO users (firstName, lastName, email, createdAt) VALUES (:firstName, :lastName, :email, :createdAt)")
    @GetGeneratedKeys
    fun insert(
        @Bind("firstName") firstName: String,
        @Bind("lastName") lastName: String,
        @Bind("email") email: String,
        @Bind("createdAt") createdAt: Timestamp = Timestamp.from(Instant.now())
    ): Int?

    @SqlQuery("SELECT * FROM users WHERE id = :id")
    fun getById(@Bind("id") id: Int): UserEntity?

    @SqlQuery("SELECT * FROM users WHERE email = :email")
    fun getByEmail(@Bind("email") email: String): UserEntity?

    @SqlUpdate("UPDATE users SET firstName = :firstName, lastName = :lastName, email = :email WHERE id = :id")
    fun updateUser(
        @Bind("id") id: Int,
        @Bind("firstName") firstName: String,
        @Bind("lastName") lastName: String,
        @Bind("email") email: String
    )

    @SqlUpdate("UPDATE users SET deletedAt = :deletedAt WHERE id = :id")
    fun deleteUser(
        @Bind("id") id: Int,
        @Bind("deletedAt") deletedAt: Timestamp = Timestamp.from(Instant.now())
    )
}
