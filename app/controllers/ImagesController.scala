package controllers

import javax.inject._

import actors.ImageProcessor.Fetch
import akka.actor.ActorRef
import dao.ImagesDAO
import models.{Envelope, Image}
import play.api.libs.json._
import play.api.mvc._
import services.ImagesService

import scala.concurrent.ExecutionContext

@Singleton
class ImagesController @Inject()(@Named("image-processor") processor: ActorRef,
                                 images: ImagesService, imagesDAO: ImagesDAO)
                                (implicit context: ExecutionContext) extends Controller {

  def read = Action.async {
    imagesDAO.findAll().map { images =>
      val results = Envelope(images)
      Ok(Json.toJson(results))
    }
  }

  def update = Action.async {
    images.all().map { urls =>
      urls foreach (processor ! Fetch(_))
      NoContent
    }
  }

}
