import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport

import actors._

class Module extends AbstractModule with AkkaGuiceSupport {
  def configure = {
    bindActor[ImageProcessor]("image-processor")
    bindActor[Thumbinator]("thumbinator")
  }
}