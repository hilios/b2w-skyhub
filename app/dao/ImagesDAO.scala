package dao

import javax.inject._

import models.Image
import org.mongodb.scala._
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Projections._
import services.MongoService

class ImagesDAO @Inject()(mongo: MongoService) {
  val collection: MongoCollection[Image] = mongo.database.getCollection("images")
  val raw: MongoCollection[Document] = mongo.database.getCollection("images")

  def findAll() = collection.find().toFuture()

  def findById(id: ObjectId) = collection.find(equal("_id", id)).first().toFuture()

  def findByUrl(url: String) = raw.find(equal("url", url))
    .projection(exclude("small", "medium", "large")).first().toFuture()

  def insert(images: Image*) = collection.insertMany(images)
}
