package api

import com.usermanagement.App
import com.usermanagement.AppConfiguration
import com.usermanagement.api.models.users.UserModel
import com.usermanagement.database.dao.users.UsersQueries
import io.dropwizard.configuration.ResourceConfigurationSourceProvider
import io.dropwizard.testing.junit5.DropwizardAppExtension
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import utils.DatabaseUtils
import utils.DatabaseUtils.fillTableWithUsers
import utils.TestUser
import utils.TestUser.Companion.toModel
import javax.ws.rs.client.Entity
import javax.ws.rs.core.GenericType
import kotlin.test.assertEquals

@ExtendWith(DropwizardExtensionsSupport::class)
internal class UsersApiTest {
    private val jdbi: Jdbi = DatabaseUtils.createTestJdbi()

    private val testApp: DropwizardAppExtension<AppConfiguration> =
        DropwizardAppExtension(
            App::class.java,
            "app-config-test.yml",
            ResourceConfigurationSourceProvider()
        )

    @BeforeEach
    fun setUp() {
        val queries = jdbi.onDemand(UsersQueries::class.java)
        queries.createTable()

        jdbi.fillTableWithUsers(5)
    }

    @AfterEach
    fun drop() {
        jdbi.withHandle<Any, Exception> { handle ->
            handle.execute("DROP TABLE IF EXISTS users;")
        }
    }

    @Test
    fun `Get users`() {
        val users = testApp.client()
            .target("http://localhost:${testApp.localPort}/users")
            .request()
            .get(object : GenericType<List<UserModel>>() {})
        assertEquals(
            users,
            listOf(
                TestUser.create(1, deleted = false).toModel(),
                TestUser.create(2, deleted = false).toModel(),
                TestUser.create(3, deleted = false).toModel(),
                TestUser.create(4, deleted = false).toModel(),
                TestUser.create(5, deleted = false).toModel()
            )
        )
    }

    @Test
    fun `Get users with limit`() {
        val users = testApp.client()
            .target("http://localhost:${testApp.localPort}/users?limit=2")
            .request()
            .get(object : GenericType<List<UserModel>>() {})
        assertEquals(
            users,
            listOf(
                TestUser.create(1, deleted = false).toModel(),
                TestUser.create(2, deleted = false).toModel(),
            )
        )
    }

    @Test
    fun `Get users with limit and offset`() {
        val users = testApp.client()
            .target("http://localhost:${testApp.localPort}/users?limit=2&offset=2")
            .request()
            .get(object : GenericType<List<UserModel>>() {})
        assertEquals(
            users,
            listOf(
                TestUser.create(3, deleted = false).toModel(),
                TestUser.create(4, deleted = false).toModel(),
            )
        )
    }

    @Test
    fun `Get users with sort & order`() {
        val users = testApp.client()
            .target("http://localhost:${testApp.localPort}/users?limit=2&sort=email&order=DESC")
            .request()
            .get(object : GenericType<List<UserModel>>() {})
        assertEquals(
            users,
            listOf(
                TestUser.create(5, deleted = false).toModel(),
                TestUser.create(4, deleted = false).toModel(),
            )
        )
    }

    @Test
    fun `Create user`() {
        val response = testApp.client()
            .target("http://localhost:${testApp.localPort}/users")
            .request()
            .post(
                Entity.json("{\"firstName\": \"Name 6\", \"lastName\": \"Last name 6\", \"email\": \"email_6@test.com\"}")
            )

        val actualUser = response.readEntity(UserModel::class.java)
        val expectedUser = TestUser.create(6, deleted = false).toModel()

        assertEquals(actualUser.id, expectedUser.id)
        assertEquals(actualUser.email, expectedUser.email)
    }

    @Test
    fun `Update user`() {
        val response = testApp.client()
            .target("http://localhost:${testApp.localPort}/users/5")
            .request()
            .put(
                Entity.json("{\"firstName\": \"Name updated\"}")
            )

        val actualUser = response.readEntity(UserModel::class.java)
        val expectedUser =
            TestUser.create(5, deleted = false).copy(firstName = "Name updated").toModel()

        assertEquals(actualUser.firstName, expectedUser.firstName)
    }

    @Test
    fun `Delete user`() {
        val response = testApp.client()
            .target("http://localhost:${testApp.localPort}/users/5")
            .request()
            .delete()

        val actualUser = response.readEntity(UserModel::class.java)
        val expectedUser = TestUser.create(5, deleted = true).toModel()

        assertEquals(actualUser.firstName, expectedUser.firstName)
        assert(actualUser.deletedAt != null)
    }
}
