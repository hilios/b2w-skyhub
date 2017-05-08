package actors

import javax.inject._

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import dao.ImagesDAO
import models.Image
import org.mongodb.scala.Completed
import services.ImagesService

import scala.concurrent.duration._
import scala.util.{Failure, Success}

class ImageProcessor @Inject()(@Named("thumbs") thumb: ActorRef, images: ImagesService,
                               imagesDAO: ImagesDAO) extends Actor with ActorLogging {
  import ImageProcessor._
  import Thumbinator._
  import context.dispatcher

  implicit val timeout: Timeout = 5.minutes

  /**
    * Fetch the image at given URL and generate thumbs for them, assuring the idempotency of the URL
    * at the database to not process the same image more than once.
    */
  def receive = {
    case Fetch(url) =>
      log.info(s"Fetching image: $url")
      imagesDAO.findByUrl(url).map(Option(_)) andThen {
        case Success(image) =>
          if (image.isEmpty) {
            for {
              img <- images.load(url)
              sm <- (thumb ? Small(img)).mapTo[Array[Byte]]
              lg <- (thumb ? Large(img)).mapTo[Array[Byte]]
              md <- (thumb ? Medium(img)).mapTo[Array[Byte]]
            } yield {
              log.info(s"New image $url")
              val img = Image(url, sm, md, lg)
              imagesDAO.insert(img).onComplete {
                case Success(_) =>
                  log.info(s"Inserted new image $url")
                case Failure(_) =>
                  log.warning(s"Could not insert $url")
              }
            }
          } else {
            log.info(s"Image $url already exists")
          }

        case Failure(_) =>
          log.info(s"DB Error: Could not find $url")
      }
  }
}

object ImageProcessor {
  def props = Props[ImageProcessor]

  case class Fetch(url: String)
}
