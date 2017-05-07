package actors

import akka.actor._
import akka.util.ByteString

class ImageProcessor extends Actor {
  import ImageProcessor._

  override def receive = {
    case Thumb(image) =>
      image.toArray
  }
}

object ImageProcessor {
  def props = Props[ImageProcessor]

  object Size extends Enumeration {
    type Size = Value
    val Small, Medium, Large = Value
  }

  case class Thumb(image: ByteString)
}
