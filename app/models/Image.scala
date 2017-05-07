package models

import org.mongodb.scala.bson.ObjectId

case class Image(_id: ObjectId, url: String,
                 small: Array[Byte], medium: Array[Byte], large: Array[Byte])

object Image {
  def apply(url: String, small: Array[Byte], medium: Array[Byte], large: Array[Byte]) =
    new Image(new ObjectId(), url, small, medium, large)
}

