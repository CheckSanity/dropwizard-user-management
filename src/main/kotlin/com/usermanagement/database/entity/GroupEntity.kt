package com.usermanagement.database.entity

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet


data class GroupEntity(
    val id: Int,
    val name: String,
    val description: String,
    val createdAt: Long,
    val deletedAt: Long?
) {
    internal class Mapper : RowMapper<GroupEntity> {
        override fun map(rs: ResultSet, ctx: StatementContext): GroupEntity {
            return GroupEntity(
                id = rs.getInt("groupId"),
                name = rs.getString("name"),
                description = rs.getString("description"),
                createdAt = rs.getTimestamp("createdAt").time,
                deletedAt = rs.getTimestamp("deletedAt")?.time
            )
        }
    }
}
