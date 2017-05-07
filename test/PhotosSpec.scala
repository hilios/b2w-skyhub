import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.{OneAppPerTest, PlaySpec}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import org.mockito.Mockito._
import services.ImagesService

import scala.concurrent.Future

class PhotosSpec extends PlaySpec with OneAppPerTest with MockitoSugar {
  val imagesService = mock[ImagesService]
  when(imagesService.all) thenReturn Future {
    Seq("http://host.com/a.jpg", "http://host.com/b.jpg")
  }

  "PhotosController" should {
    "render all photos as json" in {
      val photos = route(app, FakeRequest(GET, "/photos")).get

      status(photos) mustBe NOT_IMPLEMENTED
    }

    "render update the photos" in {
      val photos = route(app, FakeRequest(PUT, "/photos")).get

      status(photos) mustBe NOT_IMPLEMENTED
    }
  }
}
