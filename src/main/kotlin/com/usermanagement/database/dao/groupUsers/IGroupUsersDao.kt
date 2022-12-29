package com.usermanagement.database.dao.groupUsers

import com.usermanagement.database.entity.GroupUsersEntity

interface IGroupUsersDao {
    fun insert(
        userId: Int,
        groupId: Int,
        responsibility: String
    )

    fun delete(
        userId: Int,
        groupId: Int,
    )

    fun getByGroupUserId(userId: Int, groupId: Int): GroupUsersEntity?
}
