package com.usermanagement.api.models.users

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotBlank

data class AssignUserModel(
    @JsonProperty("groupId")
    @NotBlank
    val groupId: Int,

    @JsonProperty("responsibility")
    @field:Length(min = 1, max = 255)
    val responsibility: String
)
