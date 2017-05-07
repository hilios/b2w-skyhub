package models

import org.mongodb.scala.bson.ObjectId
import org.bson.types.{ObjectId => BsonObjectId}
import play.api.libs.json._

case class Image(_id: ObjectId, url: String, small: Array[Byte], medium: Array[Byte],
                 large: Array[Byte])

object Image {
  implicit object ObjectIDFormat extends Format[ObjectId] {
    def writes(objectId: ObjectId): JsValue = JsString(objectId.toString())
    def reads(json: JsValue): JsResult[ObjectId] = json match {
      case JsString(id) =>
        if (BsonObjectId.isValid(id)) {
          JsSuccess(new ObjectId(id))
        } else {
          JsError("Expected ObjectID as JsString")
        }
      case _ => JsError("Expected ObjectID as JsString")
    }
  }

  implicit val imageFormat = Json.format[Image]

  def apply(url: String, small: Array[Byte], medium: Array[Byte], large: Array[Byte]) =
    new Image(new ObjectId(), url, small, medium, large)
}

