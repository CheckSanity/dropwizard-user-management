package com.usermanagement.repository.users

import com.usermanagement.database.entity.UserEntity

data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val createdAt: Long,
    val deletedAt: Long?
) {
    companion object {
        fun UserEntity.toData() = User(
            id = this.id,
            firstName = this.firstName,
            lastName = this.lastName,
            email = this.email,
            createdAt = this.createdAt,
            deletedAt = this.deletedAt
        )

        fun List<UserEntity>.toData() = this.map {
            it.toData()
        }
    }
}
