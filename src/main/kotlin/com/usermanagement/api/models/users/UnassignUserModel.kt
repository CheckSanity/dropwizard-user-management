package com.usermanagement.api.models.users

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotBlank

data class UnassignUserModel(
    @ApiModelProperty(
        value = "Group ID",
        example = "1",
        required = true,
        dataType = "integer"
    )
    @JsonProperty("groupId")
    @NotBlank
    val groupId: Int
)
