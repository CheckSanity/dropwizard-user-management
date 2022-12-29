package com.usermanagement.database.dao.groupUsers

import com.usermanagement.database.entity.GroupUsersEntity
import org.jdbi.v3.sqlobject.config.RegisterRowMapper
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

@RegisterRowMapper(GroupUsersEntity.Mapper::class)
interface GroupUsersQueries {
    @SqlUpdate(
        "CREATE TABLE IF NOT EXISTS group_users " +
                "(PRIMARY KEY(groupId, userId), " +
                "groupId INT, " +
                "userId INT, " +
                "FOREIGN KEY (groupId) REFERENCES groups(groupId), " +
                "FOREIGN KEY (userId) REFERENCES users(userId), " +
                "responsibility VARCHAR)"
    )
    fun createTable()

    @SqlQuery("SELECT * FROM group_users WHERE groupId = :groupId AND userId = :userId")
    fun getByGroupUserId(
        @Bind("groupId") groupId: Int,
        @Bind("userId") userId: Int
    ): GroupUsersEntity?

    @SqlUpdate("INSERT INTO group_users (groupId, userId, responsibility) VALUES (:groupId, :userId, :responsibility)")
    fun insert(
        @Bind("groupId") groupId: Int,
        @Bind("userId") userId: Int,
        @Bind("responsibility") responsibility: String
    )

    @SqlUpdate("DELETE FROM group_users WHERE groupId = :groupId AND userId = :userId")
    fun delete(
        @Bind("groupId") groupId: Int,
        @Bind("userId") userId: Int
    )
}
