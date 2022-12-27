package com.usermanagement.database.entity

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet
import java.util.*


data class User(
    val email: String,
    val firstName: String,
    val lastName: String,
    val createdAt: Date,
    val deletedAt: Date?
) {
    internal class UserMapper : RowMapper<User> {
        override fun map(rs: ResultSet, ctx: StatementContext): User {
            return User(
                email = rs.getString("email"),
                firstName = rs.getString("firstName"),
                lastName = rs.getString("lastName"),
                createdAt = rs.getDate("createdAt"),
                deletedAt = rs.getDate("deletedAt")
            )
        }
    }
}
