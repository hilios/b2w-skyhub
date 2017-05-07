package dao

import javax.inject._

import models.Image
import org.mongodb.scala.MongoCollection
import services.MongoService

class ImagesDAO @Inject()(mongo: MongoService) {
  val collection: MongoCollection[Image] = mongo.database.getCollection("photos")

  def all() = collection.find()
}
