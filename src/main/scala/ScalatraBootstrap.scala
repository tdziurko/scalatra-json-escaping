import pl.tomaszdziurko.escapejson.ExampleServlet
import org.scalatra._
import javax.servlet.ServletContext


class ScalatraBootstrap extends LifeCycle {
  val Prefix = "/rest/"

  override def init(context: ServletContext) {
    context.mount(new ExampleServlet(), Prefix + "example")
    context.put("example", this)
  }


}
