package com.usermanagement.api.models.groups

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.validator.constraints.Length

data class NewGroupModel(
    @JsonProperty("name")
    @field:Length(min = 1, max = 255)
    val name: String,

    @JsonProperty("description")
    @field:Length(min = 1, max = 2000)
    val description: String,
)
