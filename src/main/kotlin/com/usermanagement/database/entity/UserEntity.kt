package com.usermanagement.database.entity

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet


data class UserEntity(
    val id: Int,
    val email: String,
    val firstName: String,
    val lastName: String,
    val createdAt: Long,
    val deletedAt: Long?
) {
    internal class Mapper : RowMapper<UserEntity> {
        override fun map(rs: ResultSet, ctx: StatementContext): UserEntity {
            return UserEntity(
                id = rs.getInt("userId"),
                email = rs.getString("email"),
                firstName = rs.getString("firstName"),
                lastName = rs.getString("lastName"),
                createdAt = rs.getTimestamp("createdAt").time,
                deletedAt = rs.getTimestamp("deletedAt")?.time
            )
        }
    }
}
