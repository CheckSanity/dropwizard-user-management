package resources

import javax.ws.rs.GET
import javax.ws.rs.Path
@Path("/helloWorld")
class HelloWorldResource(private val property: String) {
    @GET
    fun helloWorld() = "Hello World $property :)"
}
