import org.scalatestplus.play._
import play.api.test._
import play.api.test.Helpers._

class ApplicationSpec extends PlaySpec with OneAppPerTest {

  "OpsController" should {
    "render the version" in {
      val home = route(app, FakeRequest(GET, "/ops")).get

      status(home) mustBe OK
      contentType(home) mustBe Some("application/json")
      contentAsString(home) must include ("version")
    }
  }

  "ErrorHandler" should {
    "send not found on a bad request" in  {
      val error = route(app, FakeRequest(GET, "/bad")).get

      status(error) mustBe NOT_FOUND
      contentType(error) mustBe Some("application/json")
      contentAsString(error) must include ("error")
    }
  }
}
