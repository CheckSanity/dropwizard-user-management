package com.usermanagement.api.models.users

import com.fasterxml.jackson.annotation.JsonProperty
import com.usermanagement.repository.users.User

data class UserModel(
    @JsonProperty("id")
    val id: Int,

    @JsonProperty("firstName")
    val firstName: String,

    @JsonProperty("lastName")
    val lastName: String,

    @JsonProperty("email")
    val email: String,

    @JsonProperty("createdAt")
    val createdAt: Long,

    @JsonProperty("deletedAt")
    val deletedAt: Long?
) {
    companion object {
        fun List<User>.toModel() = this.map {
            it.toModel()
        }

        fun User.toModel() = UserModel(
            id = this.id,
            firstName = this.firstName,
            lastName = this.lastName,
            email = this.email,
            createdAt = this.createdAt,
            deletedAt = this.deletedAt
        )
    }
}
