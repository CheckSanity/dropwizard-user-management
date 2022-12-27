package com.usermanagement.database.dao

import com.usermanagement.database.entity.User
import org.jdbi.v3.core.Jdbi

class UsersDao(database: Jdbi) : IUsersDao {
    private val dao = database.onDemand(IUsersDao::class.java)

    init {
        createTable()
    }

    override fun createTable() = dao.createTable()

    override fun getAll(): List<User> = dao.getAll()

    override fun insert(id: Int, name: String) = dao.insert(id = id, name = name)

    override fun findById(id: Int): User? = dao.findById(id = id)
}
