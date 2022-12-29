package com.usermanagement.api.models.users

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotBlank

data class AssignUserModel(
    @ApiModelProperty(
        value = "Group ID",
        example = "1",
        required = true,
        dataType = "integer"
    )
    @JsonProperty("groupId")
    @NotBlank
    val groupId: Int,

    @ApiModelProperty(
        value = "User responsibility in group",
        example = "Some very very very important responsibility",
        required = true,
        dataType = "string"
    )
    @JsonProperty("responsibility")
    @field:Length(min = 1, max = 255)
    val responsibility: String
)
