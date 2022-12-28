package com.usermanagement.di

import com.usermanagement.AppConfiguration
import com.usermanagement.database.dao.GroupsDao
import com.usermanagement.database.dao.UsersDao
import com.usermanagement.repository.groups.GroupsRepository
import com.usermanagement.repository.groups.IGroupsRepository
import com.usermanagement.repository.users.IUsersRepository
import com.usermanagement.repository.users.UsersRepository
import io.dropwizard.jdbi3.JdbiFactory
import io.dropwizard.setup.Environment
import org.jdbi.v3.core.Jdbi
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

object AppModule {
    private lateinit var jdbi: Jdbi

    val di by lazy {
        DI {
            bindSingleton { jdbi }

            bindSingleton<IUsersRepository> {
                UsersRepository(usersDao = UsersDao(database = instance()))
            }

            bindSingleton<IGroupsRepository> {
                GroupsRepository(groupsDao = GroupsDao(database = instance()))
            }
        }
    }

    fun init(environment: Environment, configuration: AppConfiguration) {
        jdbi = JdbiFactory().build(environment, configuration.dataSource, "postgresql")
    }
}
