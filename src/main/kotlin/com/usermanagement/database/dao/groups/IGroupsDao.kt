package com.usermanagement.database.dao.groups

import com.usermanagement.database.entity.GroupEntity
import java.sql.Timestamp
import java.time.Instant

interface IGroupsDao {
    fun getList(limit: Int, offset: Int, deleted: Boolean): List<GroupEntity>

    fun insert(
        name: String,
        description: String,
        createdAt: Timestamp = Timestamp.from(Instant.now())
    ): Int?

    fun getById(id: Int): GroupEntity?

    fun getByName(name: String): GroupEntity?

    fun delete(
        id: Int,
        deletedAt: Timestamp = Timestamp.from(Instant.now())
    )
}
