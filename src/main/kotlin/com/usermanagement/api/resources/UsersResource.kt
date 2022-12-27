package com.usermanagement.api.resources

import com.usermanagement.database.dao.IUsersDao
import com.usermanagement.database.entity.User
import org.kodein.di.DI
import org.kodein.di.instance
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
class UsersResource(di: DI) {
    private val usersDao: IUsersDao by di.instance()

    @GET
    fun users(): List<User> = usersDao.getAll()

    @POST
    fun createUser(): User? = null
}
