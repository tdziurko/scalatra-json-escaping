package pl.tomaszdziurko.escapejson

import org.scalatra.json.{JValueResult, JacksonJsonSupport}
import org.json4s.{DefaultFormats, Formats}
import java.util.Date
import org.scalatra._

class ExampleServlet() extends ScalatraServlet with JacksonJsonSupport with JValueResult {

  protected implicit val jsonFormats: Formats = DefaultFormats

  val messages = List(
    Message(1, "<script><alert>Script hacker</alert></script>", "Johny Hacker", new Date()),
    Message(2, "Decent message", "John Smith", new Date()),
    Message(3, "Message with <b>bolded fragment</b>", "Kate Norton", new Date())
  )

  before() {
    contentType = formats("json")
  }

  get("/") {
    messages
  }

  get("/:id") {
    val id = params("id").toInt;
    val messageOptional = messages.find((m: Message) => m.id == id)

    log("optional =" + messageOptional)

    messageOptional match {
      case Some(e) => e
      case _ =>
    }
  }
}

case class Message(id: Int, text: String, author: String, created: Date)
