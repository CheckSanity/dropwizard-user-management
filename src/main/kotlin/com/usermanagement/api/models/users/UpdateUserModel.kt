package com.usermanagement.api.models.users

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.Email

data class UpdateUserModel(
    @JsonProperty("firstName")
    @field:Length(min = 1, max = 100)
    val firstName: String? = null,

    @JsonProperty("lastName")
    @field:Length(min = 1, max = 100)
    val lastName: String? = null,

    @JsonProperty("email")
    @field:Email
    val email: String? = null
)
