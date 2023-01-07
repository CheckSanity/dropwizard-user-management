package database

import com.usermanagement.database.dao.users.UsersDao
import com.usermanagement.database.entity.UserEntity
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import utils.DatabaseUtils
import utils.DatabaseUtils.fillTableWithUsers
import utils.TestUser
import java.sql.Timestamp
import java.time.Instant
import kotlin.test.assertEquals

internal class UsersDaoTest {
    private val jdbi: Jdbi = DatabaseUtils.createTestJdbi()
    private val usersDao: UsersDao = UsersDao(jdbi)

    @BeforeEach
    fun setUp() {
        //
    }

    @AfterEach
    fun drop() {
        jdbi.withHandle<Any, Exception> { handle ->
            handle.execute("DROP TABLE IF EXISTS users;")
        }
    }

    @Test
    fun `Add user to table and check it`() {
        assert(usersDao.getList().isEmpty())

        jdbi.fillTableWithUsers(1)

        val list = usersDao.getList()

        assert(list.size == 1)

        val testUser = TestUser.create(id = 1, deleted = false)

        assertEquals(
            UserEntity(
                id = testUser.id,
                firstName = testUser.firstName,
                lastName = testUser.lastName,
                email = testUser.email,
                createdAt = testUser.createdAt,
                deletedAt = null
            ),
            list.first()
        )

        val user = usersDao.getById(1)

        assertEquals(
            UserEntity(
                id = testUser.id,
                firstName = testUser.firstName,
                lastName = testUser.lastName,
                email = testUser.email,
                createdAt = testUser.createdAt,
                deletedAt = null
            ),
            user
        )
    }

    @Test
    fun `Get users when table empty`() {
        assertEquals(usersDao.getList(), listOf())
    }

    @Test
    fun `Get users when table not empty`() {
        jdbi.fillTableWithUsers(10)

        val list = usersDao.getList()

        assert(list.size == 10)
    }

    @Test
    fun `Get users with deleted`() {
        jdbi.fillTableWithUsers(5)

        val initialList = usersDao.getList()
        assert(initialList.size == 5)

        usersDao.delete(4, Timestamp.from(Instant.now()))

        val listWithDeleted = usersDao.getList(deleted = true)
        assert(listWithDeleted.size == 5)

        val listWithoutDeleted = usersDao.getList(deleted = false)
        assert(listWithoutDeleted.size == 4)
    }

    @Test
    fun `Get users with limit`() {
        jdbi.fillTableWithUsers(10)
        val list = usersDao.getList(limit = 5)
        assert(list.size == 5)
    }

    @Test
    fun `Get users with offset`() {
        jdbi.fillTableWithUsers(10)
        val list = usersDao.getList(limit = 5, offset = 5)
        assert(list.size == 5)
        assertEquals(6, list.first().id)
    }

    @Test
    fun `Get users with sort by email`() {
        jdbi.fillTableWithUsers(3)

        val listAsc = usersDao.getList(sort = "email", order = "ASC")
        assertEquals(TestUser.create(id = 1, deleted = false).email, listAsc.first().email)

        val listDesc = usersDao.getList(sort = "email", order = "DESC")
        assertEquals(TestUser.create(id = 3, deleted = false).email, listDesc.first().email)
    }

    @Test
    fun `Get existed user by id`() {
        jdbi.fillTableWithUsers(3)

        val expectedUser = TestUser.create(id = 2, deleted = false)
        val actualUser = usersDao.getById(id = 2)

        assertEquals(
            UserEntity(
                id = expectedUser.id,
                firstName = expectedUser.firstName,
                lastName = expectedUser.lastName,
                email = expectedUser.email,
                createdAt = expectedUser.createdAt,
                deletedAt = expectedUser.deletedAt
            ), actualUser
        )
    }

    @Test
    fun `Get non-existed user by id`() {
        jdbi.fillTableWithUsers(3)

        val actualUser = usersDao.getById(id = 4)

        assertEquals(null, actualUser)
    }

    @Test
    fun `Update user by id`() {
        jdbi.fillTableWithUsers(3)

        val expectedUser = TestUser.create(id = 1, deleted = false).copy(
            firstName = "Updated name",
            lastName = "Updated last name",
            email = "update@test.com"
        )

        usersDao.update(
            id = 1,
            firstName = "Updated name",
            lastName = "Updated last name",
            email = "update@test.com"
        )

        val actualUser = usersDao.getById(id = 1)

        assertEquals(
            UserEntity(
                id = expectedUser.id,
                firstName = expectedUser.firstName,
                lastName = expectedUser.lastName,
                email = expectedUser.email,
                createdAt = expectedUser.createdAt,
                deletedAt = null
            ),
            actualUser
        )
    }

    @Test
    fun `Delete user by id`() {
        jdbi.fillTableWithUsers(3)

        val expectedUser = TestUser.create(id = 1, deleted = false).copy(
            deletedAt = 1672342506
        )

        usersDao.delete(
            id = 1,
            deletedAt = Timestamp(1672342506),
        )

        val actualUser = usersDao.getById(id = 1)

        assertEquals(
            UserEntity(
                id = expectedUser.id,
                firstName = expectedUser.firstName,
                lastName = expectedUser.lastName,
                email = expectedUser.email,
                createdAt = expectedUser.createdAt,
                deletedAt = expectedUser.deletedAt
            ),
            actualUser
        )
    }
}
