import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.Configuration

class AppConfiguration(@JsonProperty("configTest") val configTest: String) : Configuration()
