import io.dropwizard.Application
import io.dropwizard.setup.Environment
import resources.HelloWorldResource

class App : Application<AppConfiguration>() {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = App().run(*args)
    }

    override fun run(appConfiguration: AppConfiguration, environment: Environment) {
        environment.jersey().register(HelloWorldResource(appConfiguration.configTest))
    }
}
