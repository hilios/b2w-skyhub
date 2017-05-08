package actors

import java.nio.file.{Files, Paths}

import actors.Thumbinator._
import akka.pattern.ask
import akka.util.{ByteString, Timeout}
import com.sksamuel.scrimage.Image

import scala.concurrent.Await
import scala.concurrent.duration._

class ThumbinatorSpec extends ActorSpec {

  val img = ByteString(Files.readAllBytes(Paths.get("./test/resources/b737_5.jpg")))

  val thumbs = system.actorOf(Thumbinator.props)
  implicit val timeout: Timeout = 10.seconds

  "Small(image)" should {
    "return the given image resized to 320x240" in {
      val image = (thumbs ? Small(img)).mapTo[Array[Byte]]
      val small = Await.result(image, 10.seconds)

      val thumb = Image(small)
      thumb.width mustBe 320
      thumb.height mustBe 240
    }
  }

  "Medium(image)" should {
    "return the given image resized to 384x288" in {
      val image = (thumbs ? Medium(img)).mapTo[Array[Byte]]
      val small = Await.result(image, 10.seconds)

      val thumb = Image(small)
      thumb.width mustBe 384
      thumb.height mustBe 288
    }
  }

  "Large(image)" should {
    "return the given image resized to 640x480" in {
      val image = (thumbs ? Large(img)).mapTo[Array[Byte]]
      val small = Await.result(image, 10.seconds)

      val thumb = Image(small)
      thumb.width mustBe 640
      thumb.height mustBe 480
    }
  }
}
