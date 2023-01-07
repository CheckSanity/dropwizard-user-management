package com.usermanagement.api.models.users

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.Email

data class NewUserModel(
    @ApiModelProperty(
        value = "User's first name",
        example = "Gary",
        required = true,
        dataType = "string"
    )
    @JsonProperty("firstName")
    @field:Length(min = 1, max = 100)
    val firstName: String,

    @ApiModelProperty(
        value = "User's last name",
        example = "Bezruchko",
        required = true,
        dataType = "string"
    )
    @JsonProperty("lastName")
    @field:Length(min = 1, max = 100)
    val lastName: String,

    @ApiModelProperty(
        value = "User's email in valid format",
        example = "garik@bezruchko.com",
        required = true,
        dataType = "string"
    )
    @JsonProperty("email")
    @field:Email
    val email: String
)
