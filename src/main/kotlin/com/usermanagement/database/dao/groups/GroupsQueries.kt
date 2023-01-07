package com.usermanagement.database.dao.groups

import com.usermanagement.database.entity.GroupEntity
import org.jdbi.v3.sqlobject.config.RegisterRowMapper
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import java.sql.Timestamp
import java.util.*

@RegisterRowMapper(GroupEntity.Mapper::class)
interface GroupsQueries {
    @SqlUpdate(
        "CREATE TABLE IF NOT EXISTS `groups` " +
                "(groupId INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT," +
                "name VARCHAR(100) UNIQUE NOT NULL, " +
                "description TEXT NOT NULL, " +
                "createdat TIMESTAMP, " +
                "deletedat TIMESTAMP DEFAULT(NULL))"
    )
    fun createTable()

    @SqlQuery("SELECT * FROM `groups` LIMIT :limit OFFSET :offset")
    fun getAll(
        @Bind("limit") limit: Int,
        @Bind("offset") offset: Int,
    ): List<GroupEntity>

    @SqlQuery("SELECT * FROM `groups` WHERE deletedAt IS NULL LIMIT :limit OFFSET :offset")
    fun getActive(
        @Bind("limit") limit: Int,
        @Bind("offset") offset: Int,
    ): List<GroupEntity>

    @SqlUpdate("INSERT INTO `groups` (name, description, createdAt) VALUES (:name, :description, :createdAt)")
    @GetGeneratedKeys
    fun insert(
        @Bind("name") name: String,
        @Bind("description") description: String,
        @Bind("createdAt") createdAt: Timestamp
    ): Int?

    @SqlQuery("SELECT * FROM `groups` WHERE groupId = :id")
    fun getById(@Bind("id") id: Int): GroupEntity?

    @SqlQuery("SELECT * FROM `groups` WHERE name = :name")
    fun getByName(@Bind("name") name: String): GroupEntity?

    @SqlUpdate("UPDATE `groups` SET deletedAt = :deletedAt WHERE groupId = :id")
    fun delete(
        @Bind("id") id: Int,
        @Bind("deletedAt") deletedAt: Timestamp
    )
}
