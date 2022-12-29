package com.usermanagement.api.models.users

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.Email

data class UpdateUserModel(
    @ApiModelProperty(
        value = "User's first name",
        example = "Gary",
        required = false,
        dataType = "string"
    )
    @JsonProperty("firstName")
    @field:Length(min = 1, max = 100)
    val firstName: String? = null,

    @ApiModelProperty(
        value = "User's last name",
        example = "Bezruchko",
        required = false,
        dataType = "string"
    )
    @JsonProperty("lastName")
    @field:Length(min = 1, max = 100)
    val lastName: String? = null,

    @ApiModelProperty(
        value = "User's email in valid format",
        example = "garik@bezruchko.com",
        required = false,
        dataType = "string"
    )
    @JsonProperty("email")
    @field:Email
    val email: String? = null
)
