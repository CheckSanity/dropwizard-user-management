package com.usermanagement.api.models.users

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank

data class UnassignUserModel(
    @JsonProperty("groupId")
    @NotBlank
    val groupId: Int
)
