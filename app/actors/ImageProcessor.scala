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

class ImageProcessor @Inject()(images: ImagesService,
                               imagesDAO: ImagesDAO) extends Actor with ActorLogging {
  import ImageProcessor._
  import Thumbinator._
  import context.dispatcher

  val thumb = context.actorOf(Thumbinator.props, "thumbinator")
  implicit val timeout: Timeout = 5.minutes

  def receive = {
    case Fetch(url) =>
      log.info(s"Fetching image: $url")
      imagesDAO.findByUrl(url) andThen {
        case Success(img) =>
          val image = Option(img)
          if (image.isEmpty) {
            for {
              image <- images.load(url)
              sm <- (thumb ? Small(image)).mapTo[Array[Byte]]
              lg <- (thumb ? Large(image)).mapTo[Array[Byte]]
              md <- (thumb ? Medium(image)).mapTo[Array[Byte]]
            } yield {
              log.info(s"New image $url")
              val img = Image(url, sm, md, lg)
              imagesDAO.insert(img).subscribe(
                (c: Completed) => log.info(s"Inserted new image $url"))
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
