package com.usermanagement.di

import com.usermanagement.AppConfiguration
import com.usermanagement.database.dao.IUsersDao
import com.usermanagement.database.dao.UsersDao
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

            bindSingleton<IUsersDao> {
                UsersDao(instance())
            }
        }
    }

    fun init(environment: Environment, configuration: AppConfiguration) {
        jdbi = JdbiFactory().build(environment, configuration.dataSource, "postgresql")
    }
}
