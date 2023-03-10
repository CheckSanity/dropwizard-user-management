package com.usermanagement

import com.usermanagement.api.resources.groups.GroupsResource
import com.usermanagement.api.resources.users.UsersResource
import com.usermanagement.di.AppModule
import com.usermanagement.repository.groups.IGroupsRepository
import com.usermanagement.repository.users.IUsersRepository
import io.dropwizard.Application
import io.dropwizard.configuration.EnvironmentVariableSubstitutor
import io.dropwizard.configuration.SubstitutingSourceProvider
import io.dropwizard.db.PooledDataSourceFactory
import io.dropwizard.migrations.MigrationsBundle
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import io.federecio.dropwizard.swagger.SwaggerBundle
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration
import org.kodein.di.instance

class App : Application<AppConfiguration>() {
    override fun initialize(bootstrap: Bootstrap<AppConfiguration>) {
        bootstrap.configurationSourceProvider = SubstitutingSourceProvider(
            bootstrap.configurationSourceProvider,
            EnvironmentVariableSubstitutor(false)
        )
        bootstrap.addBundle(object : MigrationsBundle<AppConfiguration>() {
            override fun getDataSourceFactory(configuration: AppConfiguration): PooledDataSourceFactory {
                return configuration.dataSource
            }
        })

        bootstrap.addBundle(object : SwaggerBundle<AppConfiguration>() {
            override fun getSwaggerBundleConfiguration(configuration: AppConfiguration): SwaggerBundleConfiguration =
                configuration.swaggerConfig
        })
    }

    override fun run(configuration: AppConfiguration, environment: Environment) {
        AppModule.init(configuration = configuration, environment = environment)

        val usersRepository: IUsersRepository by AppModule.di.instance()
        val groupsRepository: IGroupsRepository by AppModule.di.instance()

        environment.jersey().register(UsersResource(usersRepository = usersRepository))
        environment.jersey().register(GroupsResource(groupsRepository = groupsRepository))
    }


    companion object {
        private const val configuration =
            "src/main/resources/app-config.yml" // TODO Hide under environment variables

        @JvmStatic
        fun main(args: Array<String>) = App().run("server", configuration)
    }
}
