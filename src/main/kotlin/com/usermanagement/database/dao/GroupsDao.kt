package com.usermanagement.database.dao

import com.usermanagement.database.entity.GroupEntity
import org.jdbi.v3.core.Jdbi
import java.sql.Timestamp

class GroupsDao(database: Jdbi) : IGroupsDao {
    private val dao = database.onDemand(IGroupsDao::class.java)

    init {
        createTable()
    }

    override fun createTable() = dao.createTable()

    override fun getAll(
        limit: Int,
        offset: Int,
        deleted: Boolean
    ): List<GroupEntity> =
        dao.getAll(limit = limit, offset = offset, deleted = deleted)

    override fun insert(
        name: String,
        description: String,
        createdAt: Timestamp
    ): Int? =
        dao.insert(name = name, description = description, createdAt = createdAt)

    override fun getById(id: Int): GroupEntity? = dao.getById(id = id)
}
