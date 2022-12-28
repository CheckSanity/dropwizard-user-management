package com.usermanagement.database.dao

import com.usermanagement.database.entity.GroupEntity
import org.jdbi.v3.sqlobject.config.RegisterRowMapper
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import java.sql.Timestamp
import java.time.Instant
import java.util.*

@RegisterRowMapper(GroupEntity.Mapper::class)
interface IGroupsDao {
    @SqlUpdate(
        "CREATE TABLE IF NOT EXISTS groups " +
                "(id INTEGER PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY," +
                "name VARCHAR, " +
                "description TEXT, " +
                "createdAt TIMESTAMP, " +
                "deletedAt TIMESTAMP DEFAULT(NULL))"
    )
    fun createTable()

    @SqlQuery("SELECT * FROM groups LIMIT :limit OFFSET :offset")
    fun getAll(
        @Bind("limit") limit: Int,
        @Bind("offset") offset: Int,
        @Bind("deleted") deleted: Boolean
    ): List<GroupEntity>

    @SqlUpdate("INSERT INTO groups (name, description, createdAt) VALUES (:name, :description, :createdAt)")
    @GetGeneratedKeys
    fun insert(
        @Bind("name") name: String,
        @Bind("description") description: String,
        @Bind("createdAt") createdAt: Timestamp = Timestamp.from(Instant.now())
    ): Int?

    @SqlQuery("SELECT * FROM groups where id = :id")
    fun getById(@Bind("id") id: Int): GroupEntity?
}
