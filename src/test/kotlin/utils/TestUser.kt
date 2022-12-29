package utils

import com.usermanagement.api.models.users.UserModel

data class TestUser(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val createdAt: Long,
    val deletedAt: Long?
) {
    companion object {
        fun create(id: Int, deleted: Boolean) = TestUser(
            id = id,
            firstName = "Name $id",
            lastName = "Last name $id",
            email = "email_$id@test.com",
            createdAt = 1672321020,
            deletedAt = if (deleted) 1672321020 else null
        )

        fun TestUser.toModel() = UserModel(
            id = this.id,
            firstName = this.firstName,
            lastName = this.lastName,
            email = this.email,
            createdAt = this.createdAt,
            deletedAt = this.deletedAt
        )
    }
}
