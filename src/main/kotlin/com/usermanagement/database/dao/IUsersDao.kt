package com.usermanagement.database.dao

import com.usermanagement.database.entity.User
import org.jdbi.v3.sqlobject.config.RegisterRowMapper
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

@RegisterRowMapper(User.UserMapper::class)
interface IUsersDao {
    @SqlUpdate(
        "CREATE TABLE IF NOT EXISTS users " +
                "(id INTEGER PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY," +
                "firstName VARCHAR, " +
                "secondName VARCHAR, " +
                "email VARCHAR UNIQUE, " +
                "createdAt DATE, " +
                "deletedAt DATE DEFAULT(NULL))"
    )
    fun createTable()

    @SqlQuery("SELECT * FROM users")
    fun getAll(): List<User>

    @SqlUpdate("INSERT INTO users (id, name) VALUES (:id, :name)")
    fun insert(@Bind("id") id: Int, @Bind("name") name: String)

    @SqlQuery("SELECT * FROM users where id = :id")
    fun findById(@Bind("id") id: Int): User?
}
