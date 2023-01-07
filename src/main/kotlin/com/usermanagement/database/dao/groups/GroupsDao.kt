package com.usermanagement.database.dao.groups

import com.usermanagement.database.entity.GroupEntity
import org.jdbi.v3.core.Jdbi
import java.sql.Timestamp

class GroupsDao(database: Jdbi) : IGroupsDao {
    private val queries = database.onDemand(GroupsQueries::class.java)

    init {
        queries.createTable()
    }

    override fun getList(
        limit: Int,
        offset: Int,
        deleted: Boolean
    ): List<GroupEntity> {
        return if (deleted) {
            queries.getAll(limit = limit, offset = offset)
        } else {
            queries.getActive(limit = limit, offset = offset)
        }
    }

    override fun insert(
        name: String,
        description: String,
        createdAt: Timestamp
    ): Int? =
        queries.insert(name = name, description = description, createdAt = createdAt)

    override fun getById(id: Int): GroupEntity? = queries.getById(id = id)

    override fun getByName(name: String): GroupEntity? = queries.getByName(name = name)

    override fun delete(id: Int, deletedAt: Timestamp) =
        queries.delete(id = id, deletedAt = deletedAt)
}
