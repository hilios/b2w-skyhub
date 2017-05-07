package controllers

import javax.inject._

import actors.ImageProcessor
import actors.ImageProcessor.Thumb
import akka.actor.ActorSystem
import dao.ImagesDAO
import models.{Envelope, Image}
import play.api.libs.json.Json
import play.api.mvc._
import services.ImagesService

import scala.concurrent.ExecutionContext

@Singleton
class ImagesController @Inject()(implicit context: ExecutionContext, system: ActorSystem,
                                 images: ImagesService, imagesDAO: ImagesDAO) extends Controller {

  val processor = system.actorOf(ImageProcessor.props, "image-processor")

  def read = Action.async {
    imagesDAO.all().toFuture().map { images =>
      val results = Envelope(images, images.length)
      Ok(Json.toJson(results))
    }
  }

  def update = Action.async {
    images.all().map { urls =>
      urls.foreach { url =>
        images.load(url).map(processor ! Thumb(_))
      }
      NoContent
    }
  }

}
