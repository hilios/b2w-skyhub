import java.nio.file.{Files, Paths}

import actors.ActorSpec
import actors.ImageProcessor.Fetch
import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.TestProbe
import dao.ImagesDAO
import models.Image
import org.bson.types.ObjectId
import org.mockito.Mockito._
import org.scalatest.TestData
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.{OneAppPerTest, PlaySpec}
import play.api.Application
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.ImagesService

import scala.concurrent.Future
import scala.concurrent.duration._

class ImagesSpec extends ActorSpec with OneAppPerTest with MockitoSugar {
  val imagesService = mock[ImagesService]
  when(imagesService.all) thenReturn Future.successful(Seq(
    "http://host.com/a.jpg", "http://host.com/b.jpg"
  ))

  val idA = new ObjectId()
  val idB = new ObjectId()
  val img = Files.readAllBytes(Paths.get("./test/resources/b737_5.jpg"))
  val imagesDAO = mock[ImagesDAO]
  when(imagesDAO.findAll) thenReturn Future.successful {
    Seq(
      Image(idA, "http://host.com/a.jpg", null, null, null),
      Image(idB, "http://host.com/b.jpg", null, null, null)
    )
  }
  when(imagesDAO.findById(idA)) thenReturn Future.successful {
    Image(idA, "http://host.com/a.jpg", img, img, img)
  }
  val fakeId = new ObjectId()
  when(imagesDAO.findById(fakeId)) thenReturn Future.successful(null)

  val probe = TestProbe()

  override def newAppForTest(testData: TestData): Application = {
    new GuiceApplicationBuilder()
      .overrides(bind[ImagesService].to(imagesService))
      .overrides(bind[ImagesDAO].to(imagesDAO))
      .overrides(bind[ActorSystem].to(system))
      .overrides(bind[ActorRef].qualifiedWith("thumbinator").to(probe.ref))
      .overrides(bind[ActorRef].qualifiedWith("image-processor").to(probe.ref))
      .build()
  }

  "GET /images" should {
    "render all images and its thumbs address" in {
      val images = route(app, FakeRequest(GET, "/images")).get

      status(images) mustBe OK
      contentType(images) mustBe Some("application/json")

      val output = contentAsJson(images)
      (output \ "results" \\ "url").map(_.as[String]) must contain allOf (
        "http://host.com/a.jpg",
        "http://host.com/b.jpg"
      )
      (output \ "results" \\ "small").map(_.as[String]) must contain allOf (
        s"http:///images/$idA/small.jpg",
        s"http:///images/$idB/small.jpg"
      )
      (output \ "results" \\ "medium").map(_.as[String]) must contain allOf (
        s"http:///images/$idA/medium.jpg",
        s"http:///images/$idB/medium.jpg"
      )
      (output \ "results" \\ "large").map(_.as[String]) must contain allOf (
        s"http:///images/$idA/large.jpg",
        s"http:///images/$idB/large.jpg"
      )
    }
  }

  "POST /images" should {
    "fetch process all images from the endpoint" in {
      val images = route(app, FakeRequest(POST, "/images")).get

      status(images) mustBe NO_CONTENT
      verify(imagesService).all()

      within(1 second) {
        probe.expectMsg(Fetch("http://host.com/a.jpg"))
        probe.expectMsg(Fetch("http://host.com/b.jpg"))
      }
    }
  }

  "GET /images/:id/:size.jpg" should {
    "render a image thumb" in {
      val image = route(app, FakeRequest(GET, s"/images/$idA/small.jpg")).get

      status(image) mustBe OK
      contentType(image) mustBe Some("image/jpg")
      contentAsBytes(image) mustBe img
    }

    "return 404 when image does not exists" in {
      val image = route(app, FakeRequest(GET, s"/images/$fakeId/small.jpg")).get

      status(image) mustBe NOT_FOUND
    }

    "return 404 when image size does not exists" in {
      val image = route(app, FakeRequest(GET, s"/images/$idA/largeeeee.jpg")).get

      status(image) mustBe NOT_FOUND
    }
  }
}
