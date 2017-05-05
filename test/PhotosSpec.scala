import org.scalatestplus.play.{OneAppPerTest, PlaySpec}
import play.api.test.FakeRequest
import play.api.test.Helpers._

class PhotosSpec extends PlaySpec with OneAppPerTest {

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
