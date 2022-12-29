package com.usermanagement.api.models.users

import com.fasterxml.jackson.annotation.JsonProperty
import com.usermanagement.repository.users.User
import io.swagger.annotations.ApiModelProperty

data class UserModel(
    @ApiModelProperty(
        value = "User's unique ID",
        example = "1",
        required = true,
        dataType = "integer"
    )
    @JsonProperty("id")
    val id: Int,

    @ApiModelProperty(
        value = "User's first name",
        example = "Gary",
        required = true,
        dataType = "string"
    )
    @JsonProperty("firstName")
    val firstName: String,

    @ApiModelProperty(
        value = "User's last name",
        example = "Bezruchko",
        required = true,
        dataType = "string"
    )
    @JsonProperty("lastName")
    val lastName: String,

    @ApiModelProperty(
        value = "User's email in valid format",
        example = "garik@bezruchko.com",
        required = true,
        dataType = "string"
    )
    @JsonProperty("email")
    val email: String,

    @ApiModelProperty(
        value = "User creation date",
        example = "1672321020",
        required = true,
        dataType = "integer"
    )
    @JsonProperty("createdAt")
    val createdAt: Long,

    @ApiModelProperty(
        value = "User deletion date",
        example = "1672321020",
        dataType = "integer"
    )
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
