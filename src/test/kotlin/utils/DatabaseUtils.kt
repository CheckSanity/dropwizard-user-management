package utils

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.SqlObjectPlugin
import java.sql.DriverManager
import java.sql.Timestamp

object DatabaseUtils {
    fun createTestJdbi(): Jdbi {
        return Jdbi.create(
            DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/user_management_service_test",
                "user_management_user_test",
                "123456"
            )
        ).also { jdbi ->
            jdbi.installPlugin(SqlObjectPlugin())
        }
    }

    fun Jdbi.fillTableWithUsers(usersCount: Int = 10) {
        repeat(usersCount) {
            val testUser = TestUser.create(id = (it + 1), deleted = false)

            this.withHandle<Any, Exception> { handle ->
                handle.execute(
                    "INSERT INTO users (firstName, lastName, email, createdAt) VALUES ('${testUser.firstName}', '${testUser.lastName}', '${testUser.email}', '${
                        Timestamp(testUser.createdAt)
                    }')"
                )
            }
        }
    }
}
