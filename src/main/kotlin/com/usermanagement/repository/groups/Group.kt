package com.usermanagement.repository.groups

import com.usermanagement.database.entity.GroupEntity

data class Group(
    val id: Int,
    val name: String,
    val description: String,
    val createdAt: Long,
    val deletedAt: Long?
) {
    companion object {
        fun GroupEntity.toData() = Group(
            id = this.id,
            name = this.name,
            description = this.description,
            createdAt = this.createdAt,
            deletedAt = this.deletedAt
        )

        fun List<GroupEntity>.toData() = this.map {
            it.toData()
        }
    }
}
