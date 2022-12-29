package com.usermanagement.database.dao.users

import com.usermanagement.database.entity.UserEntity
import org.jdbi.v3.sqlobject.config.RegisterRowMapper
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.Define
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import java.sql.Timestamp
import java.util.*

@RegisterRowMapper(UserEntity.Mapper::class)
interface UsersQueries {
    @SqlUpdate(
        "CREATE TABLE IF NOT EXISTS users " +
                "(userId INTEGER PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY," +
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
        @Bind("offset") offset: Int
    ): List<UserEntity>

    @SqlQuery("SELECT * FROM users WHERE deletedAt IS NULL ORDER BY <sort> <order> LIMIT :limit OFFSET :offset")
    fun getActive(
        @Define("sort") sort: String,
        @Define("order") order: String,
        @Bind("limit") limit: Int,
        @Bind("offset") offset: Int
    ): List<UserEntity>

    @SqlUpdate("INSERT INTO users (firstName, lastName, email, createdAt) VALUES (:firstName, :lastName, :email, :createdAt)")
    @GetGeneratedKeys
    fun insert(
        @Bind("firstName") firstName: String,
        @Bind("lastName") lastName: String,
        @Bind("email") email: String,
        @Bind("createdAt") createdAt: Timestamp
    ): Int?

    @SqlQuery("SELECT * FROM users WHERE userId = :id")
    fun getById(@Bind("id") id: Int): UserEntity?

    @SqlQuery("SELECT * FROM users WHERE email = :email")
    fun getByEmail(@Bind("email") email: String): UserEntity?

    @SqlUpdate("UPDATE users SET firstName = :firstName, lastName = :lastName, email = :email WHERE userId = :id")
    fun update(
        @Bind("id") id: Int,
        @Bind("firstName") firstName: String,
        @Bind("lastName") lastName: String,
        @Bind("email") email: String
    )

    @SqlUpdate("UPDATE users SET deletedAt = :deletedAt WHERE userId = :id")
    fun delete(
        @Bind("id") id: Int,
        @Bind("deletedAt") deletedAt: Timestamp
    )
}
