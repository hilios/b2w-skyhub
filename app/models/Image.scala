package models

import org.mongodb.scala.bson.ObjectId
import play.api.libs.json.{Json, Writes}

case class Image(_id: ObjectId, url: String,
                 small: Array[Byte], medium: Array[Byte], large: Array[Byte])

object Image {
  implicit val imageWrites = new Writes[Image] {
    def writes(image: Image) = Json.obj(
      "_id" -> image._id.toString,
      "url" -> image.url
    )
  }
  def apply(url: String, small: Array[Byte], medium: Array[Byte], large: Array[Byte]) =
    new Image(new ObjectId(), url, small, medium, large)
}

