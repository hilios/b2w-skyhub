package models

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Envelope[T](results: Seq[T])

object Envelope {
  implicit def envelopeWrites[T: Writes] = new Writes[Envelope[T]] {
    def writes(envelope: Envelope[T]) = Json.obj(
      "results" -> Json.toJson(envelope.results)
    )
  }
}
