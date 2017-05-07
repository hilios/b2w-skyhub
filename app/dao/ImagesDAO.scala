package dao

import javax.inject._

import models.Image
import org.mongodb.scala._
import org.mongodb.scala.model.Filters._
import services.MongoService

class ImagesDAO @Inject()(mongo: MongoService) {
  val collection: MongoCollection[Image] = mongo.database.getCollection("images")

  def findAll() = collection.find().toFuture()

  def findById(id: String) = ???

  def findByUrl(url: String*) = collection.find(equal("url", url)).first().toFuture()

  def insert(images: Image*) = collection.insertMany(images)
}
