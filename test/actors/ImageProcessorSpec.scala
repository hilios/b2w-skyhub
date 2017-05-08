package actors

import java.nio.file.{Files, Paths}

import actors.ImageProcessor.Fetch
import akka.actor._
import akka.testkit.TestActorRef
import akka.util.ByteString
import dao.ImagesDAO
import models.Image
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito._
import org.mongodb.scala.bson.collection.immutable.Document
import org.scalatest.BeforeAndAfterEach
import org.scalatest.mock.MockitoSugar
import services.ImagesService

import scala.concurrent.Future

class ImageProcessorSpec extends ActorSpec with MockitoSugar with BeforeAndAfterEach {

  val img = ByteString(Files.readAllBytes(Paths.get("./test/resources/b737_5.jpg")))
  val emptyImg = Array.ofDim[Byte](0)

  val thumbs = system.actorOf(Props(new Actor {
    def receive = {
      case _ => sender() ! emptyImg
    }
  }))

  val urlA = "http://host.com/a.jpg"
  val urlB = "http://host.com/b.jpg"
  val urlC = "http://host.com/c.jpg"

  val imagesService = mock[ImagesService]
  when(imagesService.load(urlA)) thenReturn Future.successful(img)
  when(imagesService.load(urlC)) thenReturn Future.failed(new Exception())

  val imagesDAO = mock[ImagesDAO]
  when(imagesDAO.insert(any[Image])) thenReturn Future.successful(null)

  when(imagesDAO.findByUrl(urlA)) thenReturn Future.successful(null)
  when(imagesDAO.findByUrl(urlB)) thenReturn Future.successful { Document("url" -> urlB) }
  when(imagesDAO.findByUrl(urlC)) thenReturn Future.successful(null)

  val processor = TestActorRef(Props(classOf[ImageProcessor], thumbs, imagesService, imagesDAO))

  override def afterEach() = {
    clearInvocations(imagesService, imagesDAO)
  }

  "Fetch(url)" should {
    "retrieve the image from url and execute the side-effects if image is not yet processed" in {
      processor ! Fetch(urlA)
      verify(imagesDAO).findByUrl(urlA)
      verify(imagesService).load(urlA)
      verify(imagesDAO).insert(any[Image])
    }

    "do nothing if image already exists" in {
      processor ! Fetch(urlB)
      verify(imagesService, never()).load(urlB)
      verify(imagesDAO, never()).insert(any[Image])
    }

    "do nothing if image cannot be loaded" in {
      processor ! Fetch(urlC)
      verify(imagesDAO, never()).insert(any[Image])
    }
  }
}
