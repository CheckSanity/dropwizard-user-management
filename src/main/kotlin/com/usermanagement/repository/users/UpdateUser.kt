package com.usermanagement.repository.users

data class UpdateUser(
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val deleted: Boolean = false
)
