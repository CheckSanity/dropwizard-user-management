package model

import com.fasterxml.jackson.databind.ObjectMapper
import com.usermanagement.api.models.users.*
import org.junit.jupiter.api.Test
import utils.TestUser
import utils.TestUser.Companion.toModel
import kotlin.test.assertEquals

internal class UserModelTests {
    @Test
    fun `User model serialization`() {
        val model = TestUser.create(id = 1, deleted = false).toModel()
        with(ObjectMapper()) {
            assertEquals(
                readTree(writeValueAsString(model)),
                readTree(model.toJson())
            )
        }
    }

    @Test
    fun `User model deserialization`() {
        val model = TestUser.create(id = 1, deleted = false).toModel()
        with(ObjectMapper()) {
            assertEquals(
                readerFor(UserModel::class.java).readValue(model.toJson()),
                model
            )
        }
    }

    @Test
    fun `New user model serialization`() {
        val model = NewUserModel(
            firstName = "First name",
            lastName = "Last name",
            email = "email@email.com"
        )
        with(ObjectMapper()) {
            assertEquals(
                readTree(writeValueAsString(model)),
                readTree(model.toJson())
            )
        }
    }

    @Test
    fun `New User model deserialization`() {
        val model = NewUserModel(
            firstName = "First name",
            lastName = "Last name",
            email = "email@email.com"
        )

        with(ObjectMapper()) {
            assertEquals(
                readerFor(NewUserModel::class.java).readValue(model.toJson()),
                model
            )
        }
    }

    @Test
    fun `Assign user model serialization`() {
        val model = AssignUserModel(
            groupId = 1,
            responsibility = "Some value"
        )
        with(ObjectMapper()) {
            assertEquals(
                readTree(writeValueAsString(model)),
                readTree(model.toJson())
            )
        }
    }

    @Test
    fun `Assign User model deserialization`() {
        val model = AssignUserModel(
            groupId = 1,
            responsibility = "Some value"
        )
        with(ObjectMapper()) {
            assertEquals(
                readerFor(AssignUserModel::class.java).readValue(model.toJson()),
                model
            )
        }
    }

    @Test
    fun `Unassign user model serialization`() {
        val model = UnassignUserModel(groupId = 1)
        with(ObjectMapper()) {
            assertEquals(
                readTree(writeValueAsString(model)),
                readTree(model.toJson())
            )
        }
    }

    @Test
    fun `Unassign User model deserialization`() {
        val model = UnassignUserModel(groupId = 1)
        with(ObjectMapper()) {
            assertEquals(
                readerFor(UnassignUserModel::class.java).readValue(model.toJson()),
                model
            )
        }
    }

    @Test
    fun `Update user model serialization`() {
        val model = UpdateUserModel(firstName = "Updated")
        with(ObjectMapper()) {
            assertEquals(
                readTree(writeValueAsString(model)),
                readTree(model.toJson())
            )
        }
    }

    @Test
    fun `Update user model deserialization`() {
        val model = UpdateUserModel(firstName = "Updated")
        with(ObjectMapper()) {
            assertEquals(
                readerFor(UpdateUserModel::class.java).readValue(model.toJson()),
                model
            )
        }
    }

    companion object {
        private fun NewUserModel.toJson() = """{
                "firstName": "$firstName",
                "lastName": "$lastName",
                "email": "$email"
            }"""

        private fun UserModel.toJson() = """{
                "id": $id,
                "firstName": "$firstName",
                "lastName": "$lastName",
                "email": "$email",
                "createdAt": $createdAt,
                "deletedAt": $deletedAt
            }"""

        private fun UpdateUserModel.toJson() = """{
                "firstName": "$firstName",
                "lastName": null,
                "email": null
            }"""

        private fun AssignUserModel.toJson() = """{
                "groupId": $groupId,
                "responsibility": "$responsibility"
            }"""

        private fun UnassignUserModel.toJson() = """{
                "groupId": $groupId
            }"""
    }
}
