package com.usermanagement.database.entity

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet

data class GroupUsersEntity(
    val userId: Int,
    val groupId: Int,
    val responsibility: String,
) {
    internal class Mapper : RowMapper<GroupUsersEntity> {
        override fun map(rs: ResultSet, ctx: StatementContext): GroupUsersEntity {
            return GroupUsersEntity(
                groupId = rs.getInt("groupId"),
                userId = rs.getInt("userId"),
                responsibility = rs.getString("responsibility"),
            )
        }
    }
}
