package com.usermanagement.database.dao.groupUsers

import org.jdbi.v3.core.Jdbi

class GroupUsersDao(database: Jdbi) : IGroupUsersDao {
    private val queries = database.onDemand(GroupUsersQueries::class.java)

    init {
        queries.createTable()
    }

    override fun insert(userId: Int, groupId: Int, responsibility: String) =
        queries.insert(userId = userId, groupId = groupId, responsibility = responsibility)

    override fun delete(userId: Int, groupId: Int) =
        queries.delete(groupId = groupId, userId = userId)

    override fun getByGroupUserId(userId: Int, groupId: Int) =
        queries.getByGroupUserId(groupId = groupId, userId = userId)
}
