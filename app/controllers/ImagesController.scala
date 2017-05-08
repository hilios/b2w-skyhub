package controllers

import javax.inject._

import actors.ImageProcessor.Fetch
import akka.actor.ActorRef
import dao.ImagesDAO
import models.{Envelope, Image}
import org.bson.types.ObjectId
import play.api.Configuration
import play.api.libs.json._
import play.api.mvc._
import services.ImagesService

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ImagesController @Inject()(@Named("image-processor") processor: ActorRef,
                                 images: ImagesService, imagesDAO: ImagesDAO,
                                 config: Configuration)
                                (implicit context: ExecutionContext) extends Controller {

  def list = Action.async { request =>
    val baseUrl = s"${if(request.secure) "https" else "http"}://${request.host}"

    implicit val imageWrites = new Writes[Image] {
      def writes(image: Image) = {
        val objectId = image._id.toString
        Json.obj(
          "_id" -> objectId,
          "url" -> image.url,
          "small" -> s"$baseUrl/images/${objectId}/small.jpg",
          "medium" -> s"$baseUrl/images/${objectId}/medium.jpg",
          "large" -> s"$baseUrl/images/${objectId}/large.jpg"
        )
      }
    }

    imagesDAO.findAll().map { images =>
      val results = Envelope(images)
      Ok(Json.toJson(results))
    }
  }

  def create = Action.async {
    images.all().map { urls =>
      urls foreach (processor ! Fetch(_))
      NoContent
    }
  }

  def read(id: String, size: String) = Action.async {
    val objectId = new ObjectId(id)
    imagesDAO.findById(objectId).map { image =>
      val thumb = size match {
        case "small" => Some(image.small)
        case "medium" => Some(image.medium)
        case "large" => Some(image.large)
        case _ => None
      }
      thumb match {
        case Some(x) =>
          Ok(x).as("image/jpg")
        case None =>
          NotFound
      }
    } fallbackTo {
      Future.successful(NotFound)
    }
  }
}
