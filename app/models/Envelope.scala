package models

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Envelope[T](results: Seq[T], total: Int)

object Envelope {
  implicit def envelopeFormat[T: Format]: Format[Envelope[T]] = (
      (__ \ "results").format[Seq[T]] ~
      (__ \ "total").format[Int]
  )(Envelope.apply, unlift(Envelope.unapply))
}
