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
import scala.util.Try

@Singleton
class ImagesController @Inject()(@Named("image-processor") processor: ActorRef,
                                 images: ImagesService, imagesDAO: ImagesDAO)
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
    val image = for {
      objectId <- Future.fromTry(Try { new ObjectId(id) })
      image <- imagesDAO.findById(objectId)
    } yield Option(image)
    // Select the right thumb size
    image.map { image =>
      size match {
        case "small" => image.map(_.small)
        case "medium" => image.map(_.medium)
        case "large" => image.map(_.large)
        case _ => None
      }
    } map {
      case Some(thumb) =>
        Ok(thumb).as("image/jpg")
      case None =>
        NotFound
    }
  }
}
