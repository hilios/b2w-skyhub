package actors

import akka.actor._
import akka.util.ByteString
import com.sksamuel.scrimage.Image

class Thumbinator extends Actor {
  import actors.Thumbinator._

  def generateThumb(image: ByteString, width: Int, height: Int): Array[Byte] = {
    Image(image.toArray).fit(width, height).write
  }

  def receive = {
    case Large(image) =>
      sender() ! generateThumb(image, 640, 480)
    case Small(image) =>
      sender() ! generateThumb(image, 320, 240)
    case Medium(image) =>
      sender() ! generateThumb(image, 384, 288)
  }
}

object Thumbinator {
  def props = Props[Thumbinator]

  case class Small(image: ByteString)
  case class Large(image: ByteString)
  case class Medium(image: ByteString)
}
