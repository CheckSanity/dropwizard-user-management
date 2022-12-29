package com.usermanagement

import com.usermanagement.api.resources.users.UsersResource
import com.usermanagement.di.AppModule
import io.dropwizard.Application
import io.dropwizard.configuration.EnvironmentVariableSubstitutor
import io.dropwizard.configuration.SubstitutingSourceProvider
import io.dropwizard.db.PooledDataSourceFactory
import io.dropwizard.migrations.MigrationsBundle
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment

class App : Application<AppConfiguration>() {
    override fun initialize(bootstrap: Bootstrap<AppConfiguration?>) {
        bootstrap.configurationSourceProvider = SubstitutingSourceProvider(
            bootstrap.configurationSourceProvider,
            EnvironmentVariableSubstitutor(false)
        )
        bootstrap.addBundle(object : MigrationsBundle<AppConfiguration>() {
            override fun getDataSourceFactory(configuration: AppConfiguration): PooledDataSourceFactory {
                return configuration.dataSource
            }
        })
    }

    override fun run(configuration: AppConfiguration, environment: Environment) {
        AppModule.init(configuration = configuration, environment = environment)

        environment.jersey().register(UsersResource(AppModule.di))
    }


    companion object {
        private const val configuration =
            "src/main/resources/app-config.yml" // TODO Hide under environment variables

        @JvmStatic
        fun main(args: Array<String>) = App().run("server", configuration)
    }
}
