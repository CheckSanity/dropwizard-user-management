package com.usermanagement.repository.users

import com.usermanagement.database.entity.GroupUsersEntity

data class UserResponsibility(
    val responsibility: String
) {
    companion object {
        fun GroupUsersEntity.toData() = UserResponsibility(
            responsibility = this.responsibility
        )
    }
}
