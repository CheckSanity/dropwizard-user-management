package com.usermanagement.api.models.groups

import com.fasterxml.jackson.annotation.JsonProperty
import com.usermanagement.repository.groups.Group

data class GroupModel(
    @JsonProperty("id")
    val id: Int,

    @JsonProperty("name")
    val name: String,

    @JsonProperty("description")
    val description: String,

    @JsonProperty("createdAt")
    val createdAt: Long,

    @JsonProperty("deletedAt")
    val deletedAt: Long?
) {
    companion object {
        fun List<Group>.toModel() = this.map {
            it.toModel()
        }

        fun Group.toModel() = GroupModel(
            id = this.id,
            name = this.name,
            description = this.description,
            createdAt = this.createdAt,
            deletedAt = this.deletedAt
        )
    }
}
