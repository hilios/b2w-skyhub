package services

import javax.inject.Inject

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.ws.WSClient

import scala.concurrent.Future

class ImagesService(ws: WSClient, baseUrl: String) {

  @Inject def this(ws: WSClient) = this(ws, "http://54.152.221.29")

  def all(): Future[Seq[String]] = {
    ws.url(baseUrl + "/images.json").get().map { response =>
      (response.json \ "images" \\ "url").map(_.as[String])
    }
  }

  def load(url: String) = ws.url(url).get().map(_.bodyAsBytes)
}