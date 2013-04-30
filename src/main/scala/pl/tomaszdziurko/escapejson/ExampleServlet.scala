package pl.tomaszdziurko.escapejson

import org.scalatra.json.{JValueResult, JacksonJsonSupport}
import org.json4s._
import java.util.Date
import org.scalatra._
import org.json4s.JsonAST.{JString, JValue}
import java.io.Writer
import scala.Some
import org.apache.commons.lang3.StringEscapeUtils._
import org.json4s.JsonAST.JString
import scala.Some


class ExampleServlet extends ScalatraServlet with JacksonJsonSupport with JValueResult {

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
      case Some(e) => NotEscapedJsonWrapper(e)
      case _ =>
    }
  }

  override def writeJson(json: JValue, writer: Writer) {
    (json \ "notEscapedData") match {     // check if son contains field 'notEscapedData'
      case JNothing => {                  // no wrapper, so we perform escaping
        val escapedJson = json.map((x: JValue) =>
          x match {
            case JString(y) => JString(escapeHtml4(y))
            case _ => x
          }
        )
        mapper.writeValue(writer, escapedJson)
      }
      case _ => {    // field 'notEscapedData' detected, unwrap and return clean object
        mapper.writeValue(writer, json \ "notEscapedData")
      }
    }
  }
}

case class Message(id: Int, text: String, author: String, created: Date)

case class NotEscapedJsonWrapper[T](notEscapedData: T)
