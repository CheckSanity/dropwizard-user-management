package com.usermanagement.api.models.users

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.Email

data class NewUserModel(
    @JsonProperty("firstName")
    @field:Length(min = 1, max = 100)
    val firstName: String,

    @JsonProperty("lastName")
    @field:Length(min = 1, max = 100)
    val lastName: String,

    @JsonProperty("email")
    @field:Email
    val email: String
)
