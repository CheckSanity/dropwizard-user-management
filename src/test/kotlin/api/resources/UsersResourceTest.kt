package api.resources

import com.github.kittinunf.result.Result
import com.usermanagement.api.models.users.UserModel
import com.usermanagement.api.resources.users.UsersResource
import com.usermanagement.repository.users.IUsersRepository
import com.usermanagement.repository.users.NewUser
import com.usermanagement.repository.users.UpdateUser
import com.usermanagement.utils.RepositoryError
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport
import io.dropwizard.testing.junit5.ResourceExtension
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.Mockito.reset
import utils.TestUser
import utils.TestUser.Companion.toData
import utils.TestUser.Companion.toModel
import javax.ws.rs.client.Entity
import javax.ws.rs.core.Response
import kotlin.test.assertEquals


@ExtendWith(DropwizardExtensionsSupport::class)
internal class UsersResourceTest {
    private val repository: IUsersRepository = Mockito.mock(IUsersRepository::class.java)
    private val ext = ResourceExtension.builder()
        .addResource(UsersResource(repository))
        .build()

    @AfterEach
    fun tearDown() {
        reset(repository)
    }

    @Nested
    inner class UsersTests {
        @Test
        fun `Get users`() {
            Mockito.`when`(
                repository.getUsers(
                    limit = 25,
                    offset = 0,
                    deleted = false,
                    sort = "userId",
                    order = "ASC"
                )
            ).thenReturn(
                Result.success(
                    listOf(
                        TestUser.create(id = 1, deleted = false).toData(),
                        TestUser.create(id = 2, deleted = false).toData(),
                        TestUser.create(id = 3, deleted = false).toData(),
                        TestUser.create(id = 4, deleted = false).toData(),
                        TestUser.create(id = 5, deleted = false).toData(),
                        TestUser.create(id = 6, deleted = false).toData(),
                    )
                )
            )

            val users = ext.target("/users").request().get(List::class.java)
            assert(users.isNotEmpty())
            assertEquals(users.size, 6)
        }

        @Test
        fun `Get users with limit and offset`() {
            Mockito.`when`(
                repository.getUsers(
                    limit = 2,
                    offset = 2,
                    deleted = false,
                    sort = "userId",
                    order = "ASC"
                )
            ).thenReturn(
                Result.success(
                    listOf(
                        TestUser.create(id = 3, deleted = false).toData(),
                        TestUser.create(id = 4, deleted = false).toData()
                    )
                )
            )

            val users = ext.target("/users")
                .queryParam("limit", 2)
                .queryParam("offset", 2)
                .queryParam("sort", "userId")
                .queryParam("order", "ASC")
                .queryParam("show_deleted", false)
                .request()
                .get(List::class.java)

            assert(users.isNotEmpty())
            assertEquals(users.size, 2)
        }

        @Test
        fun `Get users with incorrect limit`() {
            val response: Response = ext.target("/users")
                .queryParam("limit", -1)
                .request()
                .get()

            assertEquals(response.status, Response.Status.BAD_REQUEST.statusCode)
        }

        @Test
        fun `Get users with incorrect offset`() {
            val response: Response = ext.target("/users")
                .queryParam("offset", -1)
                .request()
                .get()

            assertEquals(response.status, Response.Status.BAD_REQUEST.statusCode)
        }

        @Test
        fun `Get users with incorrect sort type`() {
            val response: Response = ext.target("/users")
                .queryParam("sort", "something")
                .request()
                .get()

            assertEquals(response.status, Response.Status.BAD_REQUEST.statusCode)
        }
    }

    @Nested
    inner class UserTests {
        @Test
        fun `Get user by id`() {
            val testUser = TestUser.create(id = 1, deleted = false)

            Mockito.`when`(
                repository.getUser(id = 1)
            ).thenReturn(
                Result.success(testUser.toData())
            )

            val user = ext.target("/users").path("1").request().get(UserModel::class.java)
            assertEquals(user, testUser.toModel())
        }

        @Test
        fun `Get non-existed user by id`() {
            Mockito.`when`(
                repository.getUser(id = 1)
            ).thenReturn(
                Result.failure(RepositoryError.UserNotFound)
            )

            val response = ext.target("/users").path("1").request().get()

            assertEquals(response.status, Response.Status.NOT_FOUND.statusCode)
        }
    }

    @Nested
    inner class CreateUserTests {
        @Test
        fun `Create user`() {
            val testUser = TestUser.create(id = 1, deleted = false)

            Mockito.`when`(
                repository.createUser(
                    newUser = NewUser(
                        firstName = testUser.firstName,
                        lastName = testUser.lastName,
                        email = testUser.email
                    )
                )
            ).thenReturn(
                Result.success(testUser.toData())
            )

            val user = ext.target("/users").request().post(
                Entity.json("""{"firstName": "${testUser.firstName}", "lastName": "${testUser.lastName}", "email": "${testUser.email}"}"""),
                UserModel::class.java
            )
            assertEquals(user, testUser.toModel())
        }

        @Test
        fun `Create user with existed e-mail`() {
            val testUser = TestUser.create(id = 1, deleted = false)

            Mockito.`when`(
                repository.createUser(
                    newUser = NewUser(
                        firstName = testUser.firstName,
                        lastName = testUser.lastName,
                        email = testUser.email
                    )
                )
            ).thenReturn(
                Result.failure(RepositoryError.UserEmailInUse)
            )

            val response = ext.target("/users").request().post(
                Entity.json("""{"firstName": "${testUser.firstName}", "lastName": "${testUser.lastName}", "email": "${testUser.email}"}"""),
            )
            assertEquals(response.status, Response.Status.CONFLICT.statusCode)
        }

        @Test
        fun `Create user with not valid e-mail`() {
            val testUser = TestUser.create(id = 1, deleted = false)

            val response = ext.target("/users").request().post(
                Entity.json("""{"firstName": "${testUser.firstName}", "lastName": "${testUser.lastName}", "email": "test.com"}"""),
            )
            assertEquals(response.status, 422)
        }

        @Test
        fun `Create user with not valid JSON`() {
            val response = ext.target("/users").request().post(
                Entity.json("""{"firstName": ""}"""),
            )
            assertEquals(response.status, 400)
        }
    }

    @Nested
    inner class DeleteUserTests {
        @Test
        fun `Delete user`() {
            val testUser = TestUser.create(id = 1, deleted = true)
            Mockito
                .`when`(repository.deleteUser(userId = testUser.id))
                .thenReturn(Result.success(testUser.toData()))

            val deletedUser = ext.target("/users")
                .path(testUser.id.toString())
                .request()
                .delete(
                    UserModel::class.java
                )
            assertEquals(deletedUser, testUser.toModel())
        }

        @Test
        fun `Delete user that already deleted`() {
            Mockito
                .`when`(repository.deleteUser(userId = 1))
                .thenReturn(Result.failure(RepositoryError.UserDeleted))

            val response = ext.target("/users")
                .path("1")
                .request()
                .delete()
            assertEquals(response.status, Response.Status.NOT_FOUND.statusCode)
        }

        @Test
        fun `Delete user that not existed`() {
            Mockito
                .`when`(repository.deleteUser(userId = 1))
                .thenReturn(Result.failure(RepositoryError.UserNotFound))

            val response = ext.target("/users")
                .path("1")
                .request()
                .delete()
            assertEquals(response.status, Response.Status.NOT_FOUND.statusCode)
        }
    }

    @Nested
    inner class UpdateUserTests {
        @Test
        fun `Update user`() {
            val testUser = TestUser.create(id = 1, deleted = false).copy(
                firstName = "Updated first name",
                lastName = "Updated last name",
                email = "updated@email.com"
            )

            Mockito
                .`when`(
                    repository.updateUser(
                        userId = testUser.id, updateUser = UpdateUser(
                            firstName = testUser.firstName,
                            lastName = testUser.lastName,
                            email = testUser.email
                        )
                    )
                )
                .thenReturn(Result.success(testUser.toData()))

            val updatedUser = ext.target("/users")
                .path(testUser.id.toString())
                .request()
                .put(
                    Entity.json("""{"firstName": "${testUser.firstName}", "lastName": "${testUser.lastName}", "email": "${testUser.email}"}"""),
                    UserModel::class.java
                )
            assertEquals(updatedUser, testUser.toModel())
        }

        @Test
        fun `Update user with only one field`() {
            val testUser = TestUser.create(id = 1, deleted = false).copy(
                firstName = "Updated first name"
            )

            Mockito
                .`when`(
                    repository.updateUser(
                        userId = testUser.id, updateUser = UpdateUser(
                            firstName = testUser.firstName,
                            lastName = null,
                            email = null
                        )
                    )
                )
                .thenReturn(Result.success(testUser.toData()))

            val updatedUser = ext.target("/users")
                .path(testUser.id.toString())
                .request()
                .put(
                    Entity.json("""{"firstName": "${testUser.firstName}"}"""),
                    UserModel::class.java
                )
            assertEquals(updatedUser, testUser.toModel())
        }

        @Test
        fun `Update user that already deleted`() {
            val testUser = TestUser.create(id = 1, deleted = false).copy(
                firstName = "Updated first name"
            )

            Mockito
                .`when`(
                    repository.updateUser(
                        userId = testUser.id, updateUser = UpdateUser(
                            firstName = testUser.firstName,
                            lastName = null,
                            email = null
                        )
                    )
                )
                .thenReturn(Result.failure(RepositoryError.UserDeleted))

            val response = ext.target("/users")
                .path("1")
                .request()
                .put(Entity.json("""{"firstName": "${testUser.firstName}"}"""))
            assertEquals(response.status, Response.Status.NOT_FOUND.statusCode)
        }

        @Test
        fun `Update user that not existed`() {
            val testUser = TestUser.create(id = 1, deleted = false).copy(
                firstName = "Updated first name"
            )

            Mockito
                .`when`(
                    repository.updateUser(
                        userId = testUser.id, updateUser = UpdateUser(
                            firstName = testUser.firstName,
                            lastName = null,
                            email = null
                        )
                    )
                )
                .thenReturn(Result.failure(RepositoryError.UserNotFound))

            val response = ext.target("/users")
                .path("1")
                .request()
                .put(Entity.json("""{"firstName": "${testUser.firstName}"}"""))
            assertEquals(response.status, Response.Status.NOT_FOUND.statusCode)
        }
    }
}
