package dao

import javax.inject._

import models.Image
import org.mongodb.scala._
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.model.Filters._
import services.MongoService

class ImagesDAO @Inject()(mongo: MongoService) {
  val collection: MongoCollection[Image] = mongo.database.getCollection("images")

  def findAll() = collection.find().toFuture()

  def findById(id: ObjectId) = collection.find(equal("_id", id)).first().toFuture()

  def findByUrl(url: String*) = collection.find(equal("url", url)).first().toFuture()

  def insert(images: Image*) = collection.insertMany(images)
}
